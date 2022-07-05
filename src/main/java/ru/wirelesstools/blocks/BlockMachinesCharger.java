package ru.wirelesstools.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileWirelessMachinesCharger;

public class BlockMachinesCharger extends BlockContainer {

    private final IIcon[] icons = new IIcon[2];

    public BlockMachinesCharger(String unlocalizedName) {
        super(Material.rock);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(MainWI.tabwi);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
    }

    public void registerBlockIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(Reference.PathTex + "blockenergydispatcher_top"); // bottom + top
        this.icons[1] = reg.registerIcon(Reference.PathTex + "blockenergydispatcher"); // sides
    }

    public IIcon getIcon(int side, int metadata) {
        // 0=bottom, 1=top, 2,3,4,5 = sides
        switch(side) {
            case 0:
            case 1:
                return this.icons[0];
            case 2:
            case 3:
            case 4:
            case 5:
                return this.icons[1];
        }
        return this.icons[1];
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
