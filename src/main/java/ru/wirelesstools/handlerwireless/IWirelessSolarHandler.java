package ru.wirelesstools.handlerwireless;

import ru.wirelesstools.tiles.IWirelessPanel;
import ru.wirelesstools.tiles.IWirelessStorage;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public interface IWirelessSolarHandler {

	void transferEnergyWirelessly(IWirelessPanel sender, IWirelessStorage receiver);
	
	void transmitEnergyWireleslyQGen(IWirelessStorage receiver, WirelessQuantumGeneratorBase qgen);

}
