package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessDifractSPPersonal extends TileWPBasePersonal {

	public TileWirelessDifractSPPersonal() {
		super("wirelessAbsorbtionPanel.personal.name", ConfigWI.wdifrsptier, ConfigWI.wdifrspgenday,
				ConfigWI.wdifrspgennight, ConfigWI.wdifrspoutput, ConfigWI.wdifrspstorage, ConfigWI.wdifrsptransfer);
	}

}
