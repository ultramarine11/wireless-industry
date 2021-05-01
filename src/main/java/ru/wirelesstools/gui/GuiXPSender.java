package ru.wirelesstools.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import ru.wirelesstools.container.ContainerXPSender;
import ru.wirelesstools.tiles.TileXPSenderElectric;

public class GuiXPSender extends GuiContainer {

	private TileXPSenderElectric tile;

	public GuiXPSender(InventoryPlayer inventoryplayer, TileXPSenderElectric tile) {
		super(new ContainerXPSender(inventoryplayer, tile));
		this.tile = tile;
		this.xSize = 175;
		this.ySize = 160;
		this.allowUserInput = false;

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

	}

}