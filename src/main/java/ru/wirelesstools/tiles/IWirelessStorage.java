package ru.wirelesstools.tiles;

public interface IWirelessStorage {

	public double getMaxCapacityOfStorage();

	public double getCurrentEnergyInStorage();

	public void addEnergy(double amount);

}
