package ru.wirelesstools.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.TileEntityLiquidTankElectricMachine;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.container.ContainerLiquidMatterCollector;
import ru.wirelesstools.gui.GuiLiquidMatterCollector;

import java.util.ArrayList;

public class TileLiquidMatterCollector extends TileEntityInventory implements IFluidHandler, IHasGui,
        INetworkClientTileEntityEventListener {
    
    //    public final InvSlotOutput outputSlot;
//    public final InvSlotConsumableLiquid containerslot;
    protected final FluidTank fluidTank;
    public final String mattercollectorname;
    private boolean isActive = false;
    
    public TileLiquidMatterCollector() {
        this("mattercollector.name");
    }
    
    public TileLiquidMatterCollector(String collectorname) {
        this.fluidTank = new FluidTank(ConfigWI.matterCollectorCapacity);
        this.mattercollectorname = collectorname;
        //    this.outputSlot = new InvSlotOutput(this, "output", 1, 1);
        //    this.containerslot = new InvSlotConsumableLiquidByList(this, "containerslot", 2, InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill, new Fluid[]{BlocksItems.getFluid(InternalName.fluidUuMatter)});
    }
    
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if(this.getIsActive()) {
            if(!this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord).chunkTileEntityMap.isEmpty()) {
                for(TileEntity tile : new ArrayList<TileEntity>(this.getWorldObj()
                        .getChunkFromBlockCoords(this.xCoord, this.zCoord).chunkTileEntityMap.values())) {
                    if(tile instanceof TileEntityLiquidTankElectricMachine) {
                        this.tryFillCollector((TileEntityLiquidTankElectricMachine)tile);
                    }
                }
            }
        }
    }
    
    public int getPercentagePart(int factor) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0 : (int)(factor * (double)this.getFluidTank().getFluidAmount() / this.getFluidTank().getCapacity());
    }
    
    public double getLiquidAmountPercentage() {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0.0 : 100.0 * (double)this.getFluidTank().getFluidAmount() / (double)this.getFluidTank().getCapacity();
    }
    
    public int getIntegerPercentage() {
        return (int)this.getLiquidAmountPercentage();
    }
    
    public boolean getIsActive() {
        return this.isActive;
    }
    
    public void reverseActive() {
        this.isActive = !this.isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public FluidTank getFluidTank() {
        return this.fluidTank;
    }
    
    private void tryFillCollector(TileEntityLiquidTankElectricMachine tiletank) {
        if(tiletank.getFluidStackfromTank() != null
                && tiletank.getFluidStackfromTank().getFluid() == BlocksItems.getFluid(InternalName.fluidUuMatter)) {
            if(tiletank.getTankAmount() > 0) {
                FluidStack drainedsim = tiletank.getFluidTank().drain(tiletank.getTankAmount(), false);
                if(drainedsim != null) {
                    int fillsim = this.getFluidTank().fill(drainedsim, false);
                    int realtransferred = Math.min(fillsim, drainedsim.amount);
                    this.getFluidTank().fill(tiletank.getFluidTank().drain(realtransferred, true), true);
                }
            }
        }
    }
    
    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0 : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }
    
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return !this.canFill(from, resource.getFluid()) ? 0 : this.getFluidTank().fill(resource, doFill);
    }
    
    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if(resource != null && resource.isFluidEqual(this.getFluidTank().getFluid())) {
            return !this.canDrain(from, resource.getFluid()) ? null : this.getFluidTank().drain(resource.amount, doDrain);
        }
        else {
            return null;
        }
    }
    
    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return !this.canDrain(from, null) ? null : this.getFluidTank().drain(maxDrain, doDrain);
    }
    
    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == BlocksItems.getFluid(InternalName.fluidUuMatter);
    }
    
    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }
    
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] {this.getFluidTank().getInfo()};
    }
    
    @Override
    public String getInventoryName() {
        return null;
    }
    
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.isActive = nbttagcompound.getBoolean("active");
        this.fluidTank.readFromNBT(nbttagcompound.getCompoundTag("fluidTank"));
    }
    
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("active", this.isActive);
        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbttagcompound.setTag("fluidTank", fluidTankTag);
    }
    
    public ItemStack getWrenchDrop(EntityPlayer player) {
        ItemStack drop = super.getWrenchDrop(player);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
        nbt.setBoolean("active", this.isActive);
        NBTTagCompound fluidTankTag = new NBTTagCompound();
        this.fluidTank.writeToNBT(fluidTankTag);
        nbt.setTag("fluidTank", fluidTankTag);
        return drop;
    }
    
    @Override
    public ContainerBase<TileLiquidMatterCollector> getGuiContainer(EntityPlayer player) {
        return new ContainerLiquidMatterCollector(player, this);
    }
    
    @SideOnly(value = Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiLiquidMatterCollector(new ContainerLiquidMatterCollector(player, this));
    }
    
    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {
    }
    
    @Override
    public void onNetworkEvent(EntityPlayer player, int id) {
        if(id == 1) this.reverseActive();
    }
}
