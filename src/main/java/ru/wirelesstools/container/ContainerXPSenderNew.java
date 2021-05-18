package ru.wirelesstools.container;

import java.util.List;

import ic2.core.ContainerFullInv;
import net.minecraft.entity.player.EntityPlayer;
import ru.wirelesstools.tiles.TileXPSenderElectric;

public class ContainerXPSenderNew extends ContainerFullInv<TileXPSenderElectric> {

	public ContainerXPSenderNew(EntityPlayer player, TileXPSenderElectric tile) {
		super(player, tile, 161);

	}

	public List<String> getNetworkedFields() {
		List<String> ret = super.getNetworkedFields();
		ret.add("energy");
		ret.add("sendradius");
		ret.add("playercountinradius");

		return ret;
	}

}
