package ru.wirelesstools.tiles;

import ru.wirelesstools.config.ConfigWI;

public class TileWirelessBarionSP extends TileWPBasePersonal {

    public TileWirelessBarionSP() {
        super("wirelessBarionPanelPersonal.name", ConfigWI.wbarionsptier,
                ConfigWI.wbarionspgenday,
                ConfigWI.wbarionspgennight, ConfigWI.wbarionspoutput,
                ConfigWI.wbarionspstorage, ConfigWI.wbarionsptransfer);
    }
}
