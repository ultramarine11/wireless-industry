package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import ic2.core.block.personal.IPersonalBlock;
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
import ru.wirelesstools.container.ContainerWPPersonal;
import ru.wirelesstools.handlerwireless.WirelessTransfer;

import java.util.List;
import java.util.Vector;

public class TileWPBasePersonal extends TileEntity implements IEnergySource, IInventory, IPersonalBlock,
        INetworkDataProvider, INetworkUpdateListener, IWirelessPanel, INetworkClientTileEntityEventListener {

    public int generating;
    public int genDay;
    public int genNight;
    public boolean initialized;
    public boolean sunIsUp;
    public boolean skyIsVisible;
    private boolean noSunWorld;
    private boolean wetBiome;
    private final int machineTier;
    public boolean addedToEnergyNet;
    private ItemStack[] chargeSlots;
    private int lastX;
    private int lastY;
    private int lastZ;
    public double storage;
    public String panelName;
    public int production;
    public int maxStorage;
    public boolean loaded;

    public int channel;
    public boolean isconnected;
    protected int wirelesstransferlimit;
    protected GameProfile owner = null;
    private int numUsingPlayers;

    public TileWPBasePersonal(String name, int tier,
                              int gDay, int gNight, int output, int maxStorage,
                              int wirelesstransfer) {
        this.loaded = false;
        this.genDay = gDay;
        this.genNight = gNight;
        this.storage = 0.0D;
        this.panelName = name;
        this.sunIsUp = false;
        this.skyIsVisible = false;
        this.maxStorage = maxStorage;
        this.chargeSlots = new ItemStack[4];
        this.initialized = false;
        this.production = output;
        this.lastX = this.xCoord;
        this.lastY = this.yCoord;
        this.lastZ = this.zCoord;
        this.machineTier = tier;
        this.isconnected = false;
        this.wirelesstransferlimit = wirelesstransfer;
        this.channel = 1;
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

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy", this.storage);
        nbttagcompound.setBoolean("isconnected", this.isconnected);
        nbttagcompound.setInteger("channel", this.channel);
        if(this.owner != null) {
            NBTTagCompound ownerNbt = new NBTTagCompound();
            NBTUtil.func_152460_a(ownerNbt, this.owner);
            nbttagcompound.setTag("ownerGameProfile", ownerNbt);
        }
    }

    protected void operateWirelessTransfer() {
        if(!this.worldObj.isRemote) {
            if(!this.isInvalid()) {
                if(TileWirelessStorageBasePersonal.mapofThis.containsKey(true)
                        & TileWirelessStorageBasePersonal.mapofThis.containsValue(TileWirelessStorageBasePersonal.listofstorages)) {
                    if(!(TileWirelessStorageBasePersonal.mapofThis.get(true).isEmpty())) {
                        for(TileWirelessStorageBasePersonal te : TileWirelessStorageBasePersonal.mapofThis.get(true)) {
                            if(areSameChannel(this.channel, te.channel, this.owner, te.owner)) {
                                this.isconnected = true;
                                WirelessTransfer.transmithandler.transferEnergyWirelessly(this, te);
                            }
                        }
                    }
                }
            }
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if(nbttagcompound.hasKey("ownerGameProfile")) {
            this.owner = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("ownerGameProfile"));
        }
        this.storage = nbttagcompound.getDouble("energy");
        this.isconnected = nbttagcompound.getBoolean("isconnected");
        this.channel = nbttagcompound.getInteger("channel");
    }

    public void invalidate() {
        if(this.loaded) {
            this.onUnloaded();
        }
        super.invalidate();
    }

    public void onUnloaded() {
        if(!this.worldObj.isRemote && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        this.loaded = false;
    }

    public void updateEntity() {
        super.updateEntity();

        if(this.worldObj.isRemote)
            return;

        if(!this.initialized) {
            this.intialize();
        }

        if(this.lastX != this.xCoord || this.lastZ != this.zCoord || this.lastY != this.yCoord) {
            this.lastX = this.xCoord;
            this.lastY = this.yCoord;
            this.lastZ = this.zCoord;
            this.onUnloaded();
            this.intialize();
        }

        this.gainFuel();

        this.operateWirelessTransfer();

        if(this.numUsingPlayers == 0 | this.numUsingPlayers > 0) {
            this.permitsAccess(this.owner);
        }

        if(this.generating > 0) {
            if(this.storage + this.generating <= this.maxStorage) {
                this.storage += this.generating;
            }
            else {
                this.storage = this.maxStorage;
            }
        }

        boolean needInvUpdate = false;
        double sentPacket;
        for(ItemStack chargeSlot : this.chargeSlots) {
            if(chargeSlot != null && chargeSlot.getItem() instanceof ic2.api.item.IElectricItem
                    && this.storage > 0) {
                sentPacket = ElectricItem.manager.charge(chargeSlot, this.storage, 2147483647, false, false);
                if(sentPacket > 0.0D) {
                    needInvUpdate = true;
                }
                this.storage -= sentPacket;
            }
        }
        if(needInvUpdate) {
            this.markDirty();
        }
    }

    public void intialize() {
        this.wetBiome = (this.worldObj.getWorldChunkManager().getBiomeGenAt(this.xCoord, this.zCoord).getIntRainfall() > 0);
        this.noSunWorld = this.worldObj.provider.hasNoSky;
        this.updateVisibility();

        this.initialized = true;
        if(!this.addedToEnergyNet) {
            this.onLoaded();
        }
    }

    public void updateVisibility() {
        boolean rainWeather = (this.wetBiome && (this.worldObj.isRaining() || this.worldObj.isThundering()));
        this.sunIsUp = this.worldObj.isDaytime() && !rainWeather;
        this.skyIsVisible = this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord) && !this.noSunWorld;
    }

    public void gainFuel() {
        if(this.worldObj.getTotalWorldTime() % 20L == 0)
            this.updateVisibility();
        if(this.sunIsUp && this.skyIsVisible) {
            this.generating = this.genDay;
            return;
        }
        if(this.skyIsVisible) {
            this.generating = this.genNight;
            return;
        }
        this.generating = 0;
    }

    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
        return true;
    }

    public double getOfferedEnergy() {
        return Math.min(this.production, this.storage);
    }

    public void drawEnergy(double amount) {
        this.storage -= (int)amount;
    }

    public int getSourceTier() {
        return this.machineTier;
    }

    private static boolean areSameChannel(int channelwpp, int channelwsb, GameProfile id1, GameProfile id2) {
        return (channelwpp == channelwsb) && (((id1 != null) && (id1.equals(id2))) || (id1 == id2));
    }

    public void changeChannel(int value) {
        this.channel = Math.max(this.channel + value, 1);
    }

    @Override
    public int getChannel() {
        return this.channel;
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setPlayerProfile(GameProfile profile) {
        this.owner = profile;
        IC2.network.get().updateTileEntityField(this, "owner");
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
        this.numUsingPlayers++;
        this.syncNumUsingPlayers();
    }

    @Override
    public void closeInventory() {
        this.numUsingPlayers--;
        this.syncNumUsingPlayers();
    }

    public boolean receiveClientEvent(int event, int data) {
        if(event == 1) {
            this.numUsingPlayers = data;
            return true;
        }
        return false;
    }

    private void syncNumUsingPlayers() {
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord,
                this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord), 1, this.numUsingPlayers);
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    public int gaugeEnergyScaled(int i) {
        return (int)(this.storage * i / this.maxStorage);
    }

    public Container getGuiContainer(InventoryPlayer inventoryplayer) {
        return new ContainerWPPersonal(inventoryplayer, this);
    }

    @Override
    public boolean permitsAccess(GameProfile profile) {
        if(profile == null)
            return (this.owner == null);
        if(!this.worldObj.isRemote) {
            if(this.owner == null) {
                this.owner = profile;
                IC2.network.get().updateTileEntityField(this, "owner");
                return true;
            }
        }
        return this.owner.equals(profile);
    }

    @Override
    public GameProfile getOwner() {
        return this.owner;
    }

    public void onNetworkUpdate(String field) {
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = new Vector<String>(1);
        ret.add("owner");
        return ret;
    }

    @Override
    public double getCurrentEnergyInPanel() {
        return this.storage;
    }

    @Override
    public int getWirelessTransferLimit() {
        return this.wirelesstransferlimit;
    }

    @Override
    public void extractEnergy(double amount) {
        this.storage -= Math.min(amount, this.storage);
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int id) {
        switch(id) {
            case 0:
                this.changeChannel(1);
                break;
            case 1:
                this.changeChannel(-1);
                break;
        }
    }
}
