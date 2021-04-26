package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessQSP extends TileWPBasePersonal {

	public TileWirelessQSP() {
		super("wirelessQuantumPanelPersonal.name", ConfigWI.wqsptier, ConfigWI.wqspgenday, ConfigWI.wqspgennight,
				ConfigWI.wqspoutput, ConfigWI.wqspstorage, ConfigWI.wqsptransfer);

	}

}
