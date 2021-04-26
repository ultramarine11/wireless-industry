package ru.wirelesstools.tiles;

public interface IWirelessPanel {
	
	public double getMaxStorageOfPanel();

	public double getCurrentEnergyInPanel();

	public int getWirelessTransferLimit();

	public void extractEnergy(double amount);

}
