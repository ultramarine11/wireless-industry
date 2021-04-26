package ru.wirelesstools.handlerwireless;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.tiles.IWirelessCharger;
import ru.wirelesstools.utils.WirelessUtil;

public class WirelessChargerHandler implements IWirelessChargerHandler {

	@Override
	public int checkPlayersAround(boolean isPrivate, IWirelessCharger tile, int radius, World world) {
		if (isPrivate) {
			if (tile.getOwnerCharger() != null) {
				AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(tile.getXCoord() - radius,
						tile.getYCoord() - radius, tile.getZCoord() - radius, tile.getXCoord() + radius,
						tile.getYCoord() + radius, tile.getZCoord() + radius);
				List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
				for (EntityPlayer localplayer : list) {
					if (localplayer != null) {
						if (!localplayer.getGameProfile().equals(tile.getOwnerCharger()))
							continue;

						this.checkPlayerInventory(localplayer, tile);
						return 1;
					}
				}
			}
		} else {
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(tile.getXCoord() - radius,
					tile.getYCoord() - radius, tile.getZCoord() - radius, tile.getXCoord() + radius,
					tile.getYCoord() + radius, tile.getZCoord() + radius);
			List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
			for (EntityPlayer localplayer : list) {
				if (localplayer != null) {

					this.checkPlayerInventory(localplayer, tile);
				}
			}
			int j = 0;
			for (int i = 0, sizelist = list.size(); i < sizelist; i++) {
				if (list.get(i) != null)
					j++;
			}
			return j;
		}
		return 0;
	}

	private void checkPlayerInventory(EntityPlayer player, IWirelessCharger tile) {
		for (ItemStack currentstackarmor : player.inventory.armorInventory) {
			if (currentstackarmor == null)
				continue;

			if (currentstackarmor.getItem() instanceof IElectricItem) {
				if (tile.getCurrentEnergyInCharger() > 0.0) {

					WirelessUtil.chargeItemEU(player, tile, currentstackarmor);
				}
			}

			if (currentstackarmor.getItem() instanceof IEnergyContainerItem) {
				if (tile.getCurrentEnergyInCharger() > 0.0) {

					WirelessUtil.chargeItemRF(player, tile, currentstackarmor);
				}
			}
		}

		for (ItemStack currentstackinventory : player.inventory.mainInventory) {
			if (currentstackinventory == null)
				continue;

			if (currentstackinventory.getItem() instanceof IElectricItem) {
				if (tile.getCurrentEnergyInCharger() > 0.0) {

					WirelessUtil.chargeItemEU(player, tile, currentstackinventory);
				}
			}

			if (currentstackinventory.getItem() instanceof IEnergyContainerItem) {
				if (tile.getCurrentEnergyInCharger() > 0.0) {

					WirelessUtil.chargeItemRF(player, tile, currentstackinventory);
				}
			}
		}
	}

}
