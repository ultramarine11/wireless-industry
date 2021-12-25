package ru.wirelesstools.tiles;

import com.mojang.authlib.GameProfile;

public interface IWirelessCharger {
	
	/** Мой метод, который уменьшает кол-во энергии в заряднике */
	void decreaseEnergy(double amount);

	double getCurrentEnergyInCharger();
	
	GameProfile getOwnerCharger();
	
	int getXCoord();
	
	int getYCoord();
	
	int getZCoord();

}
