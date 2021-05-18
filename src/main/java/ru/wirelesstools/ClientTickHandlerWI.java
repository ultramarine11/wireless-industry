package ru.wirelesstools;

import cpw.mods.fml.client.FMLClientHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.wirelesstools.item.armor.ItemSolarWirelessEURFHelmet;
import ru.wirelesstools.utils.UtilFormatGUI;

public class ClientTickHandlerWI {

	public static Minecraft mc = FMLClientHandler.instance().getClient();

	public static void onTickRender() {
		EntityClientPlayerMP entityClientPlayer = mc.thePlayer;
		if (mc.theWorld != null && mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) {
			ItemStack stack = entityClientPlayer.getHeldItem();
			ItemStack itemarmor = entityClientPlayer.inventory.armorItemInSlot(2);
			ItemStack itemarmorhelmet = entityClientPlayer.inventory.armorItemInSlot(3);
			int xPos1 = 0;
			int yPos1 = 0;
			int xPos2 = 0;
			int yPos2 = 0;
			int yPos3 = 0;
			int yOffset1 = 1;
			int yOffset2 = 2;
			int yOffset3 = 3;

			if (stack != null && stack.getItem() == MainWI.luckyVajra) {
				double currCharge = ElectricItem.manager.getCharge(stack);
				String scharge = UtilFormatGUI.formatNumber(currCharge);
				/*
				 * if (currCharge < 1000.0D) { scharge = String.valueOf(currCharge); } else if
				 * (currCharge >= 1000.0D && currCharge < 1000000.0D) { scharge =
				 * String.format("%.2fK", currCharge / 1000.0); } else if (currCharge >=
				 * 1000000.0D) { scharge = String.format("%.2fM", currCharge / 1000000.0); }
				 */
				String elevelname = I18n.format("message.text.vajra.energyLevel", new Object[0]) + ": ";
				String stringCharge = elevelname + scharge + " Eu";
				if (MainWI.hudPos == 1) {
					xPos1 = 2;
					yPos1 = 9 + mc.fontRenderer.FONT_HEIGHT;
					mc.ingameGUI.drawString(mc.fontRenderer, stringCharge, xPos1, yPos1 + 1, 16777215);
				}
			}

			if (itemarmor != null && itemarmor.getItem() == MainWI.wirelessChestPlate) {
				double currChargeArmor = ElectricItem.manager.getCharge(itemarmor);
				float chargestatus = ((float) currChargeArmor
						/ (float) ((IElectricItem) itemarmor.getItem()).getMaxCharge(itemarmor)) * 100.0F;
				String schargearmor = UtilFormatGUI.formatNumber(currChargeArmor);
				/*
				 * if (currChargeArmor < 1000.0D) { schargearmor =
				 * String.valueOf(currChargeArmor); } else if (currChargeArmor >= 1000.0D &&
				 * currChargeArmor < 1000000.0D) { schargearmor = String.format("%.2fK",
				 * currChargeArmor / 1000.0); } else if (currChargeArmor >= 1000000.0D) {
				 * schargearmor = String.format("%.2fM", currChargeArmor / 1000000.0); }
				 */
				String echargenamearmor = I18n.format("info.text.wirelesschestplate.energyLevel", new Object[0]) + ": ";
				String stringChargeArmor = echargenamearmor + schargearmor + " Eu";
				String elevelarmor = ClientTickHandlerWI.getTextEnergyStatus(chargestatus);
				xPos2 = 2;
				yPos2 = 5 + yOffset2 + mc.fontRenderer.FONT_HEIGHT;
				yPos3 = 7 + mc.fontRenderer.FONT_HEIGHT;
				mc.ingameGUI.drawString(mc.fontRenderer, stringChargeArmor + " " + elevelarmor, xPos2,
						mc.fontRenderer.FONT_HEIGHT + 1, 16777215);
				// mc.ingameGUI.drawString(mc.fontRenderer, elevelarmor, xPos2, yPos3,
				// 0xFFFFFF);
			}

			if (itemarmorhelmet != null && itemarmorhelmet.getItem() == MainWI.wirelessEuRfHelmet) {
				NBTTagCompound nbt = StackUtil.getOrCreateNbtData(itemarmorhelmet);
				String nvmodestringoff = I18n.format("info.text.wirelesshelmet.ingame.nightmode.off", new Object[0]);
				String nvmodestringauto = I18n.format("info.text.wirelesshelmet.ingame.nightmode.auto", new Object[0]);
				String nvmodestringon = I18n.format("info.text.wirelesshelmet.ingame.nightmode.on", new Object[0]);

				/*
				 * ScaledResolution scaledresolutionlocal = new ScaledResolution(mc,
				 * mc.displayWidth, mc.displayHeight); int scaledX =
				 * scaledresolutionlocal.getScaledWidth(); int scaledY =
				 * scaledresolutionlocal.getScaledHeight();
				 * 
				 * int xPosNV0center = (scaledX -
				 * mc.fontRenderer.getStringWidth(nvmodestringoff)) / 2; int xPosNV1center =
				 * (scaledX - mc.fontRenderer.getStringWidth(nvmodestringauto)) / 2; int
				 * xPosNV2center = (scaledX - mc.fontRenderer.getStringWidth(nvmodestringon)) /
				 * 2;
				 */
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

			return "\u00A7e" + "(" + Integer.toString(Math.round(energyStatus)) + "%" + ")";
		} else if (energyStatus <= 30.0F) {

			return "\u00A7c" + "(" + Integer.toString(Math.round(energyStatus)) + "%" + ")";
		} else {

			return "\u00A7a" + "(" + Integer.toString(Math.round(energyStatus)) + "%" + ")";
		}
	}

}
