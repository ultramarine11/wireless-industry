package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileVajraCharger extends TileVajraChargerElectric {

	public TileVajraCharger() {
		super(ConfigWI.tierVajraCharger, ConfigWI.vajraChargerMaxStorage);
	}

}
