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
import ru.wirelesstools.tiles.TileWirelessStoragePersonal1;

public class BlockWStoragePersonal extends BlockContainer {

    public BlockWStoragePersonal(String name, Material mat) {
        super(mat);
        this.setBlockName(name);
        this.setCreativeTab(MainWI.tabwi);
        this.setBlockTextureName(Reference.PathTex + "wreceiverpersonal");
        this.setHardness(3.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileWirelessStoragePersonal1();
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileWirelessStoragePersonal1 tile = (TileWirelessStoragePersonal1)world.getTileEntity(x, y, z);
        if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            tile.setPlayerProfile(player.getGameProfile());
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
                                    float subY, float subZ) {
        if(!world.isRemote) {
            TileWirelessStoragePersonal1 te = (TileWirelessStoragePersonal1)world.getTileEntity(x, y, z);
            boolean access = te.permitsAccess(player.getGameProfile());

            if(access || player.capabilities.isCreativeMode) {

                player.openGui(MainWI.instance, 1, world, x, y, z);

            }
            else {
                player.addChatMessage(new ChatComponentTranslation("access.wsbp.not.allowed"));
            }

        }
        return true;
    }

}
