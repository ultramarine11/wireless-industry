package ru.wirelesstools.container;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;

import java.util.List;

public class ContainerWirelessMachinesChargerNew extends ContainerFullInv<TileWirelessMachinesChargerBase> {

    public ContainerWirelessMachinesChargerNew(EntityPlayer player, TileWirelessMachinesChargerBase base) {
        super(player, base, 161);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energyEU");
        ret.add("energyRF");
        ret.add("chargeEU");
        ret.add("chargeRF");

        return ret;
    }
}
