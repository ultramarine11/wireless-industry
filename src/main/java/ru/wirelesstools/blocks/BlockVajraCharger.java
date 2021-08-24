package ru.wirelesstools.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileVajraCharger;

public class BlockVajraCharger extends Block implements ITileEntityProvider {

    public BlockVajraCharger(String unlocalizedName, Material mat) {
        super(mat);
        this.setBlockName(unlocalizedName);
        setBlockTextureName(Reference.PathTex + "blockVajraCharger");
        setCreativeTab(MainWI.tabwi);
        setHardness(3.0F);
        setResistance(5.0F);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileVajraCharger();

    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
                                    float subY, float subZ) {
        if(!world.isRemote)
            player.openGui(MainWI.instance, 1, world, x, y, z);

        return true;

    }

}
