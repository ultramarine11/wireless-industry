package ru.wirelesstools.item.tool;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.block.machine.tileentity.TileEntitySteamGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

import java.util.List;

public class ElectricDescaler extends Item implements IElectricItem {

    public double maxCharge;
    protected final int tier;
    protected final double transferLimit;

    public ElectricDescaler() {
        this.setUnlocalizedName("electricdescaler");
        this.setTextureName(Reference.PathTex + "itemelectricdescaler");
        this.setCreativeTab(MainWI.tabwi);
        this.setMaxStackSize(1);
        this.setMaxDamage(27);
        this.setNoRepair();
        this.maxCharge = 20000.0;
        this.tier = 2;
        this.transferLimit = 200.0;
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add(StatCollector.translateToLocal("info.descaler.howto.use"));
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
        ItemStack charged = new ItemStack(this);
        ItemStack discharged = new ItemStack(this);
        ElectricItem.manager.charge(charged, 2147483647, Integer.MAX_VALUE, true, false);
        ElectricItem.manager.charge(discharged, 0.0, Integer.MAX_VALUE, true, false);
        itemList.add(charged);
        itemList.add(discharged);
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int i, int j, int k,
                                  int side, float a, float b, float c) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(i, j, k);
            if (te instanceof TileEntitySteamGenerator) {
                TileEntitySteamGenerator stgente = (TileEntitySteamGenerator) te;
                try {
                    if (ElectricItem.manager.canUse(stack, 1000.0)) {
                        int calcification = ReflectionHelper.getPrivateValue(TileEntitySteamGenerator.class, stgente, "calcification");
                        if (calcification > 0) {
                            ReflectionHelper.setPrivateValue(TileEntitySteamGenerator.class, stgente, 0, "calcification");
                            ElectricItem.manager.use(stack, 1000.0, player);
                            player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("chat.message.scale.cleared")
                                    + " " + EnumChatFormatting.GREEN + String.format("%.2f", (double) calcification / 1000.0) + "%"
                                    + " " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("chat.message.of.calcification")));
                        }
                    }
                } catch (Exception e) {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED
                            + StatCollector.translateToLocal("chat.message.descaler.error")));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return this.transferLimit;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
