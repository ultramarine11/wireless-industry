package ru.wirelesstools.handlerwireless;

import ru.wirelesstools.tiles.IWirelessPanel;
import ru.wirelesstools.tiles.IWirelessStorage;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class WirelessTransmitHandler implements IWirelessSolarHandler {

	protected boolean isFreeEnergyInStorage(IWirelessStorage tile) {

		return this.getFreeEnergyInStorage(tile) > 0.0;
	}

	protected double getFreeEnergyInStorage(IWirelessStorage tile) {

		return (tile.getMaxCapacityOfStorage() - tile.getCurrentEnergyInStorage());
	}

	@Override
	public double transferEnergyWirelessly(IWirelessPanel sender, IWirelessStorage receiver) {
		if (this.isFreeEnergyInStorage(receiver)) {
			double real = Math.min(this.checkMinimumExtractedEnergy(sender), this.getFreeEnergyInStorage(receiver));
			sender.extractEnergy(real);
			receiver.addEnergy(real);
			return real;
		}
		return 0;
	}

	protected double checkMinimumExtractedEnergy(IWirelessPanel sender) {

		return Math.min(sender.getWirelessTransferLimit(), sender.getCurrentEnergyInPanel());
	}

	@Override
	public void transmitEnergyWireleslyQGen(IWirelessStorage receiver, WirelessQuantumGeneratorBase qgen) {
		if (this.isFreeEnergyInStorage(receiver)) {

			receiver.addEnergy(qgen.getWirelessTransferLimitQGen());
		}
	}

}
