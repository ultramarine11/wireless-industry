package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;

public interface IWirelessCharger {

	void decreaseEnergy(double amount);

	double getCurrentEnergyInCharger();
	
	GameProfile getOwnerCharger();
	
	int getXCoord();
	
	int getYCoord();
	
	int getZCoord();

}
