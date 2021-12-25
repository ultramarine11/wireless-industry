package ru.wirelesstools.tiles;

public interface IWirelessStorage {

	double getMaxCapacityOfStorage();

	double getCurrentEnergyInStorage();

	void addEnergy(double amount);

}
