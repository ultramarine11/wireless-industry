/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.util.ForgeDirection
 */
package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyProvider
extends IEnergyConnection {
    int extractEnergy(ForgeDirection var1, int var2, boolean var3);

    int getEnergyStored(ForgeDirection var1);

    int getMaxEnergyStored(ForgeDirection var1);
}

