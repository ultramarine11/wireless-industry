package ru.wirelesstools.tiles;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class PFPConvertorTile extends TileEntity implements IEnergySink, IInventory, IHasGui, INetworkClientTileEntityEventListener {

    public int maxStorage;
    public double energy;
    public int tier;
    private boolean loaded = false;
    private boolean addedToEnergyNet;
    public String pfpconvertorname;
    protected int energyformaxheat;

    public PFPConvertorTile(String name) {
        this.energy = 0.0;
        this.pfpconvertorname = name;
    }

    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {

            // TODO some logic

            this.markDirty();
        }
    }

    public void validate() {
        super.validate();
        if (!this.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
        this.loaded = true;
    }

    public void invalidate() {
        if (this.loaded) {
            if (!this.worldObj.isRemote && this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                this.addedToEnergyNet = false;
            }
            this.loaded = false;
        }
        super.invalidate();
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
        if (amount == 0D)
            return 0D;
        if (this.energy >= this.maxStorage)
            return amount;

        double demanded = this.getDemandedEnergy();
        if (demanded > 0.0)
            this.energy += Math.min(amount, demanded);

        return 0.0D;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {

        return true;
    }

    @Override
    public void onNetworkEvent(EntityPlayer entityPlayer, int i) {
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {

        return null;
    }

    @Override
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {

        return null;
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
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
    public boolean isUseableByPlayer(EntityPlayer player) {

        return player.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }
}
