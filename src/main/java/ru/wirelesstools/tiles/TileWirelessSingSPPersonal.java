package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessSingSPPersonal extends TileWPBasePersonal {

	public TileWirelessSingSPPersonal() {
		super("wirelessSingularPanel.personal.name", ConfigWI.wsingsptier, ConfigWI.wsingspgenday,
				ConfigWI.wsingspgennight, ConfigWI.wsingspoutput, ConfigWI.wsingspstorage,
				ConfigWI.wsingsptransfer);

	}

}
