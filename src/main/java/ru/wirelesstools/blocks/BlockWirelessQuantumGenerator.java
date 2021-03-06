package ru.wirelesstools.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.WirelessQGenerator;

public class BlockWirelessQuantumGenerator extends BlockContainer {

	public BlockWirelessQuantumGenerator(String name) {
		super(Material.rock);
		this.setBlockName(name);
		this.setCreativeTab(MainWI.tabwi);
		this.setBlockTextureName(Reference.PathTex + "qgen_wireless_active_side");
		this.setHardness(3.0F);
		this.setResistance(5.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new WirelessQGenerator();
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		WirelessQGenerator tile = (WirelessQGenerator) world.getTileEntity(x, y, z);
		if (entity instanceof EntityPlayer) {
			tile.setPlayerProfile(((EntityPlayer) entity).getGameProfile());
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
			float subY, float subZ) {
		if (!world.isRemote) {
			WirelessQGenerator tile = (WirelessQGenerator) world.getTileEntity(x, y, z);
			if (tile.permitsAccess(player.getGameProfile()) || player.capabilities.isCreativeMode) {

				player.openGui(MainWI.instance, 1, world, x, y, z);
			} else {

				player.addChatMessage(new ChatComponentTranslation("access.qgen.notallowed"));
			}

		}

		return true;
	}

}
