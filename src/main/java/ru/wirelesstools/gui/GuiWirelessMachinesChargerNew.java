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
        String autoMode = I18n.format("gui.wmcharger.mode.automatic");
        String euAll = UtilFormatNumber.formatNumber(this.container.base.energyEU) + " / "
                + UtilFormatNumber.formatNumber(this.container.base.getMaxChargerEUEnergy()) + " EU";
        String rfAll = UtilFormatNumber.formatNumber(this.container.base.energyRF) + " / "
                + UtilFormatNumber.formatNumber(this.container.base.getMaxChargerRFEnergy()) + " RF";

        this.fontRendererObj.drawString(tileentityname,
                (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2, 5, 4210752);

        if(this.container.base.getMode() == 8) {
            this.fontRendererObj.drawString(autoMode,
                    (this.xSize - this.fontRendererObj.getStringWidth(autoMode)) / 2, 22, 4210752);
        }
        else {
            String rate = String.format("%d", (int)this.container.base.getChargeRate()) + " Eu/t";
            this.fontRendererObj.drawString(rate,
                    (this.xSize - this.fontRendererObj.getStringWidth(rate)) / 2, 22, 4210752);
        }

        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, euAll, 18, 28, 29, 68);
        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, rfAll, 144, 28, 155, 68);

        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, StatCollector.translateToLocal("gui.wirind.tooltip.charger.switch.eu"), 63, 55, 80, 73);
        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, StatCollector.translateToLocal("gui.wirind.tooltip.charger.switch.rf"), 96, 55, 113, 73);

        GuiTooltipHelper.drawAreaTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, StatCollector.translateToLocal("gui.wirind.tooltip.charger.change.rate"), 60, 19, 113, 33);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getResourceLocation());
        this.xoffset = (this.width - this.xSize) / 2;
        this.yoffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize);

        if(this.container.base.energyEU > 0) {
            int l = this.container.base.gaugeEUScaled(38);
            this.drawTexturedModalRect(this.xoffset + 19, this.yoffset + 66 - l + 1, 200, 66 - l + 1, 10, l);
        }
        if(this.container.base.energyRF > 0) {
            int l = this.container.base.gaugeRFScaled(38);
            this.drawTexturedModalRect(this.xoffset + 145, this.yoffset + 66 - l + 1, 215, 66 - l + 1, 10, l);
        }

        if(this.container.base.isChargingEU()) {
            this.drawTexturedModalRect(this.xoffset + 63, this.yoffset + 55, 177, 47, 18, 18);
        } else {
            this.drawTexturedModalRect(this.xoffset + 63, this.yoffset + 55, 177, 28, 18, 18);
        }

        if(this.container.base.isChargingRF()) {
            this.drawTexturedModalRect(this.xoffset + 96, this.yoffset + 55, 177, 47, 18, 18);
        } else {
            this.drawTexturedModalRect(this.xoffset + 96, this.yoffset + 55, 177, 28, 18, 18);
        }
    }

    protected void mouseClicked(int i, int j, int button) {
        super.mouseClicked(i, j, button); // LMB (button = 0), RMB (button = 1)
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

        if(x >= 63 && x <= 80 && y >= 55 && y <= 73) {
            IC2.network.get().initiateClientTileEntityEvent(this.container.base, 0);
        }

        if(x >= 96 && x <= 113 && y >= 55 && y <= 73) {
            IC2.network.get().initiateClientTileEntityEvent(this.container.base, 1);
        }

        if(x >= 60 && x <= 113 && y >= 19 && y <= 33) {
            switch(button) {
                case 0:
                    IC2.network.get().initiateClientTileEntityEvent(this.container.base, 2);
                    break;
                case 1:
                    IC2.network.get().initiateClientTileEntityEvent(this.container.base, 3);
                    break;
            }
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal(this.container.base.chargername);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/GuiWirelessMachinesCharger3.png");
    }
}
