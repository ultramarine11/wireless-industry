package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class WirelessQGenerator extends WirelessQuantumGeneratorBase {

	public WirelessQGenerator() {
		super(ConfigWI.wirelessqgenoutput, ConfigWI.wirelessqgentier, "qgenwireless.gui.name", ConfigWI.wirelessqgentransfer);
		
	}

}
