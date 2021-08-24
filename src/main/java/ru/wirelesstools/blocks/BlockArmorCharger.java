package ru.wirelesstools.blocks;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.utils.MiscUtils;

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
        if(!world.isRemote) {
            for(int i = player.inventory.armorInventory.length - 1; i >= 0; i--) {
                ItemStack armorcharged = player.inventory.armorInventory[i];
                if(armorcharged != null && armorcharged.getItem() instanceof IElectricItem) {
                    ElectricItem.manager.charge(armorcharged, Double.POSITIVE_INFINITY, 2147483647, true, false);
                    player.inventoryContainer.detectAndSendChanges();
                    switch(i) {
                        case 0:
                            MiscUtils.sendColoredMessageToPlayer(player, "boots.successfully.charged", EnumChatFormatting.AQUA);
                            break;
                        case 1:
                            MiscUtils.sendColoredMessageToPlayer(player, "leggings.successfully.charged", EnumChatFormatting.GREEN);
                            break;
                        case 2:
                            MiscUtils.sendColoredMessageToPlayer(player, "armorchest.successfully.charged", EnumChatFormatting.RED);
                            break;
                        case 3:
                            MiscUtils.sendColoredMessageToPlayer(player, "helmet.successfully.charged", EnumChatFormatting.YELLOW);
                            break;
                    }
                }
            }

            /*for(int i = 0; i < player.inventory.armorInventory.length; i++) {
                ItemStack armorcharged = player.inventory.armorInventory[i];
                if(armorcharged != null && armorcharged.getItem() instanceof IElectricItem) {
                    ElectricItem.manager.charge(armorcharged, Double.POSITIVE_INFINITY, 2147483647, true, false);
                    player.inventoryContainer.detectAndSendChanges();
                    switch(i) {
                        case 0:
                            MiscUtils.sendColoredMessageToPlayer(player, "boots.successfully.charged", EnumChatFormatting.AQUA);
                            break;
                        case 1:
                            MiscUtils.sendColoredMessageToPlayer(player, "leggings.successfully.charged", EnumChatFormatting.GREEN);
                            break;
                        case 2:
                            MiscUtils.sendColoredMessageToPlayer(player, "armorchest.successfully.charged", EnumChatFormatting.RED);
                            break;
                        case 3:
                            MiscUtils.sendColoredMessageToPlayer(player, "helmet.successfully.charged", EnumChatFormatting.YELLOW);
                            break;
                    }
                }
            }*/
        }

        return true;

    }

}
