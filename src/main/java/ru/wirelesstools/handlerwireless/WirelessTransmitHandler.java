package ru.wirelesstools.handlerwireless;

import ru.wirelesstools.tiles.IWirelessPanel;
import ru.wirelesstools.tiles.IWirelessStorage;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class WirelessTransmitHandler implements IWirelessSolarHandler {

    protected boolean isFreeEnergyInStorage(IWirelessStorage tile) {
        return tile.getFreeEnergy() > 0.0;
    }

    @Override
    public void transferEnergyWirelessly(IWirelessPanel sender, IWirelessStorage receiver) {
        double freeEnergy = receiver.getFreeEnergy();
        if(freeEnergy > 0.0) {
            double real = Math.min(this.checkMinimumExtractedEnergy(sender), freeEnergy);
            sender.extractEnergy(real);
            receiver.addEnergy(real);
        }
    }

    protected double checkMinimumExtractedEnergy(IWirelessPanel sender) {
        return Math.min(sender.getWirelessTransferLimit(), sender.getCurrentEnergyInPanel());
    }

    @Override
    public void transmitEnergyWireleslyQGen(IWirelessStorage receiver, WirelessQuantumGeneratorBase qgen) {
        if(this.isFreeEnergyInStorage(receiver)) {
            receiver.addEnergy(qgen.getWirelessTransferLimitQGen());
        }
    }

}
