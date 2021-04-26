package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class WirelessQGen extends WirelessQuantumGeneratorBase {

	public WirelessQGen() {
		super(ConfigWI.wirelessqgenoutput, ConfigWI.wirelessqgentier, "qgenwireless.gui.name", ConfigWI.wirelessqgentransfer);
		
	}

}
