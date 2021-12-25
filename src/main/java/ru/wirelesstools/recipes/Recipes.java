package ru.wirelesstools.recipes;

import cpw.mods.fml.common.Loader;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.core.BasicMachineRecipeManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Recipes {

    public static IMachineRecipeManager PFP_RecipeManager;

    public static void initPFPRecipes() {
        Recipes.PFP_RecipeManager = new BasicMachineRecipeManager();
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.gold_ore)), null, new ItemStack(Blocks.gold_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.iron_ore)), null, new ItemStack(Blocks.iron_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.coal_ore)), null, new ItemStack(Blocks.coal_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.diamond_ore)), null, new ItemStack(Blocks.diamond_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.redstone_ore)), null, new ItemStack(Blocks.redstone_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.quartz_ore)), null, new ItemStack(Blocks.quartz_block));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputItemStack(new ItemStack(Blocks.emerald_ore)), null, new ItemStack(Blocks.emerald_block));

        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreCopper"), null, OreDictionary.getOres("blockCopper").get(0));
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreTin"), null, OreDictionary.getOres("blockTin").get(0));
        // there was silver recipe
        Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreLead"), null, OreDictionary.getOres("blockLead").get(0));

        if(Loader.isModLoaded("ThermalFoundation")) {
            Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreNickel"), null, OreDictionary.getOres("blockNickel").get(0));
            Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("orePlatinum"), null, OreDictionary.getOres("blockPlatinum").get(0));
            Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreMithril"), null, OreDictionary.getOres("blockMithril").get(0));
            Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreSilver"), null, OreDictionary.getOres("blockSilver").get(0));
        }

        if(Loader.isModLoaded("DraconicEvolution")) {
            Recipes.PFP_RecipeManager.addRecipe(new RecipeInputOreDict("oreDraconium"), null, OreDictionary.getOres("blockDraconium").get(0));
        }
    }

}
