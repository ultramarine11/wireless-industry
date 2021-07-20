/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.util.ForgeDirection
 */
package cofh.api.energy;

import cofh.api.energy.IEnergyConnection;
import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyReceiver
extends IEnergyConnection {
    public int receiveEnergy(ForgeDirection var1, int var2, boolean var3);

    public int getEnergyStored(ForgeDirection var1);

    public int getMaxEnergyStored(ForgeDirection var1);
}

