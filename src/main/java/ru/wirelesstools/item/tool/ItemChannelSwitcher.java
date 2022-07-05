package ru.wirelesstools.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.tiles.TileWPBasePersonal;
import ru.wirelesstools.tiles.TileWirelessStorageBasePersonal;
import ru.wirelesstools.utils.HelperUtils;

import java.util.List;

public class ItemChannelSwitcher extends Item {

    private final IIcon[] switcherIcons = new IIcon[2];

    public ItemChannelSwitcher() {
        this.setUnlocalizedName("wi.channelswitcher");
        this.setCreativeTab(MainWI.tabwi);
        this.setMaxStackSize(1);
        this.setMaxDamage(27);
        this.setNoRepair();
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.switcherIcons[0] = reg.registerIcon(Reference.PathTex + "channel_switcher_read");
        this.switcherIcons[1] = reg.registerIcon(Reference.PathTex + "channel_switcher_write");
    }

    @SideOnly(value = Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(value = Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int n) {
        switch(StackUtil.getOrCreateNbtData(stack).getShort("mode")) {
            case 1: // write channel
                return this.switcherIcons[1];
            case 0: // read channel
            default:
                return this.switcherIcons[0];
        }
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        if(org.lwjgl.input.Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            list.add(StatCollector.translateToLocal("info.wi.channelswitcher.change.mode"));
            switch(nbt.getShort("mode")) {
                case 0:
                    list.add(EnumChatFormatting.AQUA
                            + StatCollector.translateToLocal("info.wi.channelswitcher.reading.mode"));
                    break;
                case 1:
                    list.add(EnumChatFormatting.LIGHT_PURPLE
                            + StatCollector.translateToLocal("info.wi.channelswitcher.writing.mode"));
                    break;
            }

            if(nbt.hasKey("channel", 3)) {
                list.add(EnumChatFormatting.GREEN
                        + StatCollector.translateToLocal("info.wi.contains.channel") + ": "
                        + nbt.getInteger("channel"));
            }
            else
                list.add(EnumChatFormatting.RED
                        + StatCollector.translateToLocal("info.wi.switcher.empty"));
        }
        else
            list.add(EnumChatFormatting.ITALIC + I18n.format("press.lshift"));
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            short mode = nbt.getShort("mode");
            if(++mode > 1)
                mode = 0;
            nbt.setShort("mode", mode);
            switch(mode) {
                case 0:
                    HelperUtils.sendMessageToPlayer(player,
                            "chat.message.switcher.read.mode");
                    break;
                case 1:
                    HelperUtils.sendMessageToPlayer(player,
                            "chat.message.switcher.write.mode");
                    break;
            }
        }
        return stack;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
                                  int side, float a, float b, float c) {
        if(!world.isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            TileEntity te = world.getTileEntity(x, y, z);
            if(te instanceof TileWPBasePersonal) {
                TileWPBasePersonal panel = (TileWPBasePersonal)te;
                if(player.getGameProfile().equals(panel.getOwner())) {
                    switch(nbt.getShort("mode")) {
                        case 0: // read channel
                            int ch1 = panel.getChannel();
                            nbt.setInteger("channel", ch1);
                            HelperUtils.sendChatMessageMultiUnicolored(player,
                                    "chat.message.switcher.channel.copied", EnumChatFormatting.GREEN,
                                    new ChatComponentText(": " + ch1));
                            break;
                        case 1: // write channel
                            if(nbt.hasKey("channel", 3)) {
                                int ch2 = nbt.getInteger("channel");
                                panel.setChannel(ch2);
                                HelperUtils.sendChatMessageMultiUnicolored(player,
                                        "chat.message.switcher.channel.written", EnumChatFormatting.DARK_AQUA,
                                        new ChatComponentText(": " + ch2));
                            }
                            else {
                                HelperUtils.sendColoredMessageToPlayer(player,
                                        "chat.message.switcher.no.channel", EnumChatFormatting.GOLD);
                            }
                            break;
                    }
                }
                else {
                    HelperUtils.sendColoredMessageToPlayer(player,
                            "chat.message.switcher.wrong.owner", EnumChatFormatting.RED);
                }
                return true;
            }
            if(te instanceof TileWirelessStorageBasePersonal) {
                TileWirelessStorageBasePersonal storage = (TileWirelessStorageBasePersonal)te;
                if(player.getGameProfile().equals(storage.getOwner())) {
                    switch(nbt.getShort("mode")) {
                        case 0: // read channel
                            int ch1 = storage.getChannel();
                            nbt.setInteger("channel", ch1);
                            HelperUtils.sendChatMessageMultiUnicolored(player,
                                    "chat.message.switcher.channel.copied", EnumChatFormatting.GREEN,
                                    new ChatComponentText(": " + ch1));
                            break;
                        case 1: // write channel
                            if(nbt.hasKey("channel", 3)) {
                                int ch2 = nbt.getInteger("channel");
                                storage.setChannel(ch2);
                                HelperUtils.sendChatMessageMultiUnicolored(player,
                                        "chat.message.switcher.channel.written", EnumChatFormatting.DARK_AQUA,
                                        new ChatComponentText(": " + ch2));
                            }
                            else {
                                HelperUtils.sendColoredMessageToPlayer(player,
                                        "chat.message.switcher.no.channel", EnumChatFormatting.GOLD);
                            }
                            break;
                    }
                }
                else {
                    HelperUtils.sendColoredMessageToPlayer(player,
                            "chat.message.switcher.wrong.owner", EnumChatFormatting.RED);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

}
