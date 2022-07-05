package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerWirelessQuantumGen;
import ru.wirelesstools.gui.GuiWirelessQuantumGen;
import ru.wirelesstools.handlerwireless.WirelessTransfer;
import ru.wirelesstools.utils.WirelessUtil;

public class WirelessQuantumGeneratorBase extends TileEntityInventory
        implements IHasGui, IEnergySource, INetworkClientTileEntityEventListener, IWirelessQGen {
    
    private boolean addedToEnergyNet;
    protected int tier;
    protected GameProfile owner = null;
    public String wirelessQGenName;
    protected ChargeStatus chargingStatus;
    private int machinesCountInChunk;
    protected int modeTransmitting;
    
    public WirelessQuantumGeneratorBase(int tier, String name) {
        this.tier = tier;
        this.wirelessQGenName = name;
        this.machinesCountInChunk = 0;
        this.chargingStatus = ChargeStatus.NOT_CHARGING;
        this.modeTransmitting = 0;
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
        super.updateEntityServer();
        WirelessUtil.doWirelessChargingMachines(this);
        this.operateWirelessTransferFromQGen();
    }
    
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if(this.owner != null) {
            NBTTagCompound ownerNbt = new NBTTagCompound();
            NBTUtil.func_152460_a(ownerNbt, this.owner);
            nbttagcompound.setTag("ownerGameProfile", ownerNbt);
        }
        nbttagcompound.setInteger("modeTransmitting", this.modeTransmitting);
    }
    
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if(nbttagcompound.hasKey("ownerGameProfile")) {
            this.owner = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("ownerGameProfile"));
        }
        this.modeTransmitting = nbttagcompound.getInteger("modeTransmitting");
    }
    
    protected void operateWirelessTransferFromQGen() {
        if(TileWirelessStorageBasePersonal.mapofThis.containsKey(true) && TileWirelessStorageBasePersonal.mapofThis
                .containsValue(TileWirelessStorageBasePersonal.listofstorages)) {
            if(!(TileWirelessStorageBasePersonal.mapofThis.get(true).isEmpty())) {
                for(TileWirelessStorageBasePersonal te : TileWirelessStorageBasePersonal.mapofThis.get(true)) {
                    if(areSameOwners(this.owner, te.owner)) {
                        WirelessTransfer.transmithandler.transmitEnergyWireleslyQGen(te, this);
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
        return ((id1 != null) && (id1.equals(id2))) || (id1 == id2);
    }
    
    public int getWirelessTransferLimitQGen() {
        return this.getChargeRateByMode(this.modeTransmitting);
    }
    
    @Override
    public void setMachinesCountInChunk(int value) {
        this.machinesCountInChunk = value;
    }
    
    @Override
    public int getMachinesCountInChunk() {
        return this.machinesCountInChunk;
    }
    
    @Override
    public void setStatus(ChargeStatus status) {
        this.chargingStatus = status;
    }
    
    @Override
    public ChargeStatus getStatus() {
        return this.chargingStatus;
    }
    
    @Override
    public void onNetworkEvent(EntityPlayer player, int id) {
        if(++this.modeTransmitting > 9)
            this.modeTransmitting = 0;
    }
    
    @Override
    public boolean emitsEnergyTo(TileEntity tile, ForgeDirection dir) {
        return true;
    }
    
    @Override
    public void drawEnergy(double amount) {
        // TODO nothing here because of unlimited energy
    }
    
    @Override
    public double getOfferedEnergy() {
        return this.getChargeRateByMode(this.modeTransmitting);
    }
    
    public int getOutput() {
        return this.getChargeRateByMode(this.modeTransmitting);
    }
    
    @Override
    public int getSourceTier() {
        return this.tier;
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
    public String getInventoryName() {
        return null;
    }
    
    @Override
    public ContainerBase<WirelessQuantumGeneratorBase> getGuiContainer(EntityPlayer player) {
        return new ContainerWirelessQuantumGen(player, this);
    }
    
    @SideOnly(value = Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiWirelessQuantumGen(new ContainerWirelessQuantumGen(player, this));
    }
    
    @Override
    public void onGuiClosed(EntityPlayer player) {
    }
    
    @Override
    public World getQGenWorld() {
        return this.worldObj;
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
    public int getModeTransmitting() {
        return this.modeTransmitting;
    }
    
    @Override
    public int getChargeRateByMode(int mode) {
        return (int)(1024.0 * Math.pow(2, mode));
    }
    
}
