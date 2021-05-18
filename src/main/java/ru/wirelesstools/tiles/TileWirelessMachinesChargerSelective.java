package ru.wirelesstools.tiles;

import java.util.ArrayList;
import java.util.List;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.tile.IEnergyStorage;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileWirelessMachinesChargerSelective extends TileEntity implements IEnergySink, IInventory {

	private List<TileEntity> listofsinks = new ArrayList<>();

	protected int maxStorage;
	public double energy;
	protected int tier;
	public final String chargername;

	public TileWirelessMachinesChargerSelective(int maxStorage, int tier, String name) {
		this.energy = 0.0D;
		this.maxStorage = maxStorage;
		this.chargername = name;
		this.tier = tier;
	}

	public enum AddResult {
		ADDED, ALREADY_EXIST, NO_FREE_SLOT;
	}

	public AddResult tryAddMachineToList(World world, int x, int y, int z) {
		if (this.listofsinks.isEmpty()) {
			this.listofsinks.add(world.getTileEntity(x, y, z));
			return AddResult.ADDED;
		} else if (this.listofsinks.size() < 16) {
			for (TileEntity tileinlist : this.listofsinks) {
				if (tileinlist != null) {
					if (tileinlist.xCoord != x || tileinlist.yCoord != y || tileinlist.zCoord != z) {
						continue;
					}
					return AddResult.ALREADY_EXIST;
				}
			}
			this.listofsinks.add(world.getTileEntity(x, y, z));
			return AddResult.ADDED;
		} else {
			return AddResult.NO_FREE_SLOT;
		}
	}
	
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setDouble("energy", this.energy);
	}
	
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.energy = nbttagcompound.getDouble("energy");
	}
	
	public int getTileElectricCount() {
		
		return this.listofsinks.size();
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity arg0, ForgeDirection arg1) {

		return true;
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
		if (this.energy >= (double) this.maxStorage) {
			return amount;
		}
		this.energy += amount;
		return 0.0;
	}

}
