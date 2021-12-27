package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.core.IC2;
import ic2.core.block.personal.IPersonalBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerWSBPersonal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class TileWirelessStorageBasePersonal extends TileEntity implements IEnergySource,
        IPersonalBlock, IWirelessStorage, INetworkDataProvider, INetworkClientTileEntityEventListener {

    public int maxStorage;
    public double energy;
    public int output;
    public int tier;
    public boolean targetSet;

    public String wsbPersName;

    public boolean initialized;
    public boolean loaded;

    private boolean addedToEnergyNet;

    protected boolean isconnected;

    public int channel = 0;
    private boolean isAddedToMap;

    public static ArrayList<TileWirelessStorageBasePersonal> listofstorages = new ArrayList<>();
    public static HashMap<Boolean, ArrayList<TileWirelessStorageBasePersonal>> mapofThis = new HashMap<>();

    protected GameProfile owner = null;

    public TileWirelessStorageBasePersonal(int output, int maxStorage, int tier, String name) {
        this.energy = 0.0;
        this.tier = tier;
        this.output = output;
        this.maxStorage = maxStorage;
        this.loaded = false;
        this.initialized = false;
        this.targetSet = false;
        this.isconnected = false;
        this.wsbPersName = name;
    }

    public void setPlayerProfile(GameProfile profile) {
        this.owner = profile;
        IC2.network.get().updateTileEntityField(this, "owner");
    }

    public void updateEntity() {
        super.updateEntity();

        if(this.worldObj.isRemote) {
            return;
        }

        if(!this.initialized) {
            this.intialize();
        }

        if((!this.worldObj.isRemote) & (!this.isAddedToMap)) {
            if(!listofstorages.contains(this)) {
                listofstorages.add(this);

                if(mapofThis.isEmpty() | (mapofThis.containsKey(false) & mapofThis.containsValue(listofstorages))) {
                    mapofThis.clear();
                    mapofThis.put(true, listofstorages);
                }
            }
            this.isAddedToMap = true;
            this.isconnected = true;
        }

        if(this.energy > this.maxStorage) {
            this.energy = this.maxStorage;
        }
    }

    public void validate() {
        super.validate();
        this.onLoaded();
        this.isconnected = true;
    }

    public void onLoaded() {
        if(!this.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            this.isconnected = true;
        }
        this.loaded = true;
    }

    public void invalidate() {
        if(this.loaded) {
            this.onUnloaded();
        }
        this.isconnected = false;
        super.invalidate();
    }

    public void onUnloaded() {
        if(!this.worldObj.isRemote && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
            if(this.isAddedToMap) {
                listofstorages.remove(this);
                this.isAddedToMap = false;
                this.isconnected = false;
                mapofThis.clear();
                mapofThis.put(true, listofstorages);
            }
        }
        this.loaded = false;
    }

    public void intialize() {
        this.initialized = true;
        if(!this.addedToEnergyNet) {
            this.onLoaded();
        }
    }

    @Override
    public boolean emitsEnergyTo(TileEntity nameTileEntity, ForgeDirection nameForgeDirection) {
        return true;
    }

    @Override
    public double getOfferedEnergy() {
        if(this.energy >= this.output) {
            return Math.min(this.energy, this.output);
        }
        return 0;
    }

    @Override
    public void drawEnergy(double amount) {
        this.energy -= amount;
    }

    @Override
    public int getSourceTier() {
        return this.tier;
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if(this.owner != null) {
            NBTTagCompound ownerNbt = new NBTTagCompound();
            NBTUtil.func_152460_a(ownerNbt, this.owner);
            nbttagcompound.setTag("ownerGameProfile", ownerNbt);
        }
        nbttagcompound.setDouble("energy", this.energy);
        nbttagcompound.setInteger("maxenergy", this.maxStorage);
        nbttagcompound.setBoolean("targetset", this.targetSet);
        nbttagcompound.setBoolean("isconnected", this.isconnected);
        nbttagcompound.setInteger("channel", this.channel);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if(nbttagcompound.hasKey("ownerGameProfile")) {
            this.owner = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("ownerGameProfile"));
        }
        this.energy = nbttagcompound.getDouble("energy");
        this.maxStorage = nbttagcompound.getInteger("maxenergy");
        this.targetSet = nbttagcompound.getBoolean("targetset");
        this.isconnected = nbttagcompound.getBoolean("isconnected");
        this.channel = nbttagcompound.getInteger("channel");
    }

    public void changeChannel(int value) {
        this.channel = Math.max(this.channel + value, 0);
    }

    public Container getGuiContainer(InventoryPlayer inventory) {
        return new ContainerWSBPersonal(inventory, this);
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return (player.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
    }

    public int gaugeEnergyScaled(int i) {
        return (int) (this.energy * i / this.maxStorage);
    }

    public GameProfile getOwner() {
        return this.owner;
    }

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
    public double getMaxCapacityOfStorage() {
        return this.maxStorage;
    }

    @Override
    public double getCurrentEnergyInStorage() {
        return this.energy;
    }

    @Override
    public double getFreeEnergy() {
        return this.getMaxCapacityOfStorage() - this.getCurrentEnergyInStorage();
    }

    @Override
    public void addEnergy(double amount) {
        this.energy += amount;
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = new Vector<String>(1);
        ret.add("owner");
        return ret;
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
