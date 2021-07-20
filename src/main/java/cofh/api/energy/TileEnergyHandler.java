/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraftforge.common.util.ForgeDirection
 */
package cofh.api.energy;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnergyHandler extends TileEntity implements IEnergyHandler {
	protected EnergyStorage storage = new EnergyStorage(32000);

	public void readFromNBT(NBTTagCompound nBTTagCompound) {
		super.readFromNBT(nBTTagCompound);
		this.storage.readFromNBT(nBTTagCompound);
	}

	public void writeToNBT(NBTTagCompound nBTTagCompound) {
		super.writeToNBT(nBTTagCompound);
		this.storage.writeToNBT(nBTTagCompound);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection forgeDirection) {
		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection forgeDirection, int n, boolean bl) {
		return this.storage.receiveEnergy(n, bl);
	}

	@Override
	public int extractEnergy(ForgeDirection forgeDirection, int n, boolean bl) {
		return this.storage.extractEnergy(n, bl);
	}

	@Override
	public int getEnergyStored(ForgeDirection forgeDirection) {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection forgeDirection) {
		return this.storage.getMaxEnergyStored();
	}
}
