package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessHSP extends TileWPBasePersonal {

	public TileWirelessHSP() {
		super("wirelessHybridPanelPersonal.name", ConfigWI.whsptier, ConfigWI.whspgenday, ConfigWI.whspgennight,
				ConfigWI.whspoutput, ConfigWI.whspstorage, ConfigWI.whsptransfer);

	}

}
