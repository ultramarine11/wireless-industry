package ru.wirelesstools.tiles;

public interface IWirelessMachineCharger {
	
	double getChargerEnergy();

	int getChargerEnergyRF();

	void decreaseEnergy(double energy);

	void decreaseEnergyRF(int amount);

}
