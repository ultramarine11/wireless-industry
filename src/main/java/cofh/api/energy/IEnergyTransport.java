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

public interface IEnergyTransport
extends IEnergyProvider,
IEnergyReceiver {
    @Override
    public int getEnergyStored(ForgeDirection var1);

    public InterfaceType getTransportState(ForgeDirection var1);

    public boolean setTransportState(InterfaceType var1, ForgeDirection var2);

    public static enum InterfaceType {
        SEND,
        RECEIVE,
        BALANCE;


        public InterfaceType getOpposite() {
            return this == BALANCE ? BALANCE : (this == SEND ? RECEIVE : SEND);
        }

        public InterfaceType rotate() {
            return this.rotate(true);
        }

        public InterfaceType rotate(boolean bl) {
            if (bl) {
                return this == BALANCE ? RECEIVE : (this == RECEIVE ? SEND : BALANCE);
            }
            return this == BALANCE ? SEND : (this == SEND ? RECEIVE : BALANCE);
        }
    }
}

