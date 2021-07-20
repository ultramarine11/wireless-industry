/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.util.ForgeDirection
 */
package cofh.api.energy;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyHandler
extends IEnergyProvider,
IEnergyReceiver {
    @Override
    public int receiveEnergy(ForgeDirection var1, int var2, boolean var3);

    @Override
    public int extractEnergy(ForgeDirection var1, int var2, boolean var3);

    @Override
    public int getEnergyStored(ForgeDirection var1);

    @Override
    public int getMaxEnergyStored(ForgeDirection var1);
}

