package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessAdronSP extends TileWPBasePersonal {

    public TileWirelessAdronSP() {
        super("wirelessAdronPanelPersonal.name", ConfigWI.wadronsptier,
                ConfigWI.wadronspgenday, ConfigWI.wadronspgennight,
                ConfigWI.wadronspoutput, ConfigWI.wadronspstorage,
                ConfigWI.wadronsptransfer);
    }
}
