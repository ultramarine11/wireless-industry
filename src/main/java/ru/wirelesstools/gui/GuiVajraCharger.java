package ru.wirelesstools.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerVajraCharger;
import ru.wirelesstools.tiles.TileVajraChargerElectric;
import ru.wirelesstools.utils.UtilFormatNumber;

public class GuiVajraCharger extends GuiContainer {

	private static final ResourceLocation tex = new ResourceLocation(Reference.IDNAME, "textures/gui/GuiVajraCharger.png");
	private final TileVajraChargerElectric tileentity;

	public GuiVajraCharger(InventoryPlayer inventoryplayer, TileVajraChargerElectric tile) {
		super(new ContainerVajraCharger(inventoryplayer, tile));
		this.tileentity = tile;
		this.allowUserInput = false;
		this.xSize = 175;
		this.ySize = 160;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(tex);
		int h = (this.width - this.xSize) / 2; // = 0 ?
		int k = (this.height - this.ySize) / 2; // = 0 ?

		// Всего 6 аргументов.
		// Первые два - где нарисовать объект (x, y) начальные,
		// Вторые два - где располагается сам объект (x, y) начальные,
		// Последние два - ширина и высота объекта (x пикселей, y пикселей).
		this.drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
		if (this.tileentity.energy > 0) {

			int l = this.tileentity.gaugeEnergyScaled(77);
			this.drawTexturedModalRect(h + 49, k + 41, 177, 15, l + 1, 10);
		}

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		String tileentityname = I18n.format("gui.wirind.vajrachargername");
		String storageString = I18n.format("gui.wirind.vajrachargerstorage") + ": ";
		String aboutstring = I18n.format("gui.wirind.vajracharger.about");

		String energyformatted = UtilFormatNumber.formatNumber(this.tileentity.energy);
		String maxstorageformatted = UtilFormatNumber.formatNumber(this.tileentity.maxStorage);

		int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
		int nmPos2 = (this.xSize
				- this.fontRendererObj.getStringWidth(storageString + energyformatted + " / " + maxstorageformatted))
				/ 2;
		int nmPos3 = (this.xSize - this.fontRendererObj.getStringWidth(aboutstring)) / 2;

		// this.fontRendererObj.drawString(String, x, y, color)
		this.fontRendererObj.drawString(tileentityname, nmPos1, 7, 4210752);
		this.fontRendererObj.drawString(storageString + energyformatted + " / " + maxstorageformatted, nmPos2, 20,
				4210752);
		this.fontRendererObj.drawString(aboutstring, nmPos3, 63, 4210752);

	}

}
