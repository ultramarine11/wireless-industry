package ru.wirelesstools.tiles;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
    protected boolean addedToEnergyNet = false;
    private double chargeRate;
    private short mode;
    
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
        this.chargeRate = 512.0;
        this.mode = 0;
    }
    
    public int gaugeEUScaled(int i) {
        return (int)(this.energyEU * i / this.maxStorageEU);
    }
    
    public int gaugeRFScaled(int i) {
        return (int)((double)this.energyRF * i / this.maxStorageRF);
    }
    
    public boolean isChargingEU() {
        return this.chargeEU;
    }
    
    public boolean isChargingRF() {
        return this.chargeRF;
    }
    
    @Override
    public double getChargeRate() {
        return this.chargeRate;
    }
    
    @Override
    public short getMode() {
        return this.mode;
    }
    
    private void incrementChargeRate() {
        if(++this.mode > 8) // 8 = automatic mode
            this.mode = 0;
        switch(this.mode) {
            case 0:
                this.chargeRate = 512.0;
                break;
            case 1:
                this.chargeRate = 1024.0;
                break;
            case 2:
                this.chargeRate = 2048.0;
                break;
            case 3:
                this.chargeRate = 4096.0;
                break;
            case 4:
                this.chargeRate = 8192.0;
                break;
            case 5:
                this.chargeRate = 16384.0;
                break;
            case 6:
                this.chargeRate = 32768.0;
                break;
            case 7:
                this.chargeRate = 65536.0;
                break;
        }
    }
    
    private void decrementChargeRate() {
        if(--this.mode < 0)
            this.mode = 8; // 8 = automatic mode
        switch(this.mode) {
            case 0:
                this.chargeRate = 512.0;
                break;
            case 1:
                this.chargeRate = 1024.0;
                break;
            case 2:
                this.chargeRate = 2048.0;
                break;
            case 3:
                this.chargeRate = 4096.0;
                break;
            case 4:
                this.chargeRate = 8192.0;
                break;
            case 5:
                this.chargeRate = 16384.0;
                break;
            case 6:
                this.chargeRate = 32768.0;
                break;
            case 7:
                this.chargeRate = 65536.0;
                break;
        }
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
    
    public ItemStack getWrenchDrop(EntityPlayer player) {
        ItemStack drop = super.getWrenchDrop(player);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
        nbt.setDouble("energyEU", this.energyEU);
        nbt.setInteger("energyRF", this.energyRF);
        nbt.setBoolean("chargeEU", this.chargeEU);
        nbt.setBoolean("chargeRF", this.chargeRF);
        nbt.setDouble("chargeRate", this.chargeRate);
        nbt.setShort("chargerMode", this.mode);
        return drop;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("energyEU", this.energyEU);
        nbt.setInteger("energyRF", this.energyRF);
        nbt.setBoolean("chargeEU", this.chargeEU);
        nbt.setBoolean("chargeRF", this.chargeRF);
        nbt.setDouble("chargeRate", this.chargeRate);
        nbt.setShort("chargerMode", this.mode);
    }
    
    @Override
    public String getInventoryName() {
        return null;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energyEU = nbt.getDouble("energyEU");
        this.energyRF = nbt.getInteger("energyRF");
        this.chargeEU = nbt.getBoolean("chargeEU");
        this.chargeRF = nbt.getBoolean("chargeRF");
        this.chargeRate = nbt.getDouble("chargeRate");
        this.mode = nbt.getShort("chargerMode");
    }
    
    public void decreaseEnergy(double amount) {
        this.energyEU -= Math.min(this.energyEU, amount);
    }
    
    @Override
    public void decreaseEnergyRF(int amount) {
        this.energyRF -= Math.min(this.energyRF, amount);
    }
    
    @Override
    public World getChargerWorld() {
        return this.worldObj;
    }
    
    @Override
    public int getXCoord() {
        return this.xCoord;
    }
    
    @Override
    public int getZCoord() {
        return this.zCoord;
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
        }
        else {
            this.energyEU += amount;
            return 0.0;
        }
    }
    
    @Override
    public double getChargerEnergyEU() {
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
            case 2:
                this.incrementChargeRate();
                break;
            case 3:
                this.decrementChargeRate();
                break;
        }
    }
    
    @Override
    public ContainerBase<TileWirelessMachinesChargerBase> getGuiContainer(EntityPlayer player) {
        return new ContainerWirelessMachinesChargerNew(player, this);
    }
    
    @SideOnly(value = Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiWirelessMachinesChargerNew(new ContainerWirelessMachinesChargerNew(player, this));
    }
    
    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }
}
