package ru.wirelesstools.gui;

import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerXPTransmitter;

public class GuiXPTransmitter extends GuiIC2 {
    
    private final ContainerXPTransmitter cont;
    
    public GuiXPTransmitter(ContainerXPTransmitter container) {
        super(container, 176, 166);
        this.cont = container;
    }
    
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        this.fontRendererObj.drawString(this.getName(),
                (this.xSize - this.fontRendererObj.getStringWidth(this.getName())) / 2, 6, 4210752);
        String amountXPTransmit = I18n.format("gui.xp.amount.transmit") + ": ";
        String sendingMode = I18n.format("gui.xp.mode.send");
        String consumingMode = I18n.format("gui.xp.mode.consume");
        String progress = I18n.format("gui.xp.gen.progress") + ": ";
        
        String progressAll = progress + this.cont.base.getPercentageGeneration() + " %";
        
        this.fontRendererObj.drawString(amountXPTransmit + this.cont.base.getAmountXPTransmit() + " XP", 55, 41, 4210752);
        this.fontRendererObj.drawString(this.cont.base.getIsSendingMode() ? sendingMode : consumingMode,
                this.cont.base.getIsSendingMode() ? (this.xSize - this.fontRendererObj.getStringWidth(sendingMode)) / 2
                        : (this.xSize - this.fontRendererObj.getStringWidth(consumingMode)) / 2, 74, 4210752);
        this.fontRendererObj.drawString(progressAll, (this.xSize - this.fontRendererObj.getStringWidth(progressAll)) / 2, 32, 4210752);
        this.handleTransmitterTooltips(mouseX, mouseY);
    }
    
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        if(this.cont.base.getIsOn())
            this.drawTexturedModalRect(this.guiLeft + 95, this.guiTop + 52, 199, 23, 20, 20);
        else
            this.drawTexturedModalRect(this.guiLeft + 95, this.guiTop + 52, 177, 23, 20, 20);
        
        int xpGauge = (int)this.cont.base.gaugeExperience(48);
        if(this.cont.base.getStoredXP() > 0)
            this.drawTexturedModalRect(this.guiLeft + 63, this.guiTop + 22,
                    181, 59,
                    xpGauge + 1, 5);
    }
    
    private void handleTransmitterTooltips(int x, int y) {
        /*Set<String> namesSet = this.cont.base.getPlayerNamesSet();
        StringBuilder sb = new StringBuilder();
        if(namesSet.isEmpty())
            sb.append(I18n.format("gui.xp.no.players"));
        else {
            for(String playerName : namesSet) {
                sb.append(playerName).append("\n");
            }
        }*/
        //GuiTooltipHelper.drawAreaTooltip(x, y, sb.toString(), 46, 74, 130, 81);
        GuiTooltipHelper.drawAreaTooltip(x, y, StatCollector.translateToLocal("gui.xp.tooltip.players.count")
                + ": " + this.cont.base.getPlayersCount(), 46, 74, 130, 81);
        
        GuiTooltipHelper.drawAreaTooltip(x, y, "+1", 9, 42, 19, 52);
        GuiTooltipHelper.drawAreaTooltip(x, y, "-1", 9, 61, 19, 71);
        GuiTooltipHelper.drawAreaTooltip(x, y, StatCollector.translateToLocal("gui.xp.tooltip.on_off"), 95, 52, 114, 71);
        GuiTooltipHelper.drawAreaTooltip(x, y, StatCollector.translateToLocal("gui.xp.tooltip.toggle.transmission"), 130, 52, 149, 71);
        GuiTooltipHelper.drawAreaTooltip(x, y, "+10", 22, 42, 32, 52);
        GuiTooltipHelper.drawAreaTooltip(x, y, "-10", 22, 61, 32, 71);
        GuiTooltipHelper.drawAreaTooltip(x, y, "+100", 35, 42, 45, 52);
        GuiTooltipHelper.drawAreaTooltip(x, y, "-100", 35, 61, 45, 71);
        
        GuiTooltipHelper.drawAreaTooltip(x, y, this.cont.base.getStoredXP() + " / " + this.cont.base.getXpLimit() + " XP", 60, 19, 113, 29);
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        if(this.checkMouseCoords(mouseX, mouseY, 9, 42, 19, 52))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 1);
        
        if(this.checkMouseCoords(mouseX, mouseY, 9, 61, 19, 71))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 2);
        
        if(this.checkMouseCoords(mouseX, mouseY, 95, 52, 114, 71))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 3);
        
        if(this.checkMouseCoords(mouseX, mouseY, 130, 52, 149, 71))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 4);
        
        if(this.checkMouseCoords(mouseX, mouseY, 22, 42, 32, 52))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 5);
        
        if(this.checkMouseCoords(mouseX, mouseY, 22, 61, 32, 71))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 6);
        
        if(this.checkMouseCoords(mouseX, mouseY, 35, 42, 45, 52))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 7);
        
        if(this.checkMouseCoords(mouseX, mouseY, 35, 61, 45, 71))
            IC2.network.get().initiateClientTileEntityEvent(this.cont.base, 8);
    }
    
    private boolean checkMouseCoords(int x, int y, int xMin, int yMin, int xMax, int yMax) {
        return (x >= xMin && x <= xMax && y >= yMin && y <= yMax);
    }
    
    @Override
    public String getName() {
        return StatCollector.translateToLocal("xptransmitter.gui.name");
    }
    
    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/guiexptransmitter.png");
    }
}
