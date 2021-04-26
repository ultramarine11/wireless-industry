package ru.wirelesstools.gui;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWCharger;
import ru.wirelesstools.tiles.TileEntityWirelessCharger;
import ru.wirelesstools.utils.UtilFormatGUI;

public class GuiWCharger extends GuiContainer {

	private static ResourceLocation tex = new ResourceLocation(Reference.NAME, "textures/gui/GuiWCharger.png");

	private TileEntityWirelessCharger tile;
	private GameProfile owner;
	private boolean isPrivateCharger;

	public GuiWCharger(InventoryPlayer inventoryplayer, TileEntityWirelessCharger tile) {
		super(new ContainerWCharger(inventoryplayer, tile));
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
		this.isPrivateCharger = this.tile.getIsPrivate();
		if (this.isPrivateCharger) {

			this.drawTexturedModalRect(h + 83, k + 40, 191, 15, 10, 11);
		} else {

			this.drawTexturedModalRect(h + 71, k + 35, 196, 35, 28, 14);
		}

		if (this.tile.energy > 0) {

			int l = this.tile.gaugeEnergyScaled(30);

			// Всего 6 аргументов.
			// Первые два - где нарисовать объект (x, y) начальные,
			// Вторые два - где располагается сам объект (x, y) начальные,
			// Последние два - ширина и высота объекта (x пикселей, y пикселей).
			this.drawTexturedModalRect(h + 13, k + 63 - l + 1, 178, 61 - l + 1, 10, l);
		}

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.owner = this.tile.getOwnerCharger();

		String tileentityname = I18n.format(this.tile.chargerName, new Object[0]);
		String storageString = I18n.format("gui.wirind.wirelesschargerstorage", new Object[0]) + ": ";
		String explainstring1 = I18n.format("gui.wirind.wirelesscharger.about1", new Object[0]);
		String explainstring2 = I18n.format("gui.wirind.wirelesscharger.about2", new Object[0]);
		String energyformatted = UtilFormatGUI.formatNumber(this.tile.energy);
		String maxstorageformatted = UtilFormatGUI.formatNumber((double) this.tile.maxStorage);
		String tileowner = I18n.format("gui.wirind.wirelesscharger.owner", new Object[0]) + ": ";
		String numplayersString = I18n.format("gui.wirind.numberplayers", new Object[0]) + ": ";
		String radiusString = I18n.format("gui.wirind.radiusofcharge", new Object[0]) + ": ";
		String blocksname = I18n.format("gui.wirind.wirelesscharger.blocks", new Object[0]);
		String noownerwhy = "No owner! Why?";

		int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
		int nmPos2 = (this.xSize - this.fontRendererObj
				.getStringWidth(storageString + energyformatted + " / " + maxstorageformatted + " EU")) / 2;
		int nmPos31 = (this.xSize - this.fontRendererObj.getStringWidth(explainstring1)) / 2;
		int nmPos32 = (this.xSize - this.fontRendererObj.getStringWidth(explainstring2)) / 2;
		int nmPos4 = (this.xSize - this.fontRendererObj
				.getStringWidth(radiusString + String.valueOf(this.tile.radiusofcharge) + " " + blocksname)) / 2;
		int nmPos5 = (this.xSize
				- this.fontRendererObj.getStringWidth(numplayersString + String.valueOf(this.tile.playercount))) / 2;
		int nmPos6 = (this.xSize - this.fontRendererObj.getStringWidth(tileowner + this.owner.getName())) / 2;
		int nmPosNoOwner = (this.xSize - this.fontRendererObj.getStringWidth(noownerwhy)) / 2;

		if (this.isPrivateCharger) {
			if (this.owner != null) {

				this.fontRendererObj.drawString(tileowner + this.owner.getName(), nmPos6, 56, 4210752);
			} else {

				this.fontRendererObj.drawString(noownerwhy, nmPosNoOwner, 55, 4210752);
			}
		} else {
			// GL11.glPushMatrix();
			// GL11.glScaled(0.9, 0.9, 1);
			this.fontRendererObj.drawString(explainstring1, nmPos31, 59, 4210752);
			this.fontRendererObj.drawString(explainstring2, nmPos32, 68, 4210752);
			this.fontRendererObj.drawString(numplayersString + String.valueOf(this.tile.playercount), nmPos5, 50,
					4210752);
			// GL11.glPopMatrix();
		}

		this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 4210752);
		this.fontRendererObj.drawString(storageString + energyformatted + " / " + maxstorageformatted + " EU", nmPos2,
				23, 4210752);
		this.fontRendererObj.drawString(radiusString + String.valueOf(this.tile.radiusofcharge) + " " + blocksname,
				nmPos4, 14, 4210752);

	}

}
