package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IEnergyStorage;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.personal.IPersonalBlock;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerWChargerNew;
import ru.wirelesstools.gui.GuiWChargerNew;
import ru.wirelesstools.handlerwireless.WirelessTransfer;

import java.util.List;
import java.util.Vector;

public class TileEntityWirelessCharger extends TileEntity
        implements IEnergySink, IEnergyStorage, IPersonalBlock, IWirelessCharger, IInventory, INetworkDataProvider,
        INetworkUpdateListener, IHasGui, INetworkClientTileEntityEventListener {

    public int maxStorage;
    public double energy;
    public int tier;
    protected GameProfile owner = null;
    public int output;
    public boolean addedToEnergyNet = false;
    public boolean loaded;

    protected int radiusofcharge = 10;
    public int playercount = 0;
    protected boolean isPrivate;
    public String chargerName;

    public TileEntityWirelessCharger(String name, boolean isPrivate, int maxstorage, int tier) {
        this.energy = 0.0D;
        this.tier = tier;
        this.output = 128;
        this.maxStorage = maxstorage;
        this.isPrivate = isPrivate;
        this.loaded = false;
        this.chargerName = name;
    }

    public boolean getIsPrivate() {

        return this.isPrivate;
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if(this.owner != null) {
            NBTTagCompound ownerNbt = new NBTTagCompound();
            NBTUtil.func_152460_a(ownerNbt, this.owner);
            nbttagcompound.setTag("ownerGameProfileCharger", ownerNbt);
        }
        nbttagcompound.setDouble("energy", this.energy);
        nbttagcompound.setInteger("maxenergy", this.maxStorage);
        nbttagcompound.setInteger("radiusofcharge", this.radiusofcharge);
        nbttagcompound.setBoolean("isPrivate", this.isPrivate);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if(nbttagcompound.hasKey("ownerGameProfileCharger")) {
            this.owner = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("ownerGameProfileCharger"));
        }
        this.energy = nbttagcompound.getDouble("energy");
        this.maxStorage = nbttagcompound.getInteger("maxenergy");
        this.radiusofcharge = nbttagcompound.getInteger("radiusofcharge");
        this.isPrivate = nbttagcompound.getBoolean("isPrivate");
    }

    public int gaugeEnergyScaled(int i) {

        return (int) (this.energy * i / this.maxStorage);
    }

    public int getRadiusOfCharge() {

        return this.radiusofcharge;
    }

    public void onLoaded() {
        if(!this.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }

        this.loaded = true;
    }

    public void onUnloaded() {
        if(IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        this.loaded = false;
    }

    public void validate() {
        super.validate();
        this.onLoaded();
    }

    public void invalidate() {
        if(this.loaded) {
            this.onUnloaded();
        }
        super.invalidate();
    }

    public void updateEntity() {
        super.updateEntity();
        if(!this.worldObj.isRemote) {

            if(this.energy > this.maxStorage) {
                this.energy = this.maxStorage;
            }

            this.playercount = WirelessTransfer.chargeplayerhandler.checkPlayersAround(this.isPrivate, this,
                    this.radiusofcharge, this.worldObj);

            this.markDirty();
        }
    }

    public void changeRadius(int value) {
        int localradius = this.radiusofcharge;
        if(value < 0) {
            localradius += value;
            if(localradius < 1)
                localradius = 1;
        } else {
            localradius += value;
            if(localradius > 25)
                localradius = 25;
        }

        this.radiusofcharge = localradius;
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
        double de = this.getDemandedEnergy();
        if(amount == 0.0) {
            return 0.0;
        } else if(de <= 0.0) {
            return amount;
        } else if(amount >= de) {
            this.energy += de;
            return amount - de;
        } else {
            this.energy += amount;
            return 0.0;
        }
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
        this.energy += amount;
        return amount;
    }

    @Override
    public int getCapacity() {
        return this.maxStorage;
    }

    @Override
    public int getOutput() {
        return this.output;
    }

    @Override
    public double getOutputEnergyUnitsPerTick() {
        return this.output;
    }

    @Override
    public boolean isTeleporterCompatible(ForgeDirection side) {
        return false;
    }

    public void setPlayerProfile(GameProfile profile) {
        this.owner = profile;
        IC2.network.get().updateTileEntityField(this, "owner");
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

    @Override
    public void decreaseEnergy(double amount) {
        double energylocal = this.energy;
        energylocal -= amount;
        if(energylocal < 0)
            energylocal = 0;

        this.energy = energylocal;
    }

    @Override
    public double getCurrentEnergyInCharger() {
        return this.energy;
    }

    @Override
    public GameProfile getOwnerCharger() {
        return this.owner;
    }

    @Override
    public int getXCoord() {
        return this.xCoord;
    }

    @Override
    public int getYCoord() {
        return this.yCoord;
    }

    @Override
    public int getZCoord() {
        return this.zCoord;
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
    public void onNetworkUpdate(String field) {
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = new Vector<>(1);
        ret.add("owner");
        return ret;
    }

    @Override
    public void onNetworkEvent(EntityPlayer player, int event) {
        switch(event) {
            case 0:
                this.changeRadius(1);
                break;
            case 1:
                this.changeRadius(-1);
                break;
        }
    }

    @SideOnly(value = Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiWChargerNew(new ContainerWChargerNew(player, this));
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer player) {
        return new ContainerWChargerNew(player, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {
    }

}
