package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessASP extends TileWPBasePersonal {

	public TileWirelessASP() {
		super("wirelessAdvancedPanelPersonal.name", ConfigWI.wasptier, ConfigWI.waspgenday, ConfigWI.waspgennight,
				ConfigWI.waspoutput, ConfigWI.waspstorage, ConfigWI.wasptransfer);

	}

}
