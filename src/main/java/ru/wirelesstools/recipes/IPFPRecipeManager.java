package ru.wirelesstools.recipes;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.Map;

public interface IPFPRecipeManager {

    void addRecipe(IRecipeInput var1, ItemStack var3);

    RecipeOutput getOutputFor(ItemStack var1, boolean var2);

    Map<IRecipeInput, RecipeOutput> getRecipes();

    public static class Input {
        public final IRecipeInput container;
        public final IRecipeInput fill;

        public Input(IRecipeInput container1, IRecipeInput fill1) {
            this.container = container1;
            this.fill = fill1;
        }

        public boolean matches(ItemStack container1, ItemStack fill1) {
            return this.container.matches(container1) && this.fill.matches(fill1);
        }
    }

}
