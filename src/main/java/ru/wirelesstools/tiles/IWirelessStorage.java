package ru.wirelesstools.tiles;

public interface IWirelessStorage {

	double getMaxCapacityOfStorage();

	double getCurrentEnergyInStorage();

	double getFreeEnergy();

	void addEnergy(double amount);

}
