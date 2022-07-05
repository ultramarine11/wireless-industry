package ru.wirelesstools.tiles;

import net.minecraft.world.World;

public interface IWirelessQGen {

    World getQGenWorld();

    int getXCoord();

    int getYCoord();

    int getZCoord();

    int getModeTransmitting();

    int getChargeRateByMode(int mode);

    void setMachinesCountInChunk(int value);

    int getMachinesCountInChunk();

    void setStatus(ChargeStatus status);

    ChargeStatus getStatus();

    enum ChargeStatus {
        NOT_CHARGING,
        WAITING,
        CHARGING;
    }

}
