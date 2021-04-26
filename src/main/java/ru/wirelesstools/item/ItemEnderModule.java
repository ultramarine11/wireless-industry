package ru.wirelesstools.item;

import net.minecraft.item.Item;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

public class ItemEnderModule extends Item {
	
	public ItemEnderModule() {
		this.setCreativeTab(MainWI.tabwi);
		this.setUnlocalizedName("endermodule");
		this.setTextureName(Reference.PathTex + "module_teleportation");
		this.setMaxStackSize(64);
	}
}
