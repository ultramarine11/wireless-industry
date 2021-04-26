package ru.wirelesstools.blocks;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

public class BlockArmorCharger extends Block implements ITileEntityProvider {

	public BlockArmorCharger(String unlocalizedName, Material mat) {
		super(mat);
		this.setBlockName(unlocalizedName);
		this.setCreativeTab(MainWI.tabwi);
		this.setBlockTextureName(Reference.PathTex + "blockArmorCharger");
		this.setHardness(2.0F);
		this.setResistance(5.0F);
	}

	public TileEntity createNewTileEntity(World world, int meta) {

		return null;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
			float subY, float subZ) {
		if (!world.isRemote) {
			for (ItemStack armorcharged : player.inventory.armorInventory) {
				if (armorcharged != null) {
					if (armorcharged.getItem() instanceof IElectricItem) {
						ElectricItem.manager.charge(armorcharged, Double.POSITIVE_INFINITY, 2147483647, true, false);
						player.inventoryContainer.detectAndSendChanges();

						if (armorcharged == player.inventory.armorInventory[0]) {

							IC2.platform.messagePlayer(player,
									EnumChatFormatting.AQUA
											+ StatCollector.translateToLocal("boots.successfully.charged"),
									new Object[0]);
						} else if (armorcharged == player.inventory.armorInventory[1]) {

							IC2.platform.messagePlayer(player,
									EnumChatFormatting.GREEN
											+ StatCollector.translateToLocal("leggings.successfully.charged"),
									new Object[0]);
						} else if (armorcharged == player.inventory.armorInventory[2]) {

							IC2.platform.messagePlayer(player,
									EnumChatFormatting.RED
											+ StatCollector.translateToLocal("armorchest.successfully.charged"),
									new Object[0]);
						} else if (armorcharged == player.inventory.armorInventory[3]) {

							IC2.platform.messagePlayer(player,
									EnumChatFormatting.YELLOW
											+ StatCollector.translateToLocal("helmet.successfully.charged"),
									new Object[0]);
						}
					}
				}
			}
		}

		return true;

	}

}
