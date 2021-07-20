/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagCompound
 */
package cofh.api.energy;

import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorage {
	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public EnergyStorage(int n) {
		this(n, n, n);
	}

	public EnergyStorage(int n, int n2) {
		this(n, n2, n2);
	}

	public EnergyStorage(int n, int n2, int n3) {
		this.capacity = n;
		this.maxReceive = n2;
		this.maxExtract = n3;
	}

	public EnergyStorage readFromNBT(NBTTagCompound nBTTagCompound) {
		this.energy = nBTTagCompound.getInteger("Energy");
		if (this.energy > this.capacity) {
			this.energy = this.capacity;
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nBTTagCompound) {
		if (this.energy < 0) {
			this.energy = 0;
		}
		nBTTagCompound.setInteger("Energy", this.energy);
		return nBTTagCompound;
	}

	public void setCapacity(int n) {
		this.capacity = n;
		if (this.energy > n) {
			this.energy = n;
		}
	}

	public void setMaxTransfer(int n) {
		this.setMaxReceive(n);
		this.setMaxExtract(n);
	}

	public void setMaxReceive(int n) {
		this.maxReceive = n;
	}

	public void setMaxExtract(int n) {
		this.maxExtract = n;
	}

	public int getMaxReceive() {
		return this.maxReceive;
	}

	public int getMaxExtract() {
		return this.maxExtract;
	}

	public void setEnergyStored(int n) {
		this.energy = n;
		if (this.energy > this.capacity) {
			this.energy = this.capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	public void modifyEnergyStored(int n) {
		this.energy += n;
		if (this.energy > this.capacity) {
			this.energy = this.capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	@Override
	public int receiveEnergy(int n, boolean bl) {
		int n2 = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, n));
		if (!bl) {
			this.energy += n2;
		}
		return n2;
	}

	@Override
	public int extractEnergy(int n, boolean bl) {
		int n2 = Math.min(this.energy, Math.min(this.maxExtract, n));
		if (!bl) {
			this.energy -= n2;
		}
		return n2;
	}

	@Override
	public int getEnergyStored() {
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.capacity;
	}
}
