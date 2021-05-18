package ru.wirelesstools.container;

import java.util.List;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.TileEntityWirelessCharger;

public class ContainerWChargerNew extends ContainerFullInv<TileEntityWirelessCharger> {

	public ContainerWChargerNew(EntityPlayer player, TileEntityWirelessCharger base) {
		super(player, base, 161);

	}

	public List<String> getNetworkedFields() {
		List<String> ret = super.getNetworkedFields();
		ret.add("energy");
		ret.add("radiusofcharge");
		ret.add("playercount");

		return ret;
	}

}
