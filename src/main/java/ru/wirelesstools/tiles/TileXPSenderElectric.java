package ru.wirelesstools.tiles;

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

import java.util.List;

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
    private final int energyperpoint;
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
        if(!this.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
        this.loaded = true;
    }

    public void invalidate() {
        if(this.loaded) {
            if(!this.worldObj.isRemote && this.addedToEnergyNet) {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                this.addedToEnergyNet = false;
            }
            this.loaded = false;
        }
        super.invalidate();
    }

    public void updateEntity() {
        super.updateEntity();
        if(this.worldObj.isRemote) {
            return;
        }

        if(this.energy > this.maxStorage)
            this.energy = this.maxStorage;

        this.countPlayers(this.sendradius);
    }

    protected void consumeEnergyXPSender(double amount) {
        this.energy = Math.max(this.energy - amount, 0.0);
    }

    public int getTotalSpentEUValue() {
        return this.energyperpoint * this.pointsxp;
    }

    public int getXPPointsToSend() {
        return this.pointsxp;
    }

    protected void countPlayers(int radius) {
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.xCoord - radius, this.yCoord - radius,
                this.zCoord - radius, this.xCoord + radius + 1,
                this.yCoord + radius + 1, this.zCoord + radius + 1);
        List<EntityPlayer> list = this.getWorldObj().getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        if((this.worldObj.getTotalWorldTime() % (ConfigWI.secondsXPSender * 20L)) == 0)
            this.sendXPToPlayersAround(this.pointsxp, list);
        this.playercountinradius = list.size();
    }

    protected void sendXPToPlayersAround(int points, List<EntityPlayer> list) {
        double consume = this.getTotalSpentEUValue();
        for(EntityPlayer player : list) {
            if(player != null) {
                if(this.energy > consume) {
                    player.addExperience(points);
                    this.consumeEnergyXPSender(consume);
                }
            }
        }
    }

    public void changeRadius(int value) {
        this.sendradius = value < 0 ? Math.max(this.sendradius + value, 1) : Math.min(this.sendradius + value, 25);
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
        double de = this.getDemandedEnergy();
        if(amount == 0.0D) {
            return 0.0D;
        } else if(de <= 0.0D) {
            return amount;
        } else if(amount >= de) {
            this.energy += de;
            return amount - de;
        } else {
            this.energy += amount;
            return 0.0D;
        }
    }

    @SideOnly(value = Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiXPSenderNew(new ContainerXPSenderNew(player, this));
    }

    @Override
    public ContainerBase<TileXPSenderElectric> getGuiContainer(EntityPlayer player) {
        return new ContainerXPSenderNew(player, this);
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {
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

}
