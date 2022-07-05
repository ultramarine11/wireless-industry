package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerWChargerNew;
import ru.wirelesstools.gui.GuiWChargerNew;
import ru.wirelesstools.handlerwireless.WirelessTransfer;

import java.util.List;

public class TileEntityWirelessCharger extends TileEntityInventory
        implements IEnergySink, IWirelessCharger, INetworkDataProvider, IHasGui, INetworkClientTileEntityEventListener {

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
        this.energy = 0.0;
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

    @Override
    public String getInventoryName() {
        return null;
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
        return (int)(this.energy * i / this.maxStorage);
    }

    public int getRadiusOfCharge() {
        return this.radiusofcharge;
    }

    public void onLoaded() {
        super.onLoaded();
        if(IC2.platform.isSimulating()) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    public void onUnloaded() {
        if(IC2.platform.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }
        super.onUnloaded();
    }

    protected void updateEntityServer() {
        if(this.energy > this.maxStorage) {
            this.energy = this.maxStorage;
        }
        this.playercount = WirelessTransfer.chargeplayerhandler.checkPlayersAround(this.isPrivate, this,
                this.radiusofcharge, this.worldObj);
    }

    public void changeRadius(int value) {
        this.radiusofcharge = value < 0 ? Math.max(this.radiusofcharge + value, 1) : Math.min(this.radiusofcharge + value, 25);
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
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
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        double de = this.getDemandedEnergy();
        if(amount == 0.0) {
            return 0.0;
        }
        else if(de <= 0.0) {
            return amount;
        }
        else if(amount >= de) {
            this.energy += de;
            return amount - de;
        }
        else {
            this.energy += amount;
            return 0.0;
        }
    }

    public void setPlayerProfile(GameProfile profile) {
        this.owner = profile;
        IC2.network.get().updateTileEntityField(this, "owner");
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

    public GameProfile getOwner() {
        return this.owner;
    }

    @Override
    public void decreaseEnergy(double amount) {
        this.energy -= Math.min(this.energy, amount);
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
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
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
    public ContainerBase<TileEntityWirelessCharger> getGuiContainer(EntityPlayer player) {
        return new ContainerWChargerNew(player, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {
    }

}
