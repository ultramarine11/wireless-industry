package ru.wirelesstools.tiles;

import net.minecraft.world.World;

public interface IWirelessMachineCharger {

	double getChargerEnergyEU();

	int getChargerEnergyRF();

	void decreaseEnergy(double energy);

	void decreaseEnergyRF(int amount);

	World getChargerWorld();

	int getXCoord();

	int getZCoord();

	double getChargeRate();

	short getMode();

}
