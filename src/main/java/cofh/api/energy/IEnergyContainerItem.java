/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package cofh.api.energy;

import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem {
    public int receiveEnergy(ItemStack var1, int var2, boolean var3);

    public int extractEnergy(ItemStack var1, int var2, boolean var3);

    public int getEnergyStored(ItemStack var1);

    public int getMaxEnergyStored(ItemStack var1);
}

