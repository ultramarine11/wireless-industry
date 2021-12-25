package ru.wirelesstools.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileWirelessMachinesCharger;

public class BlockMachinesCharger extends BlockContainer {

    private IIcon[] icons;

    public BlockMachinesCharger(String unlocalizedName) {
        super(Material.rock);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(MainWI.tabwi);
        this.setBlockTextureName(Reference.PathTex + "blockLampCharger2");
        this.setHardness(3.0F);
        this.setResistance(5.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {

        return new TileWirelessMachinesCharger();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
                                    float subY, float subZ) {
        if(!world.isRemote)
            player.openGui(MainWI.instance, 1, world, x, y, z);

        return true;

    }

}
