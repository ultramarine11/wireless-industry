package ru.wirelesstools.tiles;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerVajraCharger;

public class TileVajraChargerElectric extends TileEntity implements IEnergySink, IInventory {
    
    public int tier;
    public int maxStorage;
    public double energy = 0;
    private boolean addedToEnergyNet;
    public boolean loaded = false;
    
    public TileVajraChargerElectric(int tier1, int storage1) {
        this.tier = tier1;
        this.maxStorage = storage1;
    }
    
    public void validate() {
        super.validate();
        this.onLoaded();
    }
    
    public void onLoaded() {
        if(!this.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
        this.loaded = true;
    }
    
    public void invalidate() {
        if(this.loaded) {
            this.onUnloaded();
        }
        super.invalidate();
    }
    
    public void decreaseEnergy(double amountdecrease) {
        this.energy -= Math.min(this.energy, amountdecrease);
    }
    
    public void onUnloaded() {
        if(!this.worldObj.isRemote && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        this.loaded = false;
    }
    
    public Container getGuiContainer(InventoryPlayer inventoryplayer) {
        return new ContainerVajraCharger(inventoryplayer, this);
    }
    
    public int gaugeEnergyScaled(int i) {
        return (int)(this.energy * i / this.maxStorage);
    }
    
    public void updateEntity() {
        super.updateEntity();
        
        if(this.worldObj.isRemote) {
            return;
        }
        
        if(this.energy > this.maxStorage) {
            this.energy = this.maxStorage;
        }
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return (player.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
    }
    
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy = nbttagcompound.getDouble("energy");
    }
    
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy", this.energy);
    }
    
    @Override
    public boolean acceptsEnergyFrom(TileEntity nameTileEntity, ForgeDirection nameForgeDirection) {
        return true;
    }
    
    @Override
    public double getDemandedEnergy() {
        return (double)this.maxStorage - this.energy;
    }
    
    @Override
    public int getSinkTier() {
        return this.tier;
    }
    
    @Override
    public double injectEnergy(ForgeDirection nameForgeDirection, double amount, double voltage) {
        double de = this.getDemandedEnergy();
        if(amount == 0.0D) {
            return 0.0D;
        }
        else if(de <= 0.0D) {
            return amount;
        }
        else if(amount >= de) {
            this.energy += de;
            return amount - de;
        }
        else {
            this.energy += amount;
            return 0.0D;
        }
    }
    
    @Override
    public int getSizeInventory() {
        return 0;
    }
    
    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return null;
    }
    
    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }
    
    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
    }
    
    @Override
    public String getInventoryName() {
        return null;
    }
    
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 0;
    }
    
    @Override
    public void openInventory() {
    }
    
    @Override
    public void closeInventory() {
    }
    
    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
    
}
