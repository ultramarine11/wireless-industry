package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileMachinesCharger extends TileWirelessMachinesChargerBase {

	public TileMachinesCharger() {
		super(ConfigWI.machinesChargerMaxEnergyDouble, ConfigWI.machinesChargerMaxEnergyRF, ConfigWI.machinesChargerTier, "wirind.tilemachinescharger");
		
	}

}
