package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileEntityWirelessChargerPrivate extends TileEntityWirelessCharger {

	public TileEntityWirelessChargerPrivate() {
		super("wirelesschargerprivate.name", true, ConfigWI.maxstorageofchargers, ConfigWI.tierofchargers);
		
	}

}
