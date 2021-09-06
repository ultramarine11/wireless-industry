package ru.wirelesstools.gui;

import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.util.DrawUtil;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerLiquidMatterCollector;
import ru.wirelesstools.utils.HelperUtils;

public class GuiLiquidMatterCollector extends GuiIC2  {

    private ContainerLiquidMatterCollector container;

    public GuiLiquidMatterCollector(ContainerLiquidMatterCollector container) {
        super(container, 175, 166);
        this.container = container;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String tileentityname = I18n.format(this.container.base.mattercollectorname);
        int nmPos1 = (this.xSize - this.fontRendererObj.getStringWidth(tileentityname)) / 2;
        this.fontRendererObj.drawString(tileentityname, nmPos1, 5, 4210752);
        FluidStack fluidstack = this.container.base.getFluidTank().getFluid();
        if (fluidstack != null) {
            String tooltip = StatCollector.translateToLocal("ic2.uumatter")
                    + ": " + fluidstack.amount + StatCollector.translateToLocal("ic2.generic.text.mb");
            GuiTooltipHelper.drawAreaTooltip(par1 - this.guiLeft, par2 - this.guiTop, tooltip, 99, 25, 112, 73);
        }

        String percent = this.container.base.getIntegerPercentage() + "%";
        int nmPos2 = (this.xSize - this.fontRendererObj.getStringWidth(percent)) / 2;
        this.fontRendererObj.drawString(percent, nmPos2 - 35, 46, 4210752);
        String isActive = this.container.base.getIsActive() ?
                StatCollector.translateToLocal("info.button.iscollectoron") : StatCollector.translateToLocal("info.button.iscollectoroff");
        int nmPos3 = (this.xSize - this.fontRendererObj.getStringWidth(isActive)) / 2;
        this.fontRendererObj.drawString(isActive, nmPos3 + 60, 57, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getResourceLocation());
        this.xoffset = (this.width - this.xSize) / 2;
        this.yoffset = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.xoffset, this.yoffset, 0, 0, this.xSize, this.ySize);
        if(this.container.base.getFluidTank().getFluidAmount() > 0) {
            IIcon fluidIcon = this.container.base.getFluidTank().getFluid().getFluid().getIcon();
            if (fluidIcon != null) {
                this.drawTexturedModalRect(this.xoffset + 96, this.yoffset + 22, 176, 0, 20, 55);
                this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                int liquidHeight = this.container.base.gaugeLiquidScaled(47);
                DrawUtil.drawRepeated(fluidIcon, this.xoffset + 100, this.yoffset + 26 + 47 - liquidHeight,
                        12.0D, liquidHeight, this.zLevel);
                this.mc.renderEngine.bindTexture(this.getResourceLocation());
                this.drawTexturedModalRect(this.xoffset + 100, this.yoffset + 26, 176, 55, 12, 47);
            }
        }

        HelperUtils.renderPercentageCircle(this, this.container.base.getPercentagePart(32), this.xoffset, this.yoffset);
    }

    public void initGui() {
        super.initGui();
        int xGuiPos = (this.width - this.xSize) / 2;
        int yGuiPos = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButton(1, xGuiPos + 123, yGuiPos + 34, 48, 20,
                I18n.format("button.tooltip.toggle")));
    }

    protected void actionPerformed(GuiButton guibutton) {
        super.actionPerformed(guibutton);
        if (guibutton.id == 1) {
            IC2.network.get().initiateClientTileEntityEvent(this.container.base, 1);
        }
    }

    @Override
    public String getName() {
        return StatCollector.translateToLocal(this.container.base.mattercollectorname);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(Reference.IDNAME, "textures/gui/GUILiquidMatterCollector.png");
    }
}
