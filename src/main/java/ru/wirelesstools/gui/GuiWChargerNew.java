package ru.wirelesstools.gui;

import ic2.core.GuiIC2;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWChargerNew;
import ru.wirelesstools.utils.UtilFormatNumber;

public class GuiWChargerNew extends GuiIC2 {

	private ContainerWChargerNew container;

	public GuiWChargerNew(ContainerWChargerNew container) {
		super(container, 175, 160);
		this.container = container;
	}

	@Override
	public String getName() {

		return StatCollector.translateToLocal(this.container.base.chargerName);
	}

	@Override
	public ResourceLocation getResourceLocation() {

		return new ResourceLocation(Reference.IDNAME, "textures/gui/GuiWCharger.png");
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String ownername = this.container.base.getOwnerCharger().getName();

		String tileentityname = I18n.format(this.container.base.chargerName);
		String storageString = I18n.format("gui.wirind.wirelesschargerstorage") + ": ";
		String energyformatted = UtilFormatNumber.formatNumber(this.container.base.energy);
		String maxstorageformatted = UtilFormatNumber.formatNumber(this.container.base.maxStorage);
		String tileowner = I18n.format("gui.wirind.wirelesscharger.owner") + ": ";
		String radiusString = I18n.format("gui.wirind.radiusofcharge") + ": ";
		String noownerwhy = I18n.format("gui.wirind.noowner.why");

		String stringEnergyAll = storageString + energyformatted + " / " + maxstorageformatted + " EU";
		String radiusAll = radiusString + this.container.base.getRadiusOfCharge();
		String playercountStringAll = I18n.format("gui.wirind.numberplayers") + ": "
				+ this.container.base.playercount;
		String ownerAll = tileowner + ownername;

		int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
		int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(stringEnergyAll)) / 2;
		int nmPos4 = (this.xSize - this.fontRendererObj.getStringWidth(radiusAll)) / 2;
		int nmPos5 = (this.xSize - this.fontRendererObj.getStringWidth(playercountStringAll)) / 2;
		int nmPos6 = (this.xSize - this.fontRendererObj.getStringWidth(ownerAll)) / 2;
		int nmPosNoOwner = (this.xSize - this.fontRendererObj.getStringWidth(noownerwhy)) / 2;

		if (this.container.base.getIsPrivate()) {
			if (this.container.base.getOwnerCharger() != null) {
				this.fontRendererObj.drawString(ownerAll, nmPos6, 14, 4210752);
			} else {
				this.fontRendererObj.drawString(noownerwhy, nmPosNoOwner, 14, 4210752);
			}

		} else {
			this.fontRendererObj.drawString(playercountStringAll, nmPos5, 14, 4210752);
		}

		this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 4210752);
		this.fontRendererObj.drawString(stringEnergyAll, nmPos2, 23, 4210752);
		this.fontRendererObj.drawString(radiusAll, nmPos4, 50, 4210752);

	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.getResourceLocation());
		this.xoffset = (this.width - this.xSize) / 2;
		this.yoffset = (this.height - this.ySize) / 2;
		// Всего 6 аргументов.
		// Первые два - где нарисовать объект (x, y) начальные,
		// Вторые два - где располагается сам объект (x, y) начальные,
		// Последние два - ширина и высота объекта (x пикселей, y пикселей).
		this.drawTexturedModalRect(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize);

		if (this.container.base.getIsPrivate()) {

			this.drawTexturedModalRect(this.xoffset + 81, this.yoffset + 37, 191, 15, 10, 11);
		} else {

			this.drawTexturedModalRect(this.xoffset + 71, this.yoffset + 35, 196, 35, 28, 14);
		}

		if (this.container.base.energy > 0) {

			int l = this.container.base.gaugeEnergyScaled(30);
			this.drawTexturedModalRect(this.xoffset + 13, this.yoffset + 63 - l + 1, 178, 61 - l + 1, 10, l);
		}
	}

	public void initGui() {
		super.initGui();
		int xGuiPos = (this.width - this.xSize) / 2;
		int yGuiPos = (this.height - this.ySize) / 2;
		int guiCenter = this.xSize / 2;
		int realcenter = xGuiPos + guiCenter;
		int xButtonSize = 22;
		int centerOffsetButton = xButtonSize / 2 + 2;
		// 1 аргумент - ID кнопки,
		// 2 аргумент - её X позиция,
		// 3 аргумент - её Y позиция,
		// 4 аргумент - ширина,
		// 5 аргумент - высота,
		// 6 аргумент - текст.
		this.buttonList.add(new GuiButton(0, realcenter + centerOffsetButton - 11, yGuiPos + 60, xButtonSize, 12,
				I18n.format("button.wcharger.increment")));
		this.buttonList.add(new GuiButton(1, realcenter - centerOffsetButton - 11, yGuiPos + 60, xButtonSize, 12,
				I18n.format("button.wcharger.decrement")));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 0) {
			IC2.network.get().initiateClientTileEntityEvent(this.container.base, 0);
		}

		if (guibutton.id == 1) {
			IC2.network.get().initiateClientTileEntityEvent(this.container.base, 1);
		}
	}

}
