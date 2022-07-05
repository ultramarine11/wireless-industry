package ru.wirelesstools.gui;

import com.mojang.authlib.GameProfile;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerWirelessQuantumGen;

public class GuiWirelessQuantumGen extends GuiIC2 {
    
    private final ContainerWirelessQuantumGen cont;
    private final int buttonYPos = 42;
    private final int buttonWidth = 66;
    private final int buttonHeigth = 20;
    
    public GuiWirelessQuantumGen(ContainerWirelessQuantumGen container) {
        super(container, 176, 188);
        this.cont = container;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.fontRendererObj.drawString(this.getName(),
                (this.xSize - this.fontRendererObj.getStringWidth(this.getName())) / 2, 6, 16777215);
        
        String tileIsCharging = I18n.format("gui.wirind.wirelessqgen.ischarging");
        String tileIsNotCharging = I18n.format("gui.wirind.wirelessqgen.isnotcharging");
        String tileIsWaiting = I18n.format("gui.wirind.wirelessqgen.iswaiting");
        GameProfile owner = this.cont.base.getOwner();
        String tileOwner = owner != null ? I18n.format("gui.wirind.wirelessqgen.owner")
                + ": " + owner.getName() : I18n.format("gui.wi.noowner");
        
        String machinesCount = I18n.format("gui.wirind.wirelessqgen.machinesinchunk") + ": " + this.cont.base.getMachinesCountInChunk();
        String chargeRate = this.cont.base.getChargeRateByMode(this.cont.base.getModeTransmitting()) + " Eu/t";
        
        int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(tileIsCharging)) / 2;
        int nmPos3 = (this.xSize - this.fontRendererObj.getStringWidth(tileIsNotCharging)) / 2;
        int nmPos4 = (this.xSize - this.fontRendererObj.getStringWidth(tileIsWaiting)) / 2;
        int nmPos5 = (this.xSize - this.fontRendererObj.getStringWidth(tileOwner)) / 2;
        int nmPos8 = (this.xSize - this.fontRendererObj.getStringWidth(machinesCount)) / 2;
        int nmPos9 = (this.xSize - this.fontRendererObj.getStringWidth(chargeRate)) / 2;
        
        switch(this.cont.base.getStatus()) {
            case CHARGING:
                this.fontRendererObj.drawString(tileIsCharging, nmPos2, 18, 5635925); // green color code 5635925
                break;
            case NOT_CHARGING:
                this.fontRendererObj.drawString(tileIsNotCharging, nmPos3, 18, 16733525); // red color code 16733525
                break;
            case WAITING:
                this.fontRendererObj.drawString(tileIsWaiting, nmPos4, 18, 16755200); // gold color code 16755200
                break;
        }
        this.fontRendererObj.drawString(tileOwner, nmPos5, 65, 16777215);
        
        this.fontRendererObj.drawString(machinesCount, nmPos8, 30, 16777215);
        this.fontRendererObj.drawString(chargeRate, nmPos9, this.buttonYPos + (this.buttonHeigth - this.fontRendererObj.FONT_HEIGHT) / 2, 16777215);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getResourceLocation());
        this.drawTexturedModalRect(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize);*/
    }
    
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0,
                this.guiLeft + (this.xSize - this.buttonWidth) / 2,
                this.guiTop + this.buttonYPos,
                this.buttonWidth, this.buttonHeigth, ""));
    }
    
    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);
        IC2.network.get().initiateClientTileEntityEvent(this.cont.base, guibutton.id);
    }
    
    @Override
    public String getName() {
        return StatCollector.translateToLocal(this.cont.base.wirelessQGenName);
    }
    
    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/GUIQuantumGenerator.png");
    }
}
