package ru.wirelesstools.tiles;

import cpw.mods.fml.client.FMLClientHandler;
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
import ic2.core.util.EntityIC2FX;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ru.wirelesstools.container.ContainerXPTransmitter;
import ru.wirelesstools.gui.GuiXPTransmitter;
import ru.wirelesstools.utils.ExperienceUtils;

import java.util.*;

public class TileXPTransmitter extends TileEntityInventory implements IEnergySink, IHasGui, INetworkClientTileEntityEventListener, INetworkDataProvider {
    
    protected double maxEnergy;
    protected double energy;
    protected int tier;
    protected boolean addedToEnergyNet = false;
    protected boolean loaded;
    private boolean isOn;
    private boolean sendingXPMode;
    private int amountXPTransmit;
    private int storedXP;
    private int xpLimit;
    private final List<EntityPlayer> playersStandingOnTop;
    
    private int playersCount;
    
    public TileXPTransmitter() {
        this(1000000.0, 9);
    }
    
    public TileXPTransmitter(double maxstorage, int tier) {
        this.energy = 0.0;
        this.tier = tier;
        this.maxEnergy = maxstorage;
        this.loaded = false;
        this.sendingXPMode = true;
        this.isOn = true;
        this.amountXPTransmit = 1;
        this.storedXP = 0;
        this.xpLimit = 20000000;
        this.playersStandingOnTop = new ArrayList<>();
        this.playersCount = 0;
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
    
    public void setInternalParameters(double energy, int xp, int transmit, boolean mode, boolean isOnOff) {
        this.energy = energy;
        this.storedXP = xp;
        this.amountXPTransmit = transmit;
        this.sendingXPMode = mode;
        this.isOn = isOnOff;
    }
    
    public ItemStack getWrenchDrop(EntityPlayer player) {
        ItemStack drop = super.getWrenchDrop(player);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
        nbt.setDouble("energy", this.energy);
        nbt.setInteger("storedXP", this.storedXP);
        nbt.setInteger("amountXPTransmit", this.amountXPTransmit);
        nbt.setBoolean("sendingXPMode", this.sendingXPMode);
        nbt.setBoolean("isOn", this.isOn);
        return drop;
    }
    
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("energy", this.energy);
        nbt.setInteger("storedXP", this.storedXP);
        nbt.setInteger("amountXPTransmit", this.amountXPTransmit);
        nbt.setBoolean("sendingXPMode", this.sendingXPMode);
        nbt.setBoolean("isOn", this.isOn);
    }
    
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energy = nbt.getDouble("energy");
        this.storedXP = nbt.getInteger("storedXP");
        this.amountXPTransmit = nbt.getInteger("amountXPTransmit");
        this.sendingXPMode = nbt.getBoolean("sendingXPMode");
        this.isOn = nbt.getBoolean("isOn");
    }
    
    @Override
    public double getDemandedEnergy() {
        return this.maxEnergy - this.energy;
    }
    
    @Override
    public int getSinkTier() {
        return this.tier;
    }
    
    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double amount, double v1) {
        double de = this.getDemandedEnergy();
        if(amount == 0.0)
            return 0.0;
        else if(de <= 0.0)
            return amount;
        else if(amount >= de) {
            this.energy += de;
            return amount - de;
        }
        else {
            this.energy += amount;
            return 0.0;
        }
    }
    
    @SideOnly(Side.CLIENT)
    protected void updateEntityClient() {
        super.updateEntityClient();
        Random rnd = this.worldObj.rand;
        if(rnd.nextInt(8) != 0)
            return;
        if(this.getActive()) {
            EffectRenderer effect = FMLClientHandler.instance().getClient().effectRenderer;
            float[] orangeParticles = {1.0F, 0.55F, 0F};
            float[] greenParticles = {0.5F, 0.72F, 0F};
            for(int particles = 20; particles > 0; particles--) {
                double x = (this.xCoord + 0.0F + rnd.nextFloat());
                double y = (this.yCoord + 0.9F + rnd.nextFloat());
                double z = (this.zCoord + 0.0F + rnd.nextFloat());
                effect.addEffect(new EntityIC2FX(this.worldObj, x, y, z, 45, new double[] {0.0D, 0.1D, 0.0D},
                        this.sendingXPMode ? greenParticles : orangeParticles));
            }
        }
    }
    
    @Override
    public boolean acceptsEnergyFrom(TileEntity te, ForgeDirection dir) {
        return true;
    }
    
    @Override
    public void onNetworkEvent(EntityPlayer entityPlayer, int id) {
        switch(id) {
            case 1:
                this.changeAmountSentToPlayer(1);
                break;
            case 2:
                this.changeAmountSentToPlayer(-1);
                break;
            case 3:
                this.toggleWork();
                break;
            case 4:
                this.invertMode();
                break;
            case 5:
                this.changeAmountSentToPlayer(10);
                break;
            case 6:
                this.changeAmountSentToPlayer(-10);
                break;
            case 7:
                this.changeAmountSentToPlayer(100);
                break;
            case 8:
                this.changeAmountSentToPlayer(-100);
                break;
        }
    }
    
    protected void updateEntityServer() {
        super.updateEntityServer();
        if(this.isOn) {
            this.attemptGeneration();
            this.getPlayersStandingOn();
            
            if(this.sendingXPMode)
                this.transferXPToPlayers(this.playersStandingOnTop);
            else
                this.consumeXPFromPlayers(this.playersStandingOnTop);
        }
    }
    
    protected void attemptGeneration() {
        if(this.energy >= this.maxEnergy) {
            if(this.storedXP < this.xpLimit) {
                this.storedXP++;
                this.energy -= this.maxEnergy;
            }
        }
    }
    
    protected void getPlayersStandingOn() {
        this.playersStandingOnTop.clear();
        //this.playerNamesSet.clear();
        this.playersCount = 0;
        List<EntityPlayer> playersList = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
                AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord + 1, this.zCoord,
                        this.xCoord + 1, this.yCoord + 2, this.zCoord + 1));
        if(!playersList.isEmpty()) {
            this.playersStandingOnTop.addAll(playersList);
            this.playersCount = playersList.size();
            /*for(EntityPlayer player : playersList)
                this.playerNamesSet.add(player.getGameProfile().getName());*/
        }
    }
    
    protected void transferXPToPlayers(List<EntityPlayer> playersStandingOn) {
        if(!playersStandingOn.isEmpty()) {
            if(!this.getActive())
                this.setActive(true);
            if(this.worldObj.getTotalWorldTime() % 2 == 0)
                for(EntityPlayer player : playersStandingOn) {
                    if(this.storedXP > 0) {
                        int realTransfer = Math.min(this.storedXP, this.amountXPTransmit);
                        this.storedXP -= realTransfer;
                        ExperienceUtils.addPlayerXP(player, realTransfer);
                    }
                }
        }
        else {
            if(this.getActive())
                this.setActive(false);
        }
    }
    
    protected void consumeXPFromPlayers(List<EntityPlayer> playersStandingOn) {
        if(!playersStandingOn.isEmpty()) {
            if(!this.getActive())
                this.setActive(true);
            for(EntityPlayer player : playersStandingOn) {
                int playerXP = ExperienceUtils.getPlayerXP(player);
                if(playerXP > 0) {
                    int maxAcceptedXP = Math.min(Math.min(playerXP, this.amountXPTransmit), (this.xpLimit - this.storedXP));
                    if(maxAcceptedXP > 0) {
                        ExperienceUtils.consumeXPFromPlayer(player, maxAcceptedXP);
                        this.storedXP += maxAcceptedXP;
                    }
                }
            }
        }
        else {
            if(this.getActive())
                this.setActive(false);
        }
    }
    
    protected void invertMode() {
        this.sendingXPMode = !this.sendingXPMode;
    }
    
    protected void toggleWork() {
        this.isOn = !this.isOn;
    }
    
    public int getPercentageGeneration() {
        return (int)(this.energy / this.maxEnergy * 100);
    }
    
    public boolean getIsSendingMode() {
        return this.sendingXPMode;
    }
    
    public boolean getIsOn() {
        return this.isOn;
    }
    
    protected void changeAmountSentToPlayer(int amount) {
        this.amountXPTransmit = Math.max(this.amountXPTransmit + amount, 1);
    }
    
    public double gaugeExperience(int pixels) {
        return (double)this.storedXP * pixels / this.xpLimit;
    }
    
    public int getStoredXP() {
        return this.storedXP;
    }
    
    public int getAmountXPTransmit() {
        return this.amountXPTransmit;
    }
    
    public int getXpLimit() {
        return this.xpLimit;
    }
    
    public int getPlayersCount() {
        return this.playersCount;
    }
    
    @Override
    public ContainerBase<TileXPTransmitter> getGuiContainer(EntityPlayer player) {
        return new ContainerXPTransmitter(player, this);
    }
    
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean b) {
        return new GuiXPTransmitter(new ContainerXPTransmitter(player, this));
    }
    
    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    
    }
    
    @Override
    public String getInventoryName() {
        return null;
    }
}
