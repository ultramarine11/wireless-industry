package ru.wirelesstools.nei;

import codechicken.lib.gui.GuiDraw;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.neiIntegration.core.recipehandler.MachineRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import ru.wirelesstools.Reference;
import ru.wirelesstools.gui.GuiPFPConvertor;
import ru.wirelesstools.recipes.Recipes;

import java.awt.*;
import java.util.Map;

public class NeiPFPConverter extends MachineRecipeHandler {

    public Class<? extends GuiContainer> getGuiClass() {
        return GuiPFPConvertor.class;
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("pfpconverter.name");
    }

    public void drawBackground(int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(-5, -16, 0, 0, 175, 166);  // 5 11
    }

    public void drawExtras(int i) {
        float f = this.ticks >= 20 ? (float)((this.ticks - 20) % 20) / 20.0F : 0.0F;
        this.drawProgressBar(77, 13, 177, 2, 22, 16, f, 0);
        f = this.ticks <= 20 ? (float)this.ticks / 20.0F : 1.0F;
        this.drawProgressBar(6, 30, 178, 26, 10, 30, f, 3);
    }

    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(77, 13, 25, 16), this.getRecipeId()));
    }

    public int recipiesPerPage() {
        return 1;
    }

    protected int getInputPosX() {
        return 49;
    }

    protected int getInputPosY() {
        return 12;
    }

    protected int getOutputPosX() {
        return 111;
    }

    protected int getOutputPosY() {
        return 12;
    }

    @Override
    public String getRecipeId() {
        return "pfpconverter.name";
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":textures/gui/gui_pfp1_nei.png";
    }

    @Override
    public String getOverlayIdentifier() {
        return "PFP_Converter";
    }

    @Override
    public Map<IRecipeInput, RecipeOutput> getRecipeList() {
        return Recipes.PFP_RecipeManager.getRecipes();
    }
}
