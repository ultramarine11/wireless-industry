package ru.wirelesstools.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWirelessMachinesCharger;
import ru.wirelesstools.tiles.TileWirelessMachinesChargerBase;
import ru.wirelesstools.utils.UtilFormatGUI;

public class GuiWirelessMachinesCharger extends GuiContainer {

	private static final ResourceLocation tex = new ResourceLocation(Reference.NAME,
			"textures/gui/GuiWirelessMachinesCharger.png");

	private TileWirelessMachinesChargerBase tile;

//	private boolean isCharging;

	public GuiWirelessMachinesCharger(InventoryPlayer inventoryplayer, TileWirelessMachinesChargerBase tile) {
		super(new ContainerWirelessMachinesCharger(inventoryplayer, tile));
		this.tile = tile;
		this.allowUserInput = false;
		this.xSize = 175;
		this.ySize = 160;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(tex);
		int h = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);

		if (this.tile.energy > 0) {

			int l = this.tile.gaugeEnergyScaled(77);
			this.drawTexturedModalRect(h + 49, k + 41, 177, 15, l + 1, 10);
		}

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		String tileentityname = I18n.format(this.tile.chargername);
		String storageString = I18n.format("gui.wirind.wirelessmachineschargerstorage") + ": ";
		String energyformatted = UtilFormatGUI.formatNumber(this.tile.energy);
		String maxstorageformatted = UtilFormatGUI.formatNumber(this.tile.getCapacity());

		String stringEnergyAll = storageString + energyformatted + " / " + maxstorageformatted + " EU";

		// String isChargingString =
		// I18n.format("gui.wirind.wirelessmachinescharger.isCharging", new Object[0]);
		// String isNotChargingString =
		// I18n.format("gui.wirind.wirelessmachinescharger.isNotCharging", new
		// Object[0]);

		int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
		int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(stringEnergyAll)) / 2;
		// int nmPos3 = (this.xSize -
		// this.fontRendererObj.getStringWidth(isChargingString)) / 2;
		// int nmPos4 = (this.xSize -
		// this.fontRendererObj.getStringWidth(isNotChargingString)) / 2;

		this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 4210752);
		this.fontRendererObj.drawString(stringEnergyAll, nmPos2, 23, 4210752);
		/*
		 * if(this.isCharging) {
		 * 
		 * // dark green color code 43520
		 * this.fontRendererObj.drawString(isChargingString, nmPos3, 60, 43520); } else
		 * {
		 * 
		 * // dark red color code 11141120
		 * this.fontRendererObj.drawString(isNotChargingString, nmPos4, 60, 11141120); }
		 */

	}

}
