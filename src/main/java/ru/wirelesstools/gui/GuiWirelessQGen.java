package ru.wirelesstools.gui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerQGenWireless;
import ru.wirelesstools.tiles.WirelessQuantumGeneratorBase;

public class GuiWirelessQGen extends GuiContainer {

    private static final ResourceLocation tex = new ResourceLocation(Reference.IDNAME, "textures/gui/GUIQuantumGenerator.png");

    private WirelessQuantumGeneratorBase tile;

    public GuiWirelessQGen(InventoryPlayer inventoryplayer, WirelessQuantumGeneratorBase tile) {
        super(new ContainerQGenWireless(inventoryplayer, tile));
        this.allowUserInput = false;
        this.xSize = 176;
        this.ySize = 149;
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(tex);
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        GameProfile owner = this.tile.getOwner();

        String tileentityname = I18n.format(this.tile.wirelessQGenName);
        String tileisCharging = I18n.format("gui.wirind.wirelessqgen.ischarging");
        String tileisNotCharging = I18n.format("gui.wirind.wirelessqgen.isnotcharging");
        String tileowner = I18n.format("gui.wirind.wirelessqgen.owner") + ": " + owner.getName();
        String noownerwhy = "No owner! Why?";

        String tileoutput = I18n.format("gui.wirind.wirelessqgen.output") + ": " + String.valueOf(this.tile.getOutput()) + " Eu/t";
        String tiletransmit = I18n.format("gui.wirind.wirelessqgen.transmit") + ": " + String.valueOf(this.tile.getWirelessTransferLimitQGen()) + " Eu/t";

        int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
        int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(tileisCharging)) / 2;
        int nmPos3 = (this.xSize - this.fontRendererObj.getStringWidth(tileisNotCharging)) / 2;
        int nmPos4 = (this.xSize - this.fontRendererObj.getStringWidth(tileowner)) / 2;
        int nmPosNoOwner = (this.xSize - this.fontRendererObj.getStringWidth(noownerwhy)) / 2;

        int nmPos5 = (this.xSize - this.fontRendererObj.getStringWidth(tileoutput)) / 2;
        int nmPos6 = (this.xSize - this.fontRendererObj.getStringWidth(tiletransmit)) / 2;

        this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 16777215);

        if(this.tile.isCharging)
            this.fontRendererObj.drawString(tileisCharging, nmPos2, 18, 5635925); // green color code 5635925
        else
            this.fontRendererObj.drawString(tileisNotCharging, nmPos3, 18, 16733525); // red color code 16733525

        if(owner != null)
            this.fontRendererObj.drawString(tileowner, nmPos4, 30, 16777215);
        else
            this.fontRendererObj.drawString(noownerwhy, nmPosNoOwner, 30, 16777215);

        this.fontRendererObj.drawString(tileoutput, nmPos5, 30 + this.fontRendererObj.FONT_HEIGHT + 1, 16777215);
        this.fontRendererObj.drawString(tiletransmit, nmPos6, 30 + 2 * (this.fontRendererObj.FONT_HEIGHT + 1), 16777215);

    }

}
