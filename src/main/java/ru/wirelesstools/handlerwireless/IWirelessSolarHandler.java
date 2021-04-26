package ru.wirelesstools.handlerwireless;

import ru.wirelesstools.tiles.IWirelessPanel;
import ru.wirelesstools.tiles.IWirelessStorage;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public interface IWirelessSolarHandler {

	/**
	 * This method is used to transmit EU wirelessly from solar panel to storage.
	 * 
	 * @return transferred amount
	 */
	public double transferEnergyWirelessly(IWirelessPanel sender, IWirelessStorage receiver);
	
	public void transmitEnergyWireleslyQGen(IWirelessStorage receiver, WirelessQuantumGeneratorBase qgen);

}
