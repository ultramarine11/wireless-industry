package ru.wirelesstools.tiles;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.container.ContainerXPSenderNew;
import ru.wirelesstools.gui.GuiXPSenderNew;
import ru.wirelesstools.utils.ExperienceUtils;

public class TileXPSenderElectric extends TileEntity
		implements IEnergySink, IInventory, IHasGui, INetworkClientTileEntityEventListener {

	public int maxStorage;
	public double energy;
	protected int tier;
	private boolean loaded = false;
	private boolean addedToEnergyNet;
	public String xpsendername;
	protected int sendradius = 5;
	protected int pointsxp;
	private int energyperpoint;
	protected int playercountinradius = 0;

	public TileXPSenderElectric(int maxstorage, int tier, String name, int xp, int energyperpoint) {
		this.tier = tier;
		this.maxStorage = maxstorage;
		this.xpsendername = name;
		this.pointsxp = xp;
		this.energyperpoint = energyperpoint;
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

	public void updateEntity() {
		super.updateEntity();
		if (this.worldObj.isRemote) {
			return;
		}

		if (this.energy > this.maxStorage) {

			this.energy = this.maxStorage;
		}
		
		this.countPlayers(this.sendradius);
		
	}
	
	protected void consumeEnergyXPSender() {
		double localenergy = this.energy;
		localenergy -= (double)this.getTotalSpentEUValue();
		if(localenergy < 0) {
			localenergy = 0;
		}
		this.energy = localenergy;
	}

	public int getTotalSpentEUValue() {

		return this.energyperpoint * this.pointsxp;
	}

	protected void countPlayers(int radius) {
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.xCoord - radius, this.yCoord - radius,
				this.zCoord - radius, this.xCoord + radius, this.yCoord + radius, this.zCoord + radius);
		List<EntityPlayer> list = this.getWorldObj().getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
		if ((this.worldObj.getTotalWorldTime() % (ConfigWI.secondsXPSender * 20)) == 0) {

			this.sendXPToPlayersAround(radius, this.pointsxp, list);
		}
		
		this.playercountinradius = list.size();
	}

	protected void sendXPToPlayersAround(int radius, int points, List<EntityPlayer> list) {
		for (EntityPlayer player : list) {
			if (player != null) {
				if (this.energy > this.getTotalSpentEUValue()) {
					player.addExperience(points);
					// ExperienceUtils.addPlayerXP(player, points);
					this.consumeEnergyXPSender();
				}
			}
		}
	}

	public void changeRadius(int value) {
		int localradius = this.sendradius;
		if (value < 0) {
			localradius += value;
			if (localradius < 1) {
				localradius = 1;
			}

		} else if (value > 0) {
			localradius += value;
			if (localradius > 25) {
				localradius = 25;
			}
		}

		this.sendradius = localradius;
	}

	public int getSendRadius() {

		return this.sendradius;
	}

	public int getPlayerCount() {

		return this.playercountinradius;
	}

	public int gaugeEnergyScaled(int i) {

		return (int) (this.energy * i / this.maxStorage);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.energy = nbttagcompound.getDouble("energy");
		this.sendradius = nbttagcompound.getInteger("radius");

	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setDouble("energy", this.energy);
		nbttagcompound.setInteger("radius", this.sendradius);

	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity tile, ForgeDirection dir) {

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

		return (player.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
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

	@SideOnly(value = Side.CLIENT)
	public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {

		return new GuiXPSenderNew(new ContainerXPSenderNew(player, this));
	}

	@Override
	public ContainerBase<?> getGuiContainer(EntityPlayer player) {

		return new ContainerXPSenderNew(player, this);
	}

	@Override
	public void onGuiClosed(EntityPlayer player) {

	}

	@Override
	public void onNetworkEvent(EntityPlayer player, int event) {
		switch (event) {
		case 0:
			this.changeRadius(1);
			break;
		case 1:
			this.changeRadius(-1);
			break;
		}
	}

}
