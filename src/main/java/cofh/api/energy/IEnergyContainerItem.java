/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package cofh.api.energy;

import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem {
    int receiveEnergy(ItemStack var1, int var2, boolean var3);

    int extractEnergy(ItemStack var1, int var2, boolean var3);

    int getEnergyStored(ItemStack var1);

    int getMaxEnergyStored(ItemStack var1);
}

