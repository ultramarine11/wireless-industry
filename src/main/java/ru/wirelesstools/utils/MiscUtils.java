package ru.wirelesstools.utils;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.config.ConfigWI;

public class MiscUtils {

	@Deprecated
	public static void chargeRFItemFromArmor(ItemStack currentarmorstack, ItemStack rfitemstack) {
		IEnergyContainerItem item = (IEnergyContainerItem) rfitemstack.getItem();
		int amountRfCanBeReceivedIncludesLimit = item.receiveEnergy(rfitemstack, Integer.MAX_VALUE, true);
		if (amountRfCanBeReceivedIncludesLimit > 0) {
			double helmetChargeRF = ElectricItem.manager.getCharge(currentarmorstack) * ConfigWI.EuToRfmultiplier;
			double realSentEnergyRF = Math.min(amountRfCanBeReceivedIncludesLimit, helmetChargeRF);
			double realDischargedEUFromHelmet = realSentEnergyRF / ConfigWI.EuToRfmultiplier;

			item.receiveEnergy(rfitemstack, (int) realSentEnergyRF, false);
			ElectricItem.manager.discharge(currentarmorstack, realDischargedEUFromHelmet, Integer.MAX_VALUE, true,
					false, false);
		}
	}

	public static void chargeRFItemFromArmor2(ItemStack currentarmorstack, ItemStack rfitemstack) {
		IEnergyContainerItem item = (IEnergyContainerItem) rfitemstack.getItem();
		if (item.receiveEnergy(rfitemstack, Integer.MAX_VALUE, true) > 0) {
			int chargedEUfinally = item.receiveEnergy(rfitemstack,
					(int) (ElectricItem.manager.getCharge(currentarmorstack) * ConfigWI.EuToRfmultiplier),
					false) / ConfigWI.EuToRfmultiplier;
			ElectricItem.manager.discharge(currentarmorstack, chargedEUfinally, Integer.MAX_VALUE, true,
					false, false);
		}
	}
	
	public static void chargeEUItemFromArmor(ItemStack stackToCharge, ItemStack thisarmor) {
		if (ElectricItem.manager.getCharge(thisarmor) > 0.0) {
			if (ElectricItem.manager.charge(stackToCharge, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true,
					true) > 0.0) {
				ElectricItem.manager.discharge(thisarmor,
						ElectricItem.manager.charge(stackToCharge, Double.MAX_VALUE, Integer.MAX_VALUE, false, false),
						Integer.MAX_VALUE, true, false, false);
			}
		}
	}
}
