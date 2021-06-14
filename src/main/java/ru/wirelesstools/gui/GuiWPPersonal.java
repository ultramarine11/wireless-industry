package ru.wirelesstools.gui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWPPersonal;
import ru.wirelesstools.packets.PacketGuiPressButton;
import ru.wirelesstools.tiles.TileWPBasePersonal;
import ru.wirelesstools.utils.UtilFormatGUI;

public class GuiWPPersonal extends GuiContainer {

	private static final ResourceLocation tex = new ResourceLocation(Reference.NAME, "textures/gui/wirelesssolarpanel.png");

	private TileWPBasePersonal tileentity;

	private GameProfile owner;

	public GuiWPPersonal(InventoryPlayer inventoryplayer, TileWPBasePersonal tileentitysolarpanel) {
		super(new ContainerWPPersonal(inventoryplayer, tileentitysolarpanel));
		this.tileentity = tileentitysolarpanel;
		this.xSize = 194;
		this.ySize = 168;
		this.allowUserInput = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(tex);
		int h = (this.width - this.xSize) / 2; // = 0 ?
		int k = (this.height - this.ySize) / 2; // = 0 ?

		// Всего 6 аргументов.
		// Первые два - где нарисовать объект(x, y),
		// Вторые два - где располагается сам объект (x, y),
		// Последние два - ширина и высота объекта (x пикселей, y пикселей).
		this.drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
		if (this.tileentity.storage > 0) {
			int l = this.tileentity.gaugeEnergyScaled(47);
			this.drawTexturedModalRect(h + 19, k + 24, 195, 0, l + 1, 14);
		}

		if (this.tileentity.skyIsVisible) {
			if (this.tileentity.sunIsUp) {

				this.drawTexturedModalRect(h + 24, k + 42, 195, 15, 14, 14);
			} else {

				this.drawTexturedModalRect(h + 24, k + 42, 210, 15, 14, 14);
			}

		}

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.owner = this.tileentity.getOwner();
		String formatPanelName = I18n.format(this.tileentity.panelName);
		int nmPos = (this.xSize - this.fontRendererObj.getStringWidth(formatPanelName)) / 2;
		this.fontRendererObj.drawString(formatPanelName, nmPos, 7, 7718655);
		String storageString = I18n.format("gui.WP.storage") + ": ";
		String maxOutputString = I18n.format("gui.WP.maxOutput") + ": ";
		String generatingString = I18n.format("gui.WP.generating") + ": ";
		String energyPerTickString = I18n.format("gui.WP.energyPerTick");
		String channelString = I18n.format("gui.WPPersonal.channel") + ": ";
		String owner1 = I18n.format("gui.WPP.owner") + ": ";
		this.fontRendererObj.drawString(
				storageString + UtilFormatGUI.formatNumberPanel(this.tileentity.storage) + " / "
						+ UtilFormatGUI.formatNumberPanel(this.tileentity.maxStorage), 77, 22, 13487565);
		this.fontRendererObj.drawString(maxOutputString + UtilFormatGUI.formatNumberPanel(this.tileentity.production) + " " + energyPerTickString, 77,
				32, 13487565);
		this.fontRendererObj.drawString(generatingString + UtilFormatGUI.formatNumberPanel(this.tileentity.generating) + " " + energyPerTickString, 77,
				42, 13487565);
		if (this.owner != null) {

			this.fontRendererObj.drawString(owner1 + this.owner.getName(), 77, 52, 13487565);
		}

		this.fontRendererObj.drawString(channelString + this.tileentity.getChannel(), 77, 62, 13487565);

	}

	public void initGui() {
		super.initGui();
		int xGuiPos = (this.width - this.xSize) / 2;
		int yGuiPos = (this.height - this.ySize) / 2;

		// 1 аргумент - ID кнопки,
		// 2 аргумент - её X позиция,
		// 3 аргумент - её Y позиция,
		// 4 аргумент - ширина,
		// 5 аргумент - высота,
		// 6 аргумент - текст.
		this.buttonList.add(new GuiButton(2, xGuiPos + 18, yGuiPos + 62, 22, 12,
				I18n.format("button.increment.channel")));
		this.buttonList.add(new GuiButton(3, xGuiPos + 45, yGuiPos + 62, 22, 12,
				I18n.format("button.decrement.channel")));
	}

	protected void actionPerformed(GuiButton button) {

		try {

			if (button.id == 2) {

				PacketGuiPressButton.issue(this.tileentity, 2);
			}

			if (button.id == 3) {

				PacketGuiPressButton.issue(this.tileentity, 3);
			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}

		super.actionPerformed(button);
	}

}
