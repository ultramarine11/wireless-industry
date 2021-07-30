package ru.wirelesstools.utils;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.ElectricItem;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.gui.GuiLiquidMatterCollector;

public class MiscUtils {

	@Deprecated
	public static void chargeRFItemFromArmor(ItemStack currentarmorstack, ItemStack rfitemstack) {
		IEnergyContainerItem item = (IEnergyContainerItem) rfitemstack.getItem();
		int amountRfCanBeReceivedIncludesLimit = item.receiveEnergy(rfitemstack, Integer.MAX_VALUE, true);
		if (amountRfCanBeReceivedIncludesLimit > 0) {
			double helmetChargeRF = ElectricItem.manager.getCharge(currentarmorstack) * ConfigWI.EUToRF_Multiplier;
			double realSentEnergyRF = Math.min(amountRfCanBeReceivedIncludesLimit, helmetChargeRF);
			double realDischargedEUFromHelmet = realSentEnergyRF / ConfigWI.EUToRF_Multiplier;

			item.receiveEnergy(rfitemstack, (int) realSentEnergyRF, false);
			ElectricItem.manager.discharge(currentarmorstack, realDischargedEUFromHelmet, Integer.MAX_VALUE, true,
					false, false);
		}
	}

	public static void chargeRFItemFromArmor2(ItemStack currentarmorstack, ItemStack rfitemstack) {
		IEnergyContainerItem item = (IEnergyContainerItem) rfitemstack.getItem();
		if (item.receiveEnergy(rfitemstack, Integer.MAX_VALUE, true) > 0) {
			int chargedEUfinally = item.receiveEnergy(rfitemstack,
					(int) (ElectricItem.manager.getCharge(currentarmorstack) * ConfigWI.EUToRF_Multiplier),
					false) / ConfigWI.EUToRF_Multiplier;
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

	public static void renderPercentageCircle(GuiLiquidMatterCollector gui, int partnumber,
											  int xoffset, int yoffset) {
		for(int i = 0; i <= partnumber; i++) {
			switch (i) {
				// Всего 6 аргументов.
				// Первые два - где нарисовать объект (x, y) начальные,
				// Вторые два - где располагается сам объект (x, y) начальные,
				// Последние два - ширина и высота объекта (x пикселей, y пикселей).
				case 0:
					break;
				case 1:
					gui.drawTexturedModalRect(xoffset + 51, yoffset + 34, 213, 2, 2, 6);// TODO ++
					break;
				case 2:
					gui.drawTexturedModalRect(xoffset + 53, yoffset + 34, 215, 2, 2, 6);// TODO ++
					break;
				case 3:
					gui.drawTexturedModalRect(xoffset + 55, yoffset + 35, 217, 3, 2, 6);// TODO ++
					break;
				case 4:
					gui.drawTexturedModalRect(xoffset + 57, yoffset + 36, 219, 4, 2, 7);// TODO ++
					break;
				case 5:
					gui.drawTexturedModalRect(xoffset + 59, yoffset + 37, 221, 5, 4, 4);// TODO ++
					break;
				case 6:
					gui.drawTexturedModalRect(xoffset + 59, yoffset + 41, 221, 9, 6, 2);// TODO ++
					break;
				case 7:
					gui.drawTexturedModalRect(xoffset + 59, yoffset + 43, 221, 11, 7, 2);// TODO ++
					break;
				case 8:
					gui.drawTexturedModalRect(xoffset + 60, yoffset + 45, 222, 13, 6, 2);// TODO ++
					break;
				case 9:
					gui.drawTexturedModalRect(xoffset + 61, yoffset + 47, 223, 15, 6, 2);// TODO ++
					break;
				case 10:
					gui.drawTexturedModalRect(xoffset + 61, yoffset + 49, 223, 17, 6, 2);// TODO ++
					break;
				case 11:
					gui.drawTexturedModalRect(xoffset + 61, yoffset + 51, 223, 19, 6, 2);// TODO ++
					break;
				case 12:
					gui.drawTexturedModalRect(xoffset + 60, yoffset + 53, 222, 21, 6, 2);// TODO ++
					break;
				case 13:
					gui.drawTexturedModalRect(xoffset + 58, yoffset + 55, 220, 23, 7, 2);// TODO ++
					break;
				case 14:
					gui.drawTexturedModalRect(xoffset + 57, yoffset + 57, 219, 25, 7, 2);// TODO ++
					break;
				case 15:
					gui.drawTexturedModalRect(xoffset + 57, yoffset + 59, 219, 27, 5, 4);// TODO ++
					break;
				case 16:
					gui.drawTexturedModalRect(xoffset + 55, yoffset + 58, 217, 26, 2, 6);// TODO ++
					break;
				case 17:
					gui.drawTexturedModalRect(xoffset + 53, yoffset + 59, 215, 27, 2, 5);// TODO ++
					break;
				case 18:
					gui.drawTexturedModalRect(xoffset + 51, yoffset + 59, 213, 27, 2, 5);// TODO ++
					break;
				case 19:
					gui.drawTexturedModalRect(xoffset + 49, yoffset + 59, 211, 27, 2, 5);// TODO ++
					break;
				case 20:
					gui.drawTexturedModalRect(xoffset + 47, yoffset + 59, 209, 27, 2, 5);// TODO ++
					break;
				case 21:
					gui.drawTexturedModalRect(xoffset + 45, yoffset + 57, 207, 25, 2, 6);// TODO ++
					break;
				case 22:
					gui.drawTexturedModalRect(xoffset + 43, yoffset + 55, 205, 23, 2, 7);// TODO ++
					break;
				case 23:
					gui.drawTexturedModalRect(xoffset + 38, yoffset + 54, 200, 22, 5, 7);// TODO ++
					break;
				case 24:
					gui.drawTexturedModalRect(xoffset + 37, yoffset + 52, 199, 20, 6, 2);// TODO ++
					break;
				case 25:
					gui.drawTexturedModalRect(xoffset + 37, yoffset + 49, 199, 17, 6, 3);// TODO ++
					break;
				case 26:
					gui.drawTexturedModalRect(xoffset + 37, yoffset + 46, 199, 14, 6, 3);// TODO ++
					break;
				case 27:
					gui.drawTexturedModalRect(xoffset + 37, yoffset + 43, 199, 11, 7, 3);// TODO ++
					break;
				case 28:
					gui.drawTexturedModalRect(xoffset + 38, yoffset + 40, 200, 8, 8, 3);// TODO ++
					break;
				case 29:
					gui.drawTexturedModalRect(xoffset + 41, yoffset + 36, 203, 4, 4, 4);// TODO ++
					break;
				case 30:
					gui.drawTexturedModalRect(xoffset + 45, yoffset + 35, 207, 3, 2, 5);// TODO ++
					break;
				case 31:
					gui.drawTexturedModalRect(xoffset + 47, yoffset + 35, 209, 3, 2, 5);// TODO ++
					break;
				case 32:
					gui.drawTexturedModalRect(xoffset + 49, yoffset + 35, 211, 3, 2, 5);// TODO ++
					break;
			}
		}
	}

}
