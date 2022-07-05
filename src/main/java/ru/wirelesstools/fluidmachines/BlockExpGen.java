package ru.wirelesstools.fluidmachines;

import ic2.core.IC2;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

import java.util.Random;

public class BlockExpGen extends BlockContainer {
    
    private final IIcon[][] icons = new IIcon[1][12];
    
    public BlockExpGen(String unlocalizedName, Material mat) {
        super(mat);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.95F, 1.0F);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(MainWI.tabwi);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setLightLevel(0.5F);
    }
    
    public void registerBlockIcons(IIconRegister reg) {
        this.icons[0][0] = reg.registerIcon(Reference.PathTex + "xpsender_side_bottom");
        this.icons[0][1] = reg.registerIcon(Reference.PathTex + "expgen_top");
        this.icons[0][2] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        this.icons[0][3] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        this.icons[0][4] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        this.icons[0][5] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        
        this.icons[0][6] = reg.registerIcon(Reference.PathTex + "xpsender_side_bottom");
        this.icons[0][7] = reg.registerIcon(Reference.PathTex + "expgen_top_active");
        this.icons[0][8] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        this.icons[0][9] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        this.icons[0][10] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
        this.icons[0][11] = reg.registerIcon(Reference.PathTex + "blockXPgen_side_bottom");
    }
    
    public boolean isActive(IBlockAccess blockaccess, int x, int y, int z) {
        return ((TileExpGen)blockaccess.getTileEntity(x, y, z)).getActive();
    }
    
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if(!IC2.platform.isRendering()) {
            return;
        }
        TileExpGen te = (TileExpGen)world.getTileEntity(x, y, z);
        
        if(te == null) {
            return;
        }
        te.spawnParticles(world, x, y, z, random);
    }
    
    public IIcon getIcon(IBlockAccess blockaccess, int x, int y, int z, int blockSide) {
        if(this.isActive(blockaccess, x, y, z)) {
            return this.icons[0][blockSide + 6];
        }
        else
            return this.icons[0][blockSide];
    }
    
    public IIcon getIcon(int side, int meta) {
        return this.icons[0][side];
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileXPGenPublic();
    }
    
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        TileEntity te = world.getTileEntity(x, y, z);
        if(entity instanceof EntityPlayer && te instanceof TileExpGen) {
            ((TileExpGen)te).isPlayerStandingOnBlock((EntityPlayer)entity);
        }
    }
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
                                    float subY, float subZ) {
        if(!world.isRemote) {
            player.openGui(MainWI.instance, 1, world, x, y, z);
        }
        return true;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean isNormalCube(IBlockAccess world, int i, int j, int k) {
        return false;
    }
}
