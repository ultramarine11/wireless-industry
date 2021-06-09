package ru.wirelesstools.tiles;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerWirelessMachinesCharger;
import ru.wirelesstools.utils.WirelessUtil;

public class TileWirelessMachinesChargerBase extends TileEntity
		implements IEnergySink, IEnergyStorage, IWirelessMachineCharger, IInventory {

	protected int maxStorage;
	public double energy;
	protected int tier;
	private boolean loaded = false;
	private boolean addedToEnergyNet;
	public String chargername;

//	public boolean isCharging = false;

	public TileWirelessMachinesChargerBase(int maxStorage, int tier, String name) {
		this.energy = 0.0D;
		this.maxStorage = maxStorage;
		this.chargername = name;
		this.tier = tier;
	}

	public int gaugeEnergyScaled(int i) {

		return (int) (this.energy * i / this.maxStorage);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.worldObj.isRemote) {
			return;
		}

		if (this.energy > this.maxStorage) {
			this.energy = this.maxStorage;

			this.markDirty();
		}

		if (this.energy > 0.0) {

			WirelessUtil.iterateIEnergySinkTiles(this);

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

	public Container getGuiContainer(InventoryPlayer inventory) {

		return new ContainerWirelessMachinesCharger(inventory, this);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setDouble("energy", this.energy);
		nbttagcompound.setInteger("maxenergy", this.maxStorage);
		// nbttagcompound.setBoolean("ischarging", this.isCharging);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.energy = nbttagcompound.getDouble("energy");
		this.maxStorage = nbttagcompound.getInteger("maxenergy");
		// this.isCharging = nbttagcompound.getBoolean("ischarging");
	}

	public void decreaseEnergy(double amountdecrease) {

		double realdecrease = Math.min(this.energy, amountdecrease);
		this.energy -= realdecrease;
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
		if (amount == 0D)
			return 0;
		if (this.energy >= this.maxStorage)
			return amount;

		this.energy += Math.min(amount, this.getDemandedEnergy());
		return 0.0D;
	}

	@Override
	public int getStored() {

		return (int) this.energy;
	}

	@Override
	public void setStored(int energy) {

		this.energy = energy;
	}

	@Override
	public int addEnergy(int amount) {
		this.energy += (double) amount;
		return amount;
	}

	@Override
	public int getCapacity() {

		return this.maxStorage;
	}

	@Override
	public int getOutput() {

		return 0;
	}

	@Override
	public double getOutputEnergyUnitsPerTick() {

		return 0;
	}

	@Override
	public boolean isTeleporterCompatible(ForgeDirection side) {

		return false;
	}

	@Override
	public double getChargerEnergy() {

		return this.energy;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {

		return player.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
