package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessProtonSP extends TileWPBasePersonal {

	public TileWirelessProtonSP() {
		super("wirelessProtonPanelPersonal.name", ConfigWI.wpsptier, ConfigWI.wpspgenday, ConfigWI.wpspgennight,
				ConfigWI.wpspoutput, ConfigWI.wpspstorage, ConfigWI.wpsptransfer);

	}

}
