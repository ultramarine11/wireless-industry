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
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.container.ContainerWirelessMachinesChargerNew;
import ru.wirelesstools.gui.GuiWirelessMachinesChargerNew;
import ru.wirelesstools.utils.WirelessUtil;

public class TileWirelessMachinesChargerBase extends TileEntityInventory
        implements IEnergySink, IWirelessMachineCharger, IEnergyReceiver, INetworkClientTileEntityEventListener, IHasGui {

    protected int maxStorage;
    public double energy;
    protected int tier;
    public String chargername;
    private boolean addedToEnergyNet = false;

    private boolean chargeEU;
    private boolean chargeRF;

    public TileWirelessMachinesChargerBase(int maxStorage, int tier, String name) {
        this.energy = 0.0D;
        this.maxStorage = maxStorage;
        this.chargername = name;
        this.tier = tier;
        this.chargeEU = true;
        this.chargeRF = true;
    }

    public int gaugeEnergyScaled(int i) {
        return (int) (this.energy * i / this.maxStorage);
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

        if(this.energy > this.maxStorage) {
            this.energy = this.maxStorage;
            this.markDirty();
        }

        if(this.energy > 0.0)
            WirelessUtil.iterateIEnergySinkTiles(this, this.chargeEU, this.chargeRF);
    }

    /*@Override
    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote)
            return;

        if(this.energy > this.maxStorage) {
            this.energy = this.maxStorage;
            this.markDirty();
        }

        if(this.energy > 0.0)
            WirelessUtil.iterateIEnergySinkTiles(this, this.chargeEU, this.chargeRF);
    }*/

    /*public void validate() {
        super.validate();
        if(!this.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
        this.loaded = true;
    }*/

    /*public void invalidate() {
        if(this.loaded) {
            if(!this.worldObj.isRemote && this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                this.addedToEnergyNet = false;
            }
            this.loaded = false;
        }
        super.invalidate();
    }*/

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy", this.energy);
        nbttagcompound.setInteger("maxenergy", this.maxStorage);
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
        this.energy = nbttagcompound.getDouble("energy");
        this.maxStorage = nbttagcompound.getInteger("maxenergy");
        this.chargeEU = nbttagcompound.getBoolean("chargeEU");
        this.chargeRF = nbttagcompound.getBoolean("chargeRF");
    }

    public void decreaseEnergy(double amountdecrease) {
        this.energy -= Math.min(this.energy, amountdecrease);
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return true;
    }

    @Override
    public double getDemandedEnergy() {
        return (double) this.maxStorage - this.energy;
    }

    @Override
    public int getSinkTier() {
        return this.tier;
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        double de = this.getDemandedEnergy();
        if(amount == 0.0D) {
            return 0.0D;
        } else if(de <= 0.0D) {
            return amount;
        } else if(amount >= de) {
            this.energy += de;
            return amount - de;
        } else {
            this.energy += amount;
            return 0.0D;
        }
    }

    @Override
    public double getChargerEnergy() {
        return this.energy;
    }

    public int getMaxChargerEnergy() {
        return this.maxStorage;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection dir) {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int energyReceivedRF = Math.min(this.getMaxEnergyStored(from) - this.getEnergyStored(from), maxReceive);
        if(!simulate) {
            this.addRfEnergy(energyReceivedRF);
        }
        return energyReceivedRF;
    }

    @Override
    public int getEnergyStored(ForgeDirection var1) {
        return (int) this.energy * ConfigWI.EUToRF_Multiplier;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection var1) {
        return this.maxStorage * ConfigWI.EUToRF_Multiplier;
    }

    protected void addRfEnergy(int RFamount) {
        this.energy += (double) RFamount / ConfigWI.EUToRF_Multiplier;
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
