package ru.wirelesstools.tiles;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.container.ContainerPFPConverter;
import ru.wirelesstools.gui.GuiPFPConvertor;
import ru.wirelesstools.recipes.Recipes;
import ru.wirelesstools.slots.InvSlotProcessablePFP;

public class PFPConvertorTile extends TileEntityInventory implements IEnergySink, IHasGui, INetworkClientTileEntityEventListener,
        IEnergyReceiver {
    protected short progress;
    public int operationLength;
    public int maxStorageEU;
    public double energyEU;
    public int tier;
    public String pfpconvertorname;
    private boolean addedToEnergyNet = false;
    public InvSlotProcessable inputSlotA;
    public final InvSlotOutput outputSlot;
    protected int energyPerTick;
    private double hot_energy_amount;

    public PFPConvertorTile() {
        this.energyEU = 0.0;
        this.progress = 0;
        this.operationLength = 4;
        this.energyPerTick = 5000;
        this.tier = 8;
        this.pfpconvertorname = "pfpconverter.name";
        this.maxStorageEU = 10000000;
        this.hot_energy_amount = 1000000.0;
        this.outputSlot = new InvSlotOutput(this, "blockoutput", 1, 1);
        this.inputSlotA = new InvSlotProcessablePFP(this, "oreinput", 1, 1);
    }

    public static void init() {
        Recipes.PFP_RecipeManager = new BasicMachineRecipeManager();
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.gold_ore)), null, new ItemStack(Blocks.gold_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.iron_ore)), null, new ItemStack(Blocks.iron_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.coal_ore)), null, new ItemStack(Blocks.coal_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.diamond_ore)), null, new ItemStack(Blocks.diamond_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.redstone_ore)), null, new ItemStack(Blocks.redstone_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.quartz_ore)), null, new ItemStack(Blocks.quartz_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.emerald_ore)), null, new ItemStack(Blocks.emerald_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreCopper"), null, OreDictionary.getOres("blockCopper").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreTin"), null, OreDictionary.getOres("blockTin").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreSilver"), null, OreDictionary.getOres("blockSilver").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreLead"), null, OreDictionary.getOres("blockLead").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreNickel"), null, OreDictionary.getOres("blockNickel").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("orePlatinum"), null, OreDictionary.getOres("blockPlatinum").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreMithril"), null, OreDictionary.getOres("blockMithril").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreDraconium"), null, OreDictionary.getOres("blockDraconium").get(0));
    }

    public RecipeOutput getOutput() {
        if(this.inputSlotA.isEmpty()) {
            return null;
        } else {
            RecipeOutput output = this.inputSlotA.process();
            if(output == null) {
                return null;
            } else {
                return this.outputSlot.canAdd(output.items) ? output : null;
            }
        }
    }

    public void operate(RecipeOutput output) {
        this.inputSlotA.consume();
        this.outputSlot.add(output.items);

    }

    public void setHotState(double amount) {

        this.hot_energy_amount = amount;
    }

    public double getHotState() {

        return this.hot_energy_amount;
    }

    public boolean getIsHot() {

        return this.energyEU >= this.getHotState();
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;

        if(this.energyEU > 0) {
            this.energyEU -= Math.min(125, this.energyEU);
        }

        RecipeOutput output = this.getOutput();
        if(output != null && this.getIsHot()) {
            this.setActive(true);
            if(this.progress == 0) {
                //    IC2.network.get().initiateTileEntityEvent(this, 0, true);
            }

            ++this.progress;
            this.energyEU -= (double) this.energyPerTick;
            if(this.progress >= this.operationLength) {
                this.operate(output);
                needsInvUpdate = true;
                this.progress = 0;
                //    IC2.network.get().initiateTileEntityEvent(this, 2, true);
            }
        } else {
            if(this.progress != 0 && this.getActive()) {
                //    IC2.network.get().initiateTileEntityEvent(this, 1, true);
            }

            if(output == null) {
                this.progress = 0;
            }

            this.setActive(false);
        }

        if(needsInvUpdate) {
            super.markDirty();
        }
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

    public int gaugeEnergyScaled(int i) {

        return (int) (this.energyEU * i / this.maxStorageEU);
    }

    public double gaugeProgressScaled() {

        return (double) this.progress / (double) this.operationLength;
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
        nbttagcompound.setDouble("energy", this.energyEU);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
        this.energyEU = nbttagcompound.getDouble("energy");
    }

    @Override
    public double getDemandedEnergy() {
        return (double)this.maxStorageEU - this.energyEU;
    }

    @Override
    public int getSinkTier() {
        return this.tier;
    }

    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        double de = this.getDemandedEnergy();
        if (amount == 0.0D) {
            return 0.0D;
        } else if (de <= 0.0D) {
            return amount;
        } else if (amount >= de) {
            this.energyEU += de;
            return amount - de;
        } else {
            this.energyEU += amount;
            return 0.0D;
        }
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return true;
    }

    @Override
    public void onNetworkEvent(EntityPlayer entityPlayer, int i) {

    }

    @Override
    public ContainerBase<PFPConvertorTile> getGuiContainer(EntityPlayer player) {

        return new ContainerPFPConverter(player, this);
    }

    @SideOnly(value = Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean b) {

        return new GuiPFPConvertor(new ContainerPFPConverter(player, this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    @Override
    public String getInventoryName() {
        return "PFP Converter";
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int energyReceivedRF = Math.min(this.getMaxEnergyStored(from) - this.getEnergyStored(from), maxReceive);
        if(!simulate) {
            this.addRfEnergy(energyReceivedRF);
        }
        return energyReceivedRF;
    }

    @Override
    public int getEnergyStored(ForgeDirection fd) {
        return (int) this.energyEU * ConfigWI.EUToRF_Multiplier;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection fd) {
        return this.maxStorageEU * ConfigWI.EUToRF_Multiplier;
    }

    protected void addRfEnergy(int RFamount) {
        this.energyEU += (double) RFamount / ConfigWI.EUToRF_Multiplier;
    }
}
