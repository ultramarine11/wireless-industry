package ru.wirelesstools.slots;

import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotProcessable;
import net.minecraft.item.ItemStack;
import ru.wirelesstools.recipes.Recipes;
import ru.wirelesstools.tiles.PFPConvertorTile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InvSlotProcessablePFP extends InvSlotProcessable {

    public InvSlotProcessablePFP(TileEntityInventory base1, String name1, int oldStartIndex1, int count) {
        super(base1, name1, oldStartIndex1, count);
    }

    @Override
    public boolean accepts(ItemStack itemStack) {
        return Recipes.PFP_RecipeManager.getOutputFor(itemStack, false) != null;
    }

    @Override
    public RecipeOutput process() {
        ItemStack input = ((PFPConvertorTile)this.base).inputSlotA.get(0);
        if (input == null) {
            return null;
        } else {
            RecipeOutput output = Recipes.PFP_RecipeManager.getOutputFor(input, false);
            if (output == null) {
                return null;
            } else {
                List<ItemStack> itemsCopy = new ArrayList<>(output.items.size());
                Iterator var5 = output.items.iterator();

                while(var5.hasNext()) {
                    ItemStack itemStack = (ItemStack)var5.next();
                    itemsCopy.add(itemStack.copy());
                }

                return new RecipeOutput(output.metadata, itemsCopy);
            }
        }
    }

    @Override
    public void consume() {
        ItemStack input = ((PFPConvertorTile)this.base).inputSlotA.get(0);
        if (input == null) {
            throw new IllegalStateException("consume from empty slot");
        } else {
            RecipeOutput output = Recipes.PFP_RecipeManager.getOutputFor(input, true);
            if (output == null) {
                throw new IllegalStateException("consume without a processing result");
            } else {
                if (input.stackSize <= 0) {
                    this.put(0, null);
                }
            }
        }
    }
}
