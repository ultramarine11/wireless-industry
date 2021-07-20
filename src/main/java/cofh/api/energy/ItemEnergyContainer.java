/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 */
package cofh.api.energy;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEnergyContainer extends Item implements IEnergyContainerItem {
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ItemEnergyContainer() {
	}

	public ItemEnergyContainer(int n) {
		this(n, n, n);
	}

	public ItemEnergyContainer(int n, int n2) {
		this(n, n2, n2);
	}

	public ItemEnergyContainer(int n, int n2, int n3) {
		this.capacity = n;
		this.maxReceive = n2;
		this.maxExtract = n3;
	}

	public ItemEnergyContainer setCapacity(int n) {
		this.capacity = n;
		return this;
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

	@Override
	public int receiveEnergy(ItemStack itemStack, int n, boolean bl) {
		if (itemStack.stackTagCompound == null) {
			itemStack.stackTagCompound = new NBTTagCompound();
		}
		int n2 = itemStack.stackTagCompound.getInteger("Energy");
		int n3 = Math.min(this.capacity - n2, Math.min(this.maxReceive, n));
		if (!bl) {
			itemStack.stackTagCompound.setInteger("Energy", n2 += n3);
		}
		return n3;
	}

	@Override
	public int extractEnergy(ItemStack itemStack, int n, boolean bl) {
		if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("Energy")) {
			return 0;
		}
		int n2 = itemStack.stackTagCompound.getInteger("Energy");
		int n3 = Math.min(n2, Math.min(this.maxExtract, n));
		if (!bl) {
			itemStack.stackTagCompound.setInteger("Energy", n2 -= n3);
		}
		return n3;
	}

	@Override
	public int getEnergyStored(ItemStack itemStack) {
		if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("Energy")) {
			return 0;
		}
		return itemStack.stackTagCompound.getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack itemStack) {
		return this.capacity;
	}
}
