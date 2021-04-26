package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessUHSP extends TileWPBasePersonal {

	public TileWirelessUHSP() {
		super("wirelessUltimatePanelPersonal.name", ConfigWI.wuhsptier, ConfigWI.wuhspgenday, ConfigWI.wuhspgennight,
				ConfigWI.wuhspoutput, ConfigWI.wuhspstorage, ConfigWI.wuhsptransfer);

	}

}
