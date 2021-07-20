/*
 * Decompiled with CFR 0.150.
 */
package cofh.api.energy;

public interface IEnergyStorage {
    public int receiveEnergy(int var1, boolean var2);

    public int extractEnergy(int var1, boolean var2);

    public int getEnergyStored();

    public int getMaxEnergyStored();
}

