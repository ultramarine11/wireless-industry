package ru.wirelesstools.recipes;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PFPRecipeManager implements IPFPRecipeManager {

    private final Map<IRecipeInput, RecipeOutput> recipes = new HashMap<>();

    public PFPRecipeManager() {}

    /*@Override
    public void addRecipe(IRecipeInput container, ItemStack output) {
        if (container == null) {
            throw new NullPointerException("The container recipe input is null");
        } else if (fill == null) {
            throw new NullPointerException("The fill recipe input is null");
        } else if (output == null) {
            throw new NullPointerException("The recipe output is null");
        } else if (!StackUtil.check(output)) {
            throw new IllegalArgumentException("The recipe output " + StackUtil.toStringSafe(output) + " is invalid");
        } else {
            Iterator var4 = this.recipes.keySet().iterator();

            while(var4.hasNext()) {
                IPFPRecipeManager.Input input = (IPFPRecipeManager.Input)var4.next();
                Iterator var6 = container.getInputs().iterator();

                while(var6.hasNext()) {
                    ItemStack containerStack = (ItemStack)var6.next();
                    Iterator var8 = fill.getInputs().iterator();

                    while(var8.hasNext()) {
                        ItemStack fillStack = (ItemStack)var8.next();
                        if (input.matches(containerStack, fillStack)) {
                            throw new RuntimeException("ambiguous recipe: [" + container.getInputs() + "+" + fill.getInputs() + " -> " + output
                                    + "], conflicts with [" + input.container.getInputs() + "+" + input.fill.getInputs() + " -> "
                                    + this.recipes.get(input) + "]");
                        }
                    }
                }
            }

            this.recipes.put(new IPFPRecipeManager.Input(container, fill), new RecipeOutput(null, output));
        }
    }*/

    /*@Override
    public RecipeOutput getOutputFor(ItemStack container, boolean acceptTest) {
        if (acceptTest) {
            if (container == null && fill == null) {
                return null;
            }
        } else if (container == null || fill == null) {
            return null;
        }

        Iterator<Map.Entry<IRecipeInput, RecipeOutput>> var5 = this.recipes.entrySet().iterator();

        while(var5.hasNext()) {
            Map.Entry<IRecipeInput, RecipeOutput> entry = var5.next();
            IRecipeInput recipeInput = entry.getKey();
            if (acceptTest && container == null) {
                if (recipeInput.fill.matches(fill)) {
                    return entry.getValue();
                }
            } else if (acceptTest && fill == null) {
                if (recipeInput.container.matches(container)) {
                    return entry.getValue();
                }
            } else if (recipeInput.matches(container, fill)) {
                if (!acceptTest && (container.stackSize < recipeInput.container.getAmount() || fill.stackSize < recipeInput.fill.getAmount())) {
                    break;
                }

                if (adjustInput) {
                    container.stackSize -= recipeInput.container.getAmount();
                    fill.stackSize -= recipeInput.fill.getAmount();
                }

                return entry.getValue();
            }
        }

        return null;
    }*/

    @Override
    public void addRecipe(IRecipeInput var1, ItemStack var3) {

    }

    @Override
    public RecipeOutput getOutputFor(ItemStack var1, boolean var2) {
        return null;
    }

    @Override
    public Map<IRecipeInput, RecipeOutput> getRecipes() {
        return this.recipes;
    }
}
