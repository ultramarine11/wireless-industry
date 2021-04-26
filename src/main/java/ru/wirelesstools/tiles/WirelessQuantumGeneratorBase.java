package ru.wirelesstools.tiles;

import java.util.List;
import java.util.Vector;

import com.mojang.authlib.GameProfile;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import ic2.core.network.NetworkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerQGenWireless;
import ru.wirelesstools.handlerwireless.WirelessTransfer;
import ru.wirelesstools.utils.WirelessUtil;

public class WirelessQuantumGeneratorBase extends TileEntity
		implements IEnergySource, INetworkDataProvider, INetworkUpdateListener, IInventory {

	private boolean addedToEnergyNet;
	private boolean loaded = false;
	protected int output;
	protected int tier;
	protected GameProfile owner = null;

	public String wirelessQGenName;
	protected int wirelesstransferlimit;

	public boolean isCharging = false;

	public WirelessQuantumGeneratorBase(int output, int tier, String name, int limit) {
		this.output = output;
		this.tier = tier;
		this.wirelessQGenName = name;
		this.wirelesstransferlimit = limit;
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
	public void updateEntity() {
		super.updateEntity();
		if (!this.worldObj.isRemote) {

			this.isCharging = WirelessUtil.iterateIEnergySinkTilesQGenBool(this);

			this.operateWirelessTransferFromQGen();

			this.markDirty();
		}
	}

	public Container getGuiContainer(InventoryPlayer inventoryplayer) {

		return new ContainerQGenWireless(inventoryplayer, this);
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if (this.owner != null) {
			NBTTagCompound ownerNbt = new NBTTagCompound();
			NBTUtil.func_152460_a(ownerNbt, this.owner);
			nbttagcompound.setTag("ownerGameProfile", ownerNbt);
		}
		nbttagcompound.setBoolean("isCharging", this.isCharging);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("ownerGameProfile")) {
			this.owner = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("ownerGameProfile"));
		}

		this.isCharging = nbttagcompound.getBoolean("isCharging");
	}

	protected void operateWirelessTransferFromQGen() {
		if (!this.isInvalid()) {
			if (TileWirelessStorageBasePersonal.mapofThis.containsKey(true) & TileWirelessStorageBasePersonal.mapofThis
					.containsValue(TileWirelessStorageBasePersonal.listofstorages)) {
				if (!(TileWirelessStorageBasePersonal.mapofThis.get(true).isEmpty())) {
					for (TileWirelessStorageBasePersonal te : TileWirelessStorageBasePersonal.mapofThis.get(true)) {
						if (areSameOwners(this.owner, te.owner)) {

							WirelessTransfer.transmithandler.transmitEnergyWireleslyQGen(te, this);
						}
					}
				}
			}
		}
	}

	public void setPlayerProfile(GameProfile profile) {
		this.owner = profile;
		IC2.network.get().updateTileEntityField(this, "owner");
	}

	private static boolean areSameOwners(GameProfile id1, GameProfile id2) {

		return ((id1 != null) && (id2 != null) && (id1.equals(id2))) || (id1 == id2);
	}

	public int getWirelessTransferLimitQGen() {

		return wirelesstransferlimit;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity arg0, ForgeDirection arg1) {

		return true;
	}

	@Override
	public void drawEnergy(double arg0) {

		// TODO nothing here
	}

	@Override
	public double getOfferedEnergy() {

		return this.output;
	}
	
	public int getOutput() {
		
		return this.output;
	}

	@Override
	public int getSourceTier() {

		return this.tier;
	}

	public GameProfile getOwner() {

		return this.owner;
	}

	public boolean permitsAccess(GameProfile profile) {
		if (profile == null)
			return (this.owner == null);

		if (!this.worldObj.isRemote) {
			if (this.owner == null) {
				this.owner = profile;
				IC2.network.get().updateTileEntityField(this, "owner");
				return true;
			}
		}
		return this.owner.equals(profile);
	}

	@Override
	public void onNetworkUpdate(String arg0) {

	}

	@Override
	public List<String> getNetworkedFields() {
		List<String> ret = new Vector<String>(1);
		ret.add("owner");
		return ret;
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

}
