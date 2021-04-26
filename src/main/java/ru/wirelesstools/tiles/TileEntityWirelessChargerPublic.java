package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileEntityWirelessChargerPublic extends TileEntityWirelessCharger {

	public TileEntityWirelessChargerPublic() {
		super("wirelesschargerpublic.name", false, ConfigWI.maxstorageofchargers, ConfigWI.chargerpublicradius, ConfigWI.tierofchargers);
		
	}

}
