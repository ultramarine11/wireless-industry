package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessNeutronSPPersonal extends TileWPBasePersonal {

	public TileWirelessNeutronSPPersonal() {
		super("wirelessNeutronPanelPersonal.name", ConfigWI.wneusptier, ConfigWI.wneuspgenday,
				ConfigWI.wneuspgennight, ConfigWI.wneuspoutput, ConfigWI.wneuspstorage, ConfigWI.wneusptransfer);
	}

}
