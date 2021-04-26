package ru.wirelesstools.item;

import net.minecraft.item.Item;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

public class ItemWirelessModule extends Item {

	public ItemWirelessModule() {
		this.setCreativeTab(MainWI.tabwi);
		this.setUnlocalizedName("WirelessModule");
		this.setTextureName(Reference.PathTex + "wirelessmodule");
		this.setMaxStackSize(64);
	}
}
