package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessMachinesCharger extends TileWirelessMachinesChargerBase {

	public TileWirelessMachinesCharger() {
		super(ConfigWI.machinesChargerMaxEnergyDouble, ConfigWI.machinesChargerMaxEnergyRF, ConfigWI.machinesChargerTier, "wirind.tilemachinescharger");
		
	}

}
