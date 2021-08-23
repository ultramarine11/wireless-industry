package ru.wirelesstools.gui;

import ic2.core.GuiIC2;
import ic2.core.IC2;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.container.ContainerXPSenderNew;
import ru.wirelesstools.utils.UtilFormatNumber;

public class GuiXPSenderNew extends GuiIC2 {

	private ContainerXPSenderNew container;

	public GuiXPSenderNew(ContainerXPSenderNew container) {
		super(container, 175, 160);
		this.container = container;
	}

	@Override
	public String getName() {

		return StatCollector.translateToLocal(this.container.base.xpsendername);
	}

	@Override
	public ResourceLocation getResourceLocation() {

		return new ResourceLocation(Reference.IDNAME, "textures/gui/GuiXPSender.png");
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String tileentityname = I18n.format(this.container.base.xpsendername);
		String storageString = I18n.format("gui.wirind.xpsenderstorage") + ": ";
		String energyformatted = UtilFormatNumber.formatNumber(this.container.base.energy);
		String maxstorageformatted = UtilFormatNumber.formatNumber(this.container.base.maxStorage);
		String totalspentString = I18n.format("gui.wirind.xpsendertotalspent") + ": ";
		String spentPerPlayerString = I18n.format("gui.wirind.per.player");
		String radiusString = I18n.format("gui.wirind.radius.xp.send") + ": ";
		String xppointsString = I18n.format("gui.wirind.points.xp");
		String secondsString = I18n.format("gui.wirind.seconds");
		
		String xpAmountGiven = this.container.base.getXPPointsToSend() + " " + xppointsString
				+ " / " + ConfigWI.secondsXPSender + " " + secondsString;
		
		String stringEnergyAll = storageString + energyformatted + " / " + maxstorageformatted + " EU";

		String totalspentamount = String.valueOf(this.container.base.getTotalSpentEUValue());

		String totalSpentAll = totalspentString + totalspentamount + " EU " + spentPerPlayerString;
		
		String playercountStringAll = I18n.format("gui.wirind.xpsenderplayercount") + ": "
				+ this.container.base.getPlayerCount();
		String radiusAll = radiusString + this.container.base.getSendRadius();

		int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
		int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(stringEnergyAll)) / 2;
		int nmPos3 = (this.xSize - this.fontRendererObj.getStringWidth(totalSpentAll)) / 2;
		int nmPos4 = (this.xSize - this.fontRendererObj.getStringWidth(playercountStringAll)) / 2;
		int nmPos5 = (this.xSize - this.fontRendererObj.getStringWidth(radiusAll)) / 2;
		int nmPos6 = (this.xSize - this.fontRendererObj.getStringWidth(xpAmountGiven)) / 2;

		this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 4210752);
		this.fontRendererObj.drawString(stringEnergyAll, nmPos2, 16, 4210752);
		
		this.fontRendererObj.drawString(totalSpentAll, nmPos3, 27, 4210752);
		this.fontRendererObj.drawString(playercountStringAll, nmPos4, 66, 4210752);
		this.fontRendererObj.drawString(radiusAll, nmPos5/* + 26*/, 54, 4210752);
		this.fontRendererObj.drawString(xpAmountGiven, nmPos6 + 53, 40, 4210752);

	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.getResourceLocation());
		this.xoffset = (this.width - this.xSize) / 2;
		this.yoffset = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize);

		if (this.container.base.energy > 0) {
			int l = this.container.base.gaugeEnergyScaled(40);
			this.drawTexturedModalRect(this.xoffset + 63, this.yoffset + 39, 177, 15, l + 1, 10);
		}
	}

	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		int xMin = (this.width - this.xSize) / 2;
		int yMin = (this.height - this.ySize) / 2;
		int x = i - xMin;
		int y = j - yMin;

		if(x >= 8 && x <= 18 && y >= 51 && y <= 61) {
			IC2.network.get().initiateClientTileEntityEvent(this.container.base, 0);
		}

		if(x >= 24 && x <= 34 && y >= 51 && y <= 61) {
			IC2.network.get().initiateClientTileEntityEvent(this.container.base, 1);
		}
	}
	
	/*public void initGui() {
		super.initGui();
		int xGuiPos = (this.width - this.xSize) / 2;
		int yGuiPos = (this.height - this.ySize) / 2;
		// 1 аргумент - ID кнопки,
		// 2 аргумент - её X позиция,
		// 3 аргумент - её Y позиция,
		// 4 аргумент - ширина,
		// 5 аргумент - высота,
		// 6 аргумент - текст.
		this.buttonList.add(new GuiButton(0, xGuiPos + 36, yGuiPos + 50, 22, 12,
				I18n.format("button.xpsender.increment")));
		this.buttonList.add(new GuiButton(1, xGuiPos + 7, yGuiPos + 50, 22, 12,
				I18n.format("button.xpsender.decrement")));
	}*/
	
	/*protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 0) {
			
			IC2.network.get().initiateClientTileEntityEvent(this.container.base, 0);
		}
		
		if (guibutton.id == 1) {
			
			IC2.network.get().initiateClientTileEntityEvent(this.container.base, 1);
		}
	}*/

}
