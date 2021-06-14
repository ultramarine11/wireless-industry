package ru.wirelesstools.handlerwireless;

import net.minecraft.world.World;
import ru.wirelesstools.tiles.IWirelessCharger;

public interface IWirelessChargerHandler {

	/**
	 * This method is used to check players presence near charger block and to call
	 * next method which checks and then charges their inventory. If
	 * <code>isPrivate==true</code>, the only owner of charger will receive the
	 * energy
	 * 
	 * @param isPrivate this indicates if the Wireless Charger is private or public
	 * @param tile      Wireless charger
	 * @param radius    the distance of charging (in blocks)
	 * @return the number of players are currently around block (size of the players
	 *         list)
	 */
	int checkPlayersAround(boolean isPrivate, IWirelessCharger tile, int radius, World world);

}
