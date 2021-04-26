package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessSpSPPersonal extends TileWPBasePersonal {

	public TileWirelessSpSPPersonal() {
		super("wirelessSpectralPanel.personal.name", ConfigWI.wspsptier, ConfigWI.wspspgenday,
				ConfigWI.wspspgennight, ConfigWI.wspspoutput, ConfigWI.wspspstorage, ConfigWI.wspsptransfer);

	}

}
