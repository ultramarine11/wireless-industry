package ru.wirelesstools.tiles;

public interface IWirelessPanel {

	double getCurrentEnergyInPanel();

	int getWirelessTransferLimit();

	void extractEnergy(double amount);

	int getChannel();

	void setChannel(int channel);

}
