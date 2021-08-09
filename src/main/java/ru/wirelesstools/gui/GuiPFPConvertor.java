package ru.wirelesstools.gui;

import ic2.core.GuiIC2;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerPFPConverter;

public class GuiPFPConvertor extends GuiIC2 {

    private ContainerPFPConverter container;

    public GuiPFPConvertor(ContainerPFPConverter container) {
        super(container, 175, 166);
        this.container = container;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String tileentityname = I18n.format(this.container.base.pfpconvertorname);
        int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
        this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 16766720); // цвет Gold #FFD700

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

        if (this.container.base.energyEU > 0) {
            int l = this.container.base.gaugeEnergyScaled(30);
            this.drawTexturedModalRect(this.xoffset + 11, this.yoffset + 70 - l + 1, 178, 55 - l + 1, 10, l);
        }

        int progress = (int)(22.0D * this.container.base.gaugeProgressScaled());
        if (progress > 0) {
            this.drawTexturedModalRect(this.xoffset + 82, this.yoffset + 24,
                    177, 2, progress + 1, 16);
        }

        if(this.container.base.getIsHot()) {
            this.drawTexturedModalRect(this.xoffset + 27, this.yoffset + 64, 177, 57, 6, 8);
        }

    }

    @Override
    public String getName() {

        return StatCollector.translateToLocal(this.container.base.pfpconvertorname);
    }

    @Override
    public ResourceLocation getResourceLocation() {

        return new ResourceLocation(Reference.IDNAME, "textures/gui/gui_pfp1_1.png");
    }
}
