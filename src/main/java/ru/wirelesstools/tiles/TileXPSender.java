package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileXPSender extends TileXPSenderElectric {

	public TileXPSender() {
		super(ConfigWI.maxstorageXPSender, ConfigWI.tierXPSender, "xpsender1.name", ConfigWI.amountXPsent, ConfigWI.energyperxppointXPSender);

	}

}
