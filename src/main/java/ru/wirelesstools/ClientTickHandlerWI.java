package ru.wirelesstools;

import cpw.mods.fml.client.FMLClientHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.wirelesstools.utils.UtilFormatNumber;

public class ClientTickHandlerWI {

	public static Minecraft mc = FMLClientHandler.instance().getClient();

	public static void onTickRender() {
		EntityClientPlayerMP entityClientPlayer = mc.thePlayer;
		if (mc.theWorld != null && mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) {
			ItemStack itemvajra = entityClientPlayer.getHeldItem();
			ItemStack itemarmorchest = entityClientPlayer.inventory.armorItemInSlot(2);
			ItemStack itemarmorhelmet = entityClientPlayer.inventory.armorItemInSlot(3);
			int xPos1 = 0;
			int yPos1 = 0;
			int xPos2 = 0;
			if (itemvajra != null && itemvajra.getItem() == MainWI.luckyVajra) {
				String scharge = UtilFormatNumber.formatNumber(ElectricItem.manager.getCharge(itemvajra));
				String elevelname = I18n.format("message.text.vajra.energyLevel") + ": ";
				String stringCharge = elevelname + scharge + " Eu";
				xPos1 = 2;
				yPos1 = 9 + mc.fontRenderer.FONT_HEIGHT;
				mc.ingameGUI.drawString(mc.fontRenderer, stringCharge, xPos1, yPos1 + 1, 16777215);
			}

			if (itemarmorchest != null && itemarmorchest.getItem() == MainWI.wirelessChestPlate) {
				double currChargeArmor = ElectricItem.manager.getCharge(itemarmorchest);
				float chargestatus = ((float) currChargeArmor
						/ (float) ((IElectricItem) itemarmorchest.getItem()).getMaxCharge(itemarmorchest)) * 100.0F;
				String schargearmor = UtilFormatNumber.formatNumber(currChargeArmor);
				String echargenamearmor = I18n.format("info.text.wirelesschestplate.energyLevel") + ": ";
				String stringChargeArmor = echargenamearmor + schargearmor + " Eu";
				String elevelarmor = ClientTickHandlerWI.getTextEnergyStatus(chargestatus);
				xPos2 = 2;
				mc.ingameGUI.drawString(mc.fontRenderer, stringChargeArmor + " " + elevelarmor, xPos2,
						mc.fontRenderer.FONT_HEIGHT + 1, 16777215);
			}

			if (itemarmorhelmet != null && itemarmorhelmet.getItem() == MainWI.wirelessEuRfHelmet) {
				NBTTagCompound nbt = StackUtil.getOrCreateNbtData(itemarmorhelmet);
				String nvmodestringoff = I18n.format("info.text.wirelesshelmet.ingame.nightmode.off");
				String nvmodestringauto = I18n.format("info.text.wirelesshelmet.ingame.nightmode.auto");
				String nvmodestringon = I18n.format("info.text.wirelesshelmet.ingame.nightmode.on");

				// 0: off, 1: auto, 2: on
				int nvmode = nbt.getInteger("NightVisMode");
				switch (nvmode) {
				case 0:
					mc.ingameGUI.drawString(mc.fontRenderer, nvmodestringoff, 2, mc.fontRenderer.FONT_HEIGHT - 8,
							16733525);
					break;
				case 1:
					mc.ingameGUI.drawString(mc.fontRenderer, nvmodestringauto, 2, mc.fontRenderer.FONT_HEIGHT - 8,
							43690);
					break;
				case 2:
					mc.ingameGUI.drawString(mc.fontRenderer, nvmodestringon, 2, mc.fontRenderer.FONT_HEIGHT - 8,
							5635925);
					break;
				}
			}
		}
	}

	public static String getTextEnergyStatus(float energyStatus) {
		if (energyStatus <= 60.0F && energyStatus > 30.0F) {
			return "\u00A7e" + "(" + Math.round(energyStatus) + "%" + ")";
		} else if (energyStatus <= 30.0F) {
			return "\u00A7c" + "(" + Math.round(energyStatus) + "%" + ")";
		} else {
			return "\u00A7a" + "(" + Math.round(energyStatus) + "%" + ")";
		}
	}

}
