package ru.wirelesstools.item;

import net.minecraft.item.Item;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

public class ItemGoldenWrench extends Item {

	public ItemGoldenWrench() {
		this.setCreativeTab(MainWI.tabwi);
		this.setUnlocalizedName("goldenWrench");
		this.setTextureName(Reference.PathTex + "itemGoldenWrench");
		this.setMaxStackSize(64);
	}
}
