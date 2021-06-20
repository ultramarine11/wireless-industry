package ru.wirelesstools.tiles;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.TileEntityLiquidTankElectricMachine;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;

public class TileLiquidMatterCollector extends TileEntityInventory implements IFluidHandler {

    public final InvSlotOutput outputSlot;
    public final InvSlotConsumableLiquid containerslot;
    protected final FluidTank fluidTank;
    public final String mattercollectorname;

    public TileLiquidMatterCollector(String collectorname) {
        this.fluidTank = new FluidTank(1000000);
        this.mattercollectorname = collectorname;
        this.outputSlot = new InvSlotOutput(this, "output", 1, 1);
        this.containerslot = new InvSlotConsumableLiquidByList(this, "containerslot", 2, InvSlot.Access.I, 1, InvSlot.InvSide.TOP, InvSlotConsumableLiquid.OpType.Fill, new Fluid[]{BlocksItems.getFluid(InternalName.fluidUuMatter)});
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        if(!this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord).chunkTileEntityMap.isEmpty()) {
            for (TileEntity tile : new ArrayList<TileEntity>(this.getWorldObj()
                    .getChunkFromBlockCoords(this.xCoord, this.zCoord).chunkTileEntityMap.values())) {
                if(tile instanceof TileEntityLiquidTankElectricMachine) {
                    this.tryFillCollector((TileEntityLiquidTankElectricMachine)tile);
                }
            }
        }
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }

    private void tryFillCollector(TileEntityLiquidTankElectricMachine tiletank) {
        if(tiletank.getFluidStackfromTank().getFluid() != null
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

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return !this.canFill(from, resource.getFluid()) ? 0 : this.getFluidTank().fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource != null && resource.isFluidEqual(this.getFluidTank().getFluid())) {
            return !this.canDrain(from, resource.getFluid()) ? null : this.getFluidTank().drain(resource.amount, doDrain);
        } else {
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
        return new FluidTankInfo[]{this.getFluidTank().getInfo()};
    }

    @Override
    public String getInventoryName() {
        return null;
    }
}
