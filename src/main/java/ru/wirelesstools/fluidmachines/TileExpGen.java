package ru.wirelesstools.fluidmachines;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IEnergyStorage;
import ic2.core.IC2;
import ic2.core.util.EntityIC2FX;
import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.container.ContainerExpGen;
import ru.wirelesstools.packets.PacketPlayerStandOn;
import ru.wirelesstools.utils.ExperienceUtils;

public class TileExpGen extends TileEntity implements IEnergySink, IEnergyStorage, IInventory, IFluidHandler {

	public double energy = 0D;
	public int maxEnergy;
	private boolean addedToEnergyNet = false;
	private int tier;
	private boolean loaded;
	public FluidTank fluidTank;
	private EntityPlayer player = null;
	public boolean initialized;
	public String nametile;
	public boolean active;
	public boolean lastState;

	public TileExpGen(int maxenergy, int tier1, String tileName, int maxtankcapacity) {
		this.loaded = false;
		this.fluidTank = new FluidTank(1000 * maxtankcapacity);
		this.initialized = false;
		this.loaded = false;
		this.maxEnergy = maxenergy;
		this.tier = tier1;
		this.nametile = tileName;
		this.lastState = false;
		this.active = false;
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbt);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		this.readFromNBT(packet.func_148857_g());
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.active = nbttagcompound.getBoolean("active");
		this.energy = nbttagcompound.getDouble("energy");
		this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
	}

	public int gaugeLiquidScaled(int i) {
		if (getFluidTank().getFluidAmount() <= 0)
			return 0;

		return getFluidTank().getFluidAmount() * i / getFluidTank().getCapacity();
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setDouble("energy", this.energy);
		NBTTagCompound fluidTankTag = new NBTTagCompound();
		this.fluidTank.writeToNBT(fluidTankTag);
		nbttagcompound.setTag("fluidTank", fluidTankTag);
		nbttagcompound.setBoolean("active", this.active);

	}

	public void validate() {
		super.validate();
		onLoaded();
	}

	public void onLoaded() {
		if (!this.worldObj.isRemote) {
			MinecraftForge.EVENT_BUS.post((Event) new EnergyTileLoadEvent(this));
			this.addedToEnergyNet = true;
		}
		this.loaded = true;
	}

	public void invalidate() {
		if (this.loaded) {
			onUnloaded();
		}
		super.invalidate();
	}

	public void onUnloaded() {
		if (!this.worldObj.isRemote && this.addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post((Event) new EnergyTileUnloadEvent(this));
			this.addedToEnergyNet = false;
		}
		this.loaded = false;
		this.setActive(false);
	}

	public void updateEntity() {
		super.updateEntity();
		// this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		if (!this.initialized && this.worldObj != null) {
			intialize();
		}
		if (!this.worldObj.isRemote) {
			if (this.energy > this.maxEnergy) {
				this.energy = this.maxEnergy;
			}
			this.attemptGeneration();
			// System.out.println("Invokes after attemptgen");

			if (this.player != null & this.active) {
				this.transferExpToPlayer(this.player);
				this.player = null;
				// this.markDirty();
				// this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			} else {
				this.setActive(false);
				// this.markDirty();
				// this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			}
			// this.markDirty();
		}

	}

	protected void transferExpToPlayer(EntityPlayer entityplayer) {
		FluidStack fluid = getFluidTank().getFluid();
		// int currentXP = ExperienceUtils.getPlayerXP(entityplayer);
		// int nextLevelXP =
		// ExperienceUtils.getExperienceForLevel(entityplayer.experienceLevel + 1);
		// int requiredXP = nextLevelXP - currentXP;
		// int requiredXPJuice = 20 * requiredXP;
		if (fluid != null && fluid.isFluidEqual(new FluidStack(MainWI.FluidXP.xpJuice, 0))) {
			if (fluid.amount > 0) {

				// גלוסעמ 4 בûכמ requiredXPJuice
				int actuallydrained = Math.min(fluid.amount, 4);
				FluidStack drained = this.fluidTank.drain(actuallydrained, true);
				if (drained != null) {
					int xp = drained.amount;
					ExperienceUtils.addPlayerXP(entityplayer, xp);
				}
			}
		}
	}

	@SideOnly(value = Side.CLIENT)
	public void spawnParticles(World world, int blockX, int blockY, int blockZ, Random rand) {
		if (this.getActive()) {
			EffectRenderer effect = FMLClientHandler.instance().getClient().effectRenderer;
			for (int particles = 20; particles > 0; --particles) {
				double x = (float) blockX + 0.0f + rand.nextFloat();
				double y = (float) blockY + 0.9f + rand.nextFloat();
				double z = (float) blockZ + 0.0f + rand.nextFloat();
				effect.addEffect(new EntityIC2FX(world, x, y, z, 60, new double[] { 0.0, 0.1, 0.0 },
						new float[] { 0.5F, 0.72F, 0F }));
			}
		}
	}

	public void setActive(boolean bool) {
		if (this.active != bool) {
			this.active = bool;
			this.markDirty();
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	public boolean getActive() {

		return this.active;
	}

	public void isPlayerStandingOnBlock(EntityPlayer entity, int x, int y, int z, World world) {
		if (this.player == null || this.player.getUniqueID() != entity.getUniqueID()) {
			this.player = entity;
			this.setActive(true);
		}
	}

	public void intialize() {
		this.initialized = true;
		if (!this.addedToEnergyNet) {
			this.onLoaded();
		}
	}

	public void attemptGeneration() {
		if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
			return;
		}
		if (this.energy >= 2000) {
			if (this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {
				this.fluidTank.fill(new FluidStack(MainWI.FluidXP.xpJuice, 1), true);
				this.energy -= 2000;
			}
		}
	}

	public Container getGuiContainer(InventoryPlayer inventory) {

		return new ContainerExpGen(inventory, this);
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity nameTileEntity, ForgeDirection nameForgeDirection) {

		return true;
	}

	@Override
	public double getDemandedEnergy() {
		return (double) this.maxEnergy - this.energy;
	}

	@Override
	public int getSinkTier() {

		return this.tier;
	}

	@Override
	public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
		if (this.energy >= this.maxEnergy) {
			return amount;
		}
		this.energy += amount;
		return 0.0D;
	}

	public FluidTank getFluidTank() {
		return this.fluidTank;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!canFill(from, resource.getFluid()))
			return 0;
		// markDirty();

		return getFluidTank().fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (resource == null || !resource.isFluidEqual(getFluidTank().getFluid())) {
			return null;
		}
		// markDirty();

		if (!canDrain(from, resource.getFluid()))
			return null;

		return getFluidTank().drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (!canDrain(from, null))
			return null;
		// markDirty();
		return getFluidTank().drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {

		return (fluid == MainWI.FluidXP.xpJuice);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {

		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { getFluidTank().getInfo() };
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

		return true;
	}

	@Override
	public int addEnergy(int amount) {

		this.energy += amount;

		return getStored();
	}

	@Override
	public int getCapacity() {

		return this.maxEnergy;
	}

	@Override
	public int getOutput() {

		return 128;
	}

	@Override
	public double getOutputEnergyUnitsPerTick() {

		return 128;
	}

	@Override
	public int getStored() {

		return (int) this.energy;
	}

	@Override
	public boolean isTeleporterCompatible(ForgeDirection arg0) {

		return false;
	}

	@Override
	public void setStored(int energy) {

		this.energy = energy;
	}

}
