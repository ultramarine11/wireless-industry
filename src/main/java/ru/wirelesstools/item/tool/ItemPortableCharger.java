package ru.wirelesstools.item.tool;

import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.utils.HelperUtils;

import java.util.List;

public class ItemPortableCharger extends Item implements IElectricItem {

    protected double maxCharge;
    protected final int tier;
    protected final double transferLimit;

    public ItemPortableCharger() {
        this.setUnlocalizedName("wi.portablecharger");
        this.setTextureName(Reference.PathTex + "quantumwirelesscharger");
        this.setCreativeTab(MainWI.tabwi);
        this.setMaxStackSize(1);
        this.setMaxDamage(27);
        this.setNoRepair();
        this.maxCharge = 80000000.0;
        this.tier = 4;
        this.transferLimit = 20000.0;
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean extraInformation) {
        if(org.lwjgl.input.Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            info.add(I18n.format("info.wi.qwcharger.about"));
            info.add(I18n.format("info.wi.qwcharger.radius") + ": "
                    + nbt.getInteger("radius")
                    + " " + I18n.format("info.wirelesscharge.blocks"));
            info.add(I18n.format("info.wi.qwcharger.charge.rate") + ": "
                    + String.format("%.0f", this.getRateFromModeRate(nbt.getShort("rate"))) + " EU/t");
            info.add(nbt.getBoolean("enabled_on")
                    ? EnumChatFormatting.GREEN + I18n.format("info.wi.qwcharger.on")
                    : EnumChatFormatting.RED + I18n.format("info.wi.qwcharger.off"));
        }
        else
            info.add(EnumChatFormatting.ITALIC + I18n.format("press.lshift"));

        if(Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
            // GameSettings.getKeyDisplayString(56);  ALT key IC2
            // GameSettings.getKeyDisplayString(50);  Mode switch key IC2
            info.add(I18n.format("qwch.tooltip.change.rate") + " "
                    + GameSettings.getKeyDisplayString(50) + " + "
                    + I18n.format("qwch.tooltip.press.rmb"));
            info.add(I18n.format("qwch.tooltip.change.radius"));
            info.add(I18n.format("qwch.tooltip.on_off"));
        }
        else
            info.add(EnumChatFormatting.ITALIC + I18n.format("press.leftalt"));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if(player.isSneaking()) {
                int radius = nbt.getInteger("radius");
                if(++radius > 15)
                    radius = 1;
                nbt.setInteger("radius", radius);
                HelperUtils.sendChatMessageMultiUnicolored(player, "chat.message.portablecharger.radius", EnumChatFormatting.GOLD,
                        new ChatComponentText(": " + radius + " "), new ChatComponentTranslation("info.wirelesscharge.blocks"));
            }
            else {
                if(IC2.keyboard.isModeSwitchKeyDown(player)) {
                    short chargeModeRate = nbt.getShort("rate");
                    if(++chargeModeRate > 6)
                        chargeModeRate = 0;
                    nbt.setShort("rate", chargeModeRate);
                    HelperUtils.sendChatMessageMultiUnicolored(player, "chat.message.portablecharger.chargerate", EnumChatFormatting.GREEN,
                            new ChatComponentText(": " + String.format("%.0f", this.getRateFromModeRate(chargeModeRate))),
                            new ChatComponentText(" EU/t"));
                }
                else {
                    boolean enabled = nbt.getBoolean("enabled_on");
                    enabled = !enabled;
                    nbt.setBoolean("enabled_on", enabled);
                    HelperUtils.sendColoredMessageToPlayer(player,
                            enabled ? "chat.message.portablecharger.on" : "chat.message.portablecharger.off",
                            enabled ? EnumChatFormatting.AQUA : EnumChatFormatting.LIGHT_PURPLE);
                }
            }
        }
        return stack;
    }

    protected double getRateFromModeRate(short modeRate) {
        switch(modeRate) {
            case 0:
                return 2048.0;
            case 1:
                return 4096.0;
            case 2:
                return 8192.0;
            case 3:
                return 16384.0;
            case 4:
                return 32768.0;
            case 5:
                return 65536.0;
            case 6:
                return 131072.0;
            default:
                return 0.0;
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
        ItemStack charged = new ItemStack(this);
        ItemStack discharged = new ItemStack(this);
        ElectricItem.manager.charge(charged, Double.MAX_VALUE, Integer.MAX_VALUE, true, false);
        ElectricItem.manager.charge(discharged, 0.0, Integer.MAX_VALUE, true, false);
        StackUtil.getOrCreateNbtData(charged).setInteger("radius", 5);
        StackUtil.getOrCreateNbtData(discharged).setInteger("radius", 5);
        itemList.add(charged);
        itemList.add(discharged);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            StackUtil.getOrCreateNbtData(stack).setInteger("radius", 5);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if(!world.isRemote) {
            if(entity instanceof EntityPlayer) {
                NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
                EntityPlayer player = (EntityPlayer)entity;
                int radius = nbt.getInteger("radius");
                if(nbt.getBoolean("enabled_on")) {
                    for(double x = player.posX - radius; x < player.posX + radius + 1; x++) {
                        for(double y = player.posY - radius; y < player.posY + radius + 1; y++) {
                            for(double z = player.posZ - radius; z < player.posZ + radius + 1; z++) {
                                TileEntity te = world.getTileEntity((int)x, (int)y, (int)z);
                                short rateMode = nbt.getShort("rate");
                                double qwchCharge = ElectricItem.manager.getCharge(stack);
                                if(te instanceof IEnergySink) {
                                    IEnergySink sink = (IEnergySink)te;
                                    double demandedEnergy = sink.getDemandedEnergy();
                                    if(demandedEnergy > 0.0 && qwchCharge > 0.0) {
                                        double realSent = Math.min(demandedEnergy,
                                                Math.min(qwchCharge, this.getRateFromModeRate(rateMode)));
                                        sink.injectEnergy(ForgeDirection.UNKNOWN, realSent, 1.0);
                                        ElectricItem.manager.discharge(stack, realSent, Integer.MAX_VALUE, true, false, false);
                                    }
                                }
                                else if(te instanceof IEnergyReceiver) {
                                    IEnergyReceiver rfTile = (IEnergyReceiver)te;
                                    if((rfTile.getMaxEnergyStored(ForgeDirection.UNKNOWN) - rfTile.getEnergyStored(ForgeDirection.UNKNOWN)) > 0
                                            && qwchCharge > 0.0) {
                                        ElectricItem.manager.discharge(stack,
                                                rfTile.receiveEnergy(ForgeDirection.UNKNOWN,
                                                        (int)Math.min(qwchCharge, this.getRateFromModeRate(rateMode)) * ConfigWI.EUToRF_Multiplier,
                                                        false) / (double)ConfigWI.EUToRF_Multiplier, Integer.MAX_VALUE,
                                                true, false, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
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
}
