package ru.wirelesstools.gui;

import ic2.core.util.DrawUtil;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.container.ContainerExpGen;
import ru.wirelesstools.fluidmachines.TileExpGen;

public class GuiExpGen extends GuiContainer {

    private static final ResourceLocation tex = new ResourceLocation(Reference.MOD_ID, "textures/gui/GUIExpGen.png");

    public TileExpGen tileentity;

    public GuiExpGen(InventoryPlayer inventoryplayer, TileExpGen te) {
        super(new ContainerExpGen(inventoryplayer, te));
        this.tileentity = te;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String formatXPName = I18n.format(this.tileentity.nametile);
        int nmPos = (this.xSize - this.fontRendererObj.getStringWidth(formatXPName)) / 2;
        this.fontRendererObj.drawString(formatXPName, nmPos, 8, 4210752);
        FluidStack fsamount = this.tileentity.getFluidTank().getFluid();
        int xpamount;
        if(fsamount != null)
            xpamount = fsamount.amount;
        else
            xpamount = 0;

        this.fontRendererObj.drawString(
                I18n.format("tile.xpgen.energy") + ": " + (int) this.tileentity.energy + " Eu", 6, 40,
                4210752);
        this.fontRendererObj.drawString(I18n.format("xpgen.amount.xp") + ": " + xpamount + " mB", 6,
                30, 4210752);
        if(fsamount != null) {
            String tooltip = I18n.format(fsamount.getFluid().getName()) + ": " + fsamount.amount + " mB";
            GuiTooltipHelper.drawAreaTooltip(par1 - this.guiLeft, par2 - this.guiTop, tooltip, 99, 25, 112, 73);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(tex);
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);

        if(this.tileentity.getFluidTank().getFluidAmount() > 0) {
            IIcon fluidIcon = this.tileentity.getFluidTank().getFluid().getFluid().getIcon();
            if(fluidIcon != null) {
                this.drawTexturedModalRect(h + 96, k + 22, 176, 0, 20, 55);
                this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                int liquidHeight = this.tileentity.gaugeLiquidScaled(47);
                DrawUtil.drawRepeated(fluidIcon, h + 100, (k + 26 + 47 - liquidHeight), 12.0D, liquidHeight,
                        this.zLevel);
                this.mc.renderEngine.bindTexture(tex);
                this.drawTexturedModalRect(h + 100, k + 26, 176, 55, 12, 47);
            }

        }
    }
}
