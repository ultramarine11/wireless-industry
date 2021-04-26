package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessPhSPPersonal extends TileWPBasePersonal {

	public TileWirelessPhSPPersonal() {
		super("wirelessPhotonicPanel.personal.name", ConfigWI.wphsptier, ConfigWI.wphspgenday,
				ConfigWI.wphspgennight, ConfigWI.wphspoutput, ConfigWI.wphspstorage, ConfigWI.wphsptransfer);

	}

}
