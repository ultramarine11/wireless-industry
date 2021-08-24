package ru.wirelesstools.gui;

import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWirelessMachinesChargerNew;
import ru.wirelesstools.utils.UtilFormatNumber;

public class GuiWirelessMachinesChargerNew extends GuiIC2 {

    private ContainerWirelessMachinesChargerNew container;

    public GuiWirelessMachinesChargerNew(ContainerWirelessMachinesChargerNew container) {
        super(container, 175, 160);
        this.container = container;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String tileentityname = I18n.format(this.container.base.chargername);
        String storageString = I18n.format("gui.wirind.wirelessmachineschargerstorage") + ": ";
        String energyformatted = UtilFormatNumber.formatNumber(this.container.base.energy);
        String maxstorageformatted = UtilFormatNumber.formatNumber(this.container.base.getMaxChargerEnergy());

        String stringEnergyAll = storageString + energyformatted + " / " + maxstorageformatted + " EU";

        int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
        int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(stringEnergyAll)) / 2;

        this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 4210752);
        this.fontRendererObj.drawString(stringEnergyAll, nmPos2, 23, 4210752);

        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, StatCollector.translateToLocal("gui.wirind.tooltip.charger.switch.eu"), 63, 55, 80, 72);
        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, StatCollector.translateToLocal("gui.wirind.tooltip.charger.switch.rf"), 97, 55, 114, 72);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getResourceLocation());
        this.xoffset = (this.width - this.xSize) / 2;
        this.yoffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize);

        if(this.container.base.energy > 0) {
            int l = this.container.base.gaugeEnergyScaled(77);
            this.drawTexturedModalRect(this.xoffset + 49, this.yoffset + 41, 177, 15, l + 1, 10);
        }

        if(this.container.base.isChargingEU()) {
            this.drawTexturedModalRect(this.xoffset + 63, this.yoffset + 55, 177, 47, 18, 18);
        } else {
            this.drawTexturedModalRect(this.xoffset + 63, this.yoffset + 55, 177, 28, 18, 18);
        }

        if(this.container.base.isChargingRF()) {
            this.drawTexturedModalRect(this.xoffset + 97, this.yoffset + 55, 177, 47, 18, 18);
        } else {
            this.drawTexturedModalRect(this.xoffset + 97, this.yoffset + 55, 177, 28, 18, 18);
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

        if(x >= 63 && x <= 80 && y >= 55 && y <= 72) {
            IC2.network.get().initiateClientTileEntityEvent(this.container.base, 0);
        }

        if(x >= 97 && x <= 114 && y >= 55 && y <= 72) {
            IC2.network.get().initiateClientTileEntityEvent(this.container.base, 1);
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal(this.container.base.chargername);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(Reference.IDNAME, "textures/gui/GuiWirelessMachinesCharger.png");
    }
}
