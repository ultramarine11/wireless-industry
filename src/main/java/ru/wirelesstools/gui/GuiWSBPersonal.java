package ru.wirelesstools.gui;

import com.mojang.authlib.GameProfile;
import ic2.core.IC2;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWSBPersonal;
import ru.wirelesstools.tiles.TileWirelessStorageBasePersonal;
import ru.wirelesstools.utils.UtilFormatNumber;

public class GuiWSBPersonal extends GuiContainer {

    private static final ResourceLocation tex = new ResourceLocation(Reference.IDNAME, "textures/gui/GuiTWSBPersonal.png");

    private TileWirelessStorageBasePersonal tileentity;

    private GameProfile owner;

    public GuiWSBPersonal(InventoryPlayer inventoryplayer, TileWirelessStorageBasePersonal te) {
        super(new ContainerWSBPersonal(inventoryplayer, te));
        this.tileentity = te;
        this.xSize = 175;
        this.ySize = 160;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(tex);

        int h = (this.width - this.xSize) / 2; // = 0 ?
        int k = (this.height - this.ySize) / 2; // = 0 ?

        // Всего 6 аргументов.
        // Первые два - где нарисовать объект,
        // Вторые два - где располагается сам объект,
        // Последние два - ширина и высота объекта.
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        if(this.tileentity.energy > 0) {

            int l = this.tileentity.gaugeEnergyScaled(24);
            drawTexturedModalRect(h + 79, k + 34, 176, 14, l + 1, 18);
        }

    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.owner = this.tileentity.getOwner();
        String formatWSBName = I18n.format(this.tileentity.wsbPersName);
        int nmPos = (this.xSize - this.fontRendererObj.getStringWidth(formatWSBName)) / 2;
        this.fontRendererObj.drawString(formatWSBName, nmPos, 6, 4210752);
        String storageString = I18n.format("gui.WSB.storage") + ": ";
        String maxOutputString = I18n.format("gui.WSBP.maxOutput") + ": ";
        String channelString = I18n.format("gui.WSBPersonal.channel") + ": ";
        String owner1 = I18n.format("gui.WSB.owner") + ": ";
        String storage_all = storageString + UtilFormatNumber.formatNumberPanel(this.tileentity.energy) + " / "
                + UtilFormatNumber.formatNumberPanel(this.tileentity.maxStorage) + " Eu";
        int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(storage_all)) / 2;
        String max_output_all = maxOutputString + UtilFormatNumber.formatNumberPanel(this.tileentity.output) + " Eu/t";
        int nmPos3 = (this.xSize - this.fontRendererObj.getStringWidth(max_output_all)) / 2;
        this.fontRendererObj.drawString(storage_all, nmPos2, 24, 4210752);
        this.fontRendererObj.drawString(max_output_all, nmPos3, 15, 4210752);
        if(this.owner != null)
            this.fontRendererObj.drawString(owner1 + this.owner.getName(), 75, 60, 4210752);


        this.fontRendererObj.drawString(channelString + this.tileentity.channel, 16, 44, 4210752);
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

        if(x >= 17 && x <= 27 && y >= 58 && y <= 68) {
            IC2.network.get().initiateClientTileEntityEvent(this.tileentity, 0);
        }

        if(x >= 33 && x <= 43 && y >= 58 && y <= 68) {
            IC2.network.get().initiateClientTileEntityEvent(this.tileentity, 1);
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
        this.buttonList.add(new GuiButton(4, xGuiPos + 18, yGuiPos + 50, 20, 20,
                I18n.format("button.increment.channel")));
        this.buttonList.add(new GuiButton(5, xGuiPos + 50, yGuiPos + 50, 20, 20,
                I18n.format("button.decrement.channel")));
    }*/

    /*protected void actionPerformed(GuiButton button) {
        try {
            if(button.id == 4) {
                IC2.network.get().initiateClientTileEntityEvent(this.tileentity, 0);
            }

            if(button.id == 5) {
                IC2.network.get().initiateClientTileEntityEvent(this.tileentity, 1);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        super.actionPerformed(button);
    }*/

}
