package ru.wirelesstools.blocks;

import ic2.core.util.StackUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileXPTransmitter;

public class BlockXPTransmitter extends BlockContainer {
    
    private final IIcon[][] icons = new IIcon[1][12];
    
    public BlockXPTransmitter(String unlocalizedName, Material mat) {
        super(mat);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(MainWI.tabwi);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setLightLevel(0.5F);
    }
    
    public boolean isActive(IBlockAccess blockaccess, int x, int y, int z) {
        return ((TileXPTransmitter)blockaccess.getTileEntity(x, y, z)).getActive();
    }
    
    public IIcon getIcon(int side, int meta) {
        return this.icons[0][side];
    }
    
    public IIcon getIcon(IBlockAccess blockaccess, int x, int y, int z, int blockSide) {
        if(this.isActive(blockaccess, x, y, z)) {
            return this.icons[0][blockSide + 6];
        }
        else
            return this.icons[0][blockSide];
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
    
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack stack) {
        if(!world.isRemote) {
            TileXPTransmitter te = (TileXPTransmitter)world.getTileEntity(x, y, z);
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            te.setInternalParameters(nbt.getDouble("energy"), nbt.getInteger("storedXP"),
                    nbt.hasKey("amountXPTransmit") ? nbt.getInteger("amountXPTransmit") : 1,
                    !nbt.hasKey("sendingXPMode") || nbt.getBoolean("sendingXPMode"),
                    !nbt.hasKey("isOn") || nbt.getBoolean("isOn"));
        }
    }
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
                                    float subY, float subZ) {
        if(!world.isRemote) {
            player.openGui(MainWI.instance, 1, world, x, y, z);
        }
        return true;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileXPTransmitter();
    }
}
