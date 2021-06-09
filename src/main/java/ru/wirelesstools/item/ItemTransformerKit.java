package ru.wirelesstools.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.fluidmachines.TileExpGen;
import ru.wirelesstools.tiles.*;

import java.util.List;

public class ItemTransformerKit extends Item {

    private int kit_type;

    public ItemTransformerKit(int type) {
        this.setCreativeTab(MainWI.tabwi);
        this.kit_type = type;
        this.setUnlocalizedName(this.kit_type == 0 ? "transformkit" : "kitownerchange");
        this.setTextureName(Reference.PathTex + (this.kit_type == 0 ? "module_kit_upgrade" : "module_owner_change"));
        this.setMaxStackSize(1);
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean b) {
        switch (this.kit_type) {
            case 0:
                info.add(StatCollector.translateToLocal("info.transformmodule_0.about"));
                break;
            case 1:
                info.add(StatCollector.translateToLocal("info.transformmodule_1.about"));
                info.add(StatCollector.translateToLocal("info.transformmodule_1.creative"));
                break;
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {

        return this.kit_type == 0 ? EnumRarity.common : EnumRarity.epic;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            switch (this.kit_type) {
                case 0:
                    TileEntity te = world.getTileEntity(x, y, z);
                    if (te instanceof TileExpGen) {
                        double euxpgen = ((TileExpGen) te).energy;
                        world.setBlock(x, y, z, MainWI.blockxpsender, 0, 2);
                        TileEntity te2 = world.getTileEntity(x, y, z);
                        if (te2 instanceof TileXPSenderElectric) {
                            TileXPSenderElectric tee = (TileXPSenderElectric) te2;
                            tee.energy = euxpgen;
                            tee.markDirty();
                            stack.stackSize--;
                            if (stack.stackSize < 0)
                                stack = null;

                            return true;
                        }
                    }
                    break;
                case 1:
                    TileEntity telocal = world.getTileEntity(x, y, z);
                    if (telocal instanceof WirelessQuantumGeneratorBase) {
                        WirelessQuantumGeneratorBase teqgen = (WirelessQuantumGeneratorBase) telocal;
                        teqgen.setPlayerProfile(player.getGameProfile());
                        player.addChatMessage(new ChatComponentTranslation(
                                EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("chat.message.now.you.are.owner")));
                        return true;
                    }

                    if (telocal instanceof TileWirelessStorageBasePersonal) {
                        TileWirelessStorageBasePersonal tewsp = (TileWirelessStorageBasePersonal) telocal;
                        tewsp.setPlayerProfile(player.getGameProfile());
                        player.addChatMessage(new ChatComponentTranslation(
                                EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("chat.message.now.you.are.owner")));
                        return true;
                    }

                    if (telocal instanceof TileEntityWirelessChargerPrivate) {
                        TileEntityWirelessChargerPrivate tewchp = (TileEntityWirelessChargerPrivate) telocal;
                        tewchp.setPlayerProfile(player.getGameProfile());
                        player.addChatMessage(new ChatComponentTranslation(
                                EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("chat.message.now.you.are.owner")));
                        return true;
                    }

                    if (telocal instanceof TileWPBasePersonal) {
                        TileWPBasePersonal tewp = (TileWPBasePersonal) telocal;
                        tewp.setPlayerProfile(player.getGameProfile());
                        player.addChatMessage(new ChatComponentTranslation(
                                EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("chat.message.now.you.are.owner")));
                        return true;
                    }
                    break;
            }
        }
        return false;
    }
}
