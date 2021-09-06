package ru.wirelesstools.tiles;

import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerWirelessMachinesChargerNew;
import ru.wirelesstools.gui.GuiWirelessMachinesChargerNew;
import ru.wirelesstools.utils.WirelessUtil;

public class TileWirelessMachinesChargerBase extends TileEntityInventory
        implements IEnergySink, IWirelessMachineCharger, IEnergyReceiver, INetworkClientTileEntityEventListener, IHasGui {

    public int energyRF;
    protected int maxStorageRF;
    protected double maxStorageEU;
    public double energyEU;
    protected int tier;
    public String chargername;
    private boolean addedToEnergyNet = false;

    private boolean chargeEU;
    private boolean chargeRF;

    public TileWirelessMachinesChargerBase(double maxStorageEU, int maxStorageRF, int tier, String name) {
        this.energyEU = 0.0;
        this.maxStorageEU = maxStorageEU;
        this.energyRF = 0;
        this.maxStorageRF = maxStorageRF;
        this.chargername = name;
        this.tier = tier;
        this.chargeEU = true;
        this.chargeRF = true;
    }

    public int gaugeEUScaled(int i) {
        return (int) (this.energyEU * i / this.maxStorageEU);
    }

    public int gaugeRFScaled(int i) {
        return (int) ((double) this.energyRF * i / this.maxStorageRF);
    }

    public boolean isChargingEU() {
        return this.chargeEU;
    }

    public boolean isChargingRF() {
        return this.chargeRF;
    }

    public void onLoaded() {
        super.onLoaded();
        if(IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    public void onUnloaded() {
        if(IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        WirelessUtil.iterateEnergyTiles(this, this.chargeEU, this.chargeRF);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy", this.energyEU);
        nbttagcompound.setInteger("energyRF", this.energyRF);
        nbttagcompound.setBoolean("chargeEU", this.chargeEU);
        nbttagcompound.setBoolean("chargeRF", this.chargeRF);
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energyEU = nbttagcompound.getDouble("energy");
        this.energyRF = nbttagcompound.getInteger("energyRF");
        this.chargeEU = nbttagcompound.getBoolean("chargeEU");
        this.chargeRF = nbttagcompound.getBoolean("chargeRF");
    }

    public void decreaseEnergy(double amountdecrease) {
        this.energyEU -= Math.min(this.energyEU, amountdecrease);
    }

    @Override
    public void decreaseEnergyRF(int amount) {
        this.energyRF -= amount;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return true;
    }

    @Override
    public double getDemandedEnergy() {
        return this.maxStorageEU - this.energyEU;
    }

    @Override
    public int getSinkTier() {
        return this.tier;
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        double de = this.getDemandedEnergy();
        if(amount == 0.0)
            return 0.0;
        else if(de <= 0.0)
            return amount;
        else if(amount >= de) {
            this.energyEU += de;
            return amount - de;
        } else {
            this.energyEU += amount;
            return 0.0;
        }
    }

    @Override
    public double getChargerEnergy() {
        return this.energyEU;
    }

    @Override
    public int getChargerEnergyRF() {
        return this.energyRF;
    }

    public double getMaxChargerEUEnergy() {
        return this.maxStorageEU;
    }

    public int getMaxChargerRFEnergy() {
        return this.maxStorageRF;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection dir) {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int energyReceivedRF = Math.min(this.getMaxEnergyStored(from) - this.getEnergyStored(from), maxReceive);
        if(!simulate)
            this.energyRF += energyReceivedRF;
        return energyReceivedRF;
    }

    @Override
    public int getEnergyStored(ForgeDirection var1) {
        return this.energyRF;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection var1) {
        return this.maxStorageRF;
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int id) {
        switch(id) {
            case 0:
                this.chargeEU = !this.chargeEU;
                break;
            case 1:
                this.chargeRF = !this.chargeRF;
                break;
        }
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerWirelessMachinesChargerNew(entityPlayer, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiWirelessMachinesChargerNew(new ContainerWirelessMachinesChargerNew(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }
}
