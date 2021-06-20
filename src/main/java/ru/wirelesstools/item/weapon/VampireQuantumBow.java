package ru.wirelesstools.item.weapon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import org.lwjgl.input.Keyboard;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.entityarrow.ArrowVampEUNew;
import ru.wirelesstools.entityarrow.ArrowVampXPNew;

import java.util.List;

public class VampireQuantumBow extends ItemBow implements IElectricItem {

    private final IIcon[] qbowEU = new IIcon[4];
    private final IIcon[] qbowXP = new IIcon[4];

    protected double maxenergy;
    protected int tier;
    protected double transferlimit;

    public VampireQuantumBow(String name) {
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setMaxDamage(27);
        this.setCreativeTab(MainWI.tabwi);
        this.maxenergy = ConfigWI.vampBowMaxCharge;
        this.tier = 3;
        this.transferlimit = 30000.0;
    }

    @SideOnly(value = Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List info, boolean par4) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (nbt.getBoolean("vampiremode")) {
                info.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("info.qbowxp.mode"));
                info.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("info.qbowxp.vamped.per.shot")
                + ": " + EnumChatFormatting.DARK_GREEN + String.valueOf(ConfigWI.vampBowXPVampiredAmount)
                + " " + StatCollector.translateToLocal("info.qbowxp.xppoints"));
                info.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("info.qbow.change.mode"));
            } else {
                info.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("info.qboweu.mode"));
                info.add(EnumChatFormatting.DARK_AQUA + StatCollector.translateToLocal("info.qboweu.about"));
                info.add(EnumChatFormatting.DARK_AQUA
                        + StatCollector.translateToLocal("info.qboweu.steal.energy") + ": "
                        + EnumChatFormatting.GREEN
                        + String.valueOf(ConfigWI.stolenEnergyEUFromArmor) + " EU");
                info.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("info.qbow.change.mode"));
            }
        } else {
            info.add(EnumChatFormatting.ITALIC + I18n.format("press.lshift"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
        this.qbowEU[0] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_standby");
        this.qbowEU[1] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_0");
        this.qbowEU[2] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_1");
        this.qbowEU[3] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_2");

        this.qbowXP[0] = reg.registerIcon(Reference.PathTex + "quantumbow_xp_steal_standby");
        this.qbowXP[1] = reg.registerIcon(Reference.PathTex + "quantumbow_xp_steal_0");
        this.qbowXP[2] = reg.registerIcon(Reference.PathTex + "quantumbow_xp_steal_1");
        this.qbowXP[3] = reg.registerIcon(Reference.PathTex + "quantumbow_xp_steal_2");
    }

    @SideOnly(value = Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int n) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return nbt.getBoolean("vampiremode") ? this.qbowXP[0] : this.qbowEU[0];
    }

    public EnumAction getItemUseAction(ItemStack par1ItemStack) {

        return EnumAction.bow;
    }

    public int getMaxItemUseDuration(ItemStack par1ItemStack) {

        return 72000;
    }

    public boolean onBlockDestroyed(ItemStack par1ItemStack, World world, Block block, int par4, int par5, int par6,
                                    EntityLivingBase par7EntityLiving) {

        return true;
    }

    @SideOnly(value = Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        if (usingItem != null
                && Item.getIdFromItem(usingItem.getItem()) == Item.getIdFromItem(MainWI.quantumVampBowEu)) {
            int usingticks = this.getMaxItemUseDuration(stack) - useRemaining;
            if (usingticks > 17)
                return nbt.getBoolean("vampiremode") ? this.qbowXP[3] : this.qbowEU[3];

            if (usingticks > 13)
                return nbt.getBoolean("vampiremode") ? this.qbowXP[2] : this.qbowEU[2];

            if (usingticks > 0)
                return nbt.getBoolean("vampiremode") ? this.qbowXP[1] : this.qbowEU[1];
        }
        return nbt.getBoolean("vampiremode") ? this.qbowXP[0] : this.qbowEU[0];
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(itemstack);
        return nbt.getBoolean("vampiremode") ? EnumRarity.uncommon : MainWI.RARITY_EU;
    }

    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving,
                             EntityLivingBase par3EntityLiving) {
        return true;
    }

    @Override
    public boolean isFull3D() {

        return true;
    }

    public void getSubItems(Item var1, CreativeTabs var2, List itemList) {
        ItemStack chargedbow = new ItemStack(this);
        ElectricItem.manager.charge(chargedbow, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
        itemList.add(chargedbow);
        ItemStack dischargedbow = new ItemStack(this);
        ElectricItem.manager.charge(dischargedbow, 0.0, Integer.MAX_VALUE, true, false);
        itemList.add(dischargedbow);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!(ElectricItem.manager.canUse(itemStack, ConfigWI.vampBowShotEnergyCost) &&
                !player.capabilities.isCreativeMode) ^ player.capabilities.isCreativeMode) {
            return itemStack;
        }
        ArrowNockEvent event = new ArrowNockEvent(player, itemStack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            return event.result;

        if (!world.isRemote && IC2.keyboard.isModeSwitchKeyDown(player)) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(itemStack);
            boolean vampmodenew = !nbt.getBoolean("vampiremode");
            nbt.setBoolean("vampiremode", vampmodenew);
            if (vampmodenew) {
                player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.YELLOW
                        + StatCollector.translateToLocal("info.chatmessage.vampmode.xp")));
            } else {
                player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.DARK_AQUA
                        + StatCollector.translateToLocal("info.chatmessage.vampmode.eu")));
            }
            return itemStack;
        }

        player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));

        return itemStack;
    }

    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount) {
        int inUseDuration = this.getMaxItemUseDuration(itemStack) - itemInUseCount;
        ArrowLooseEvent event = new ArrowLooseEvent(player, itemStack, inUseDuration);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled())
            return;

        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(itemStack);
        boolean vampXPmodelocal = nbt.getBoolean("vampiremode");

        inUseDuration = event.charge;
        float inUseSeconds = (float) inUseDuration / 20.0F;
        inUseSeconds = (inUseSeconds * inUseSeconds + inUseSeconds * 2.0F) / 3.0F;

        if (inUseSeconds < 0.1F)
            return;

        if (inUseSeconds > 1.0F)
            inUseSeconds = 1.0F;

        if (vampXPmodelocal) {
            ArrowVampXPNew arrowvampxpNew = new ArrowVampXPNew(world, player, inUseSeconds * 2.0F);

            if (inUseSeconds == 1.0F)
                arrowvampxpNew.setIsCritical(true);

            int enchpow = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
            if (arrowvampxpNew.getIsCritical()) {
                enchpow += 3;
            }

            if (enchpow > 0) {
                arrowvampxpNew.setDamage(arrowvampxpNew.getDamage() + (double) enchpow * 0.5 + 0.5);
            }

            int enchpunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack);
            if (arrowvampxpNew.getIsCritical()) {
                ++enchpunch;
            }

            if (enchpunch > 0) {
                arrowvampxpNew.setKnockbackStrength(enchpunch);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) > 0) {
                arrowvampxpNew.setFire(100);
            }

            world.playSoundAtEntity(player, "random.bow", 1.0f,
                    1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + inUseSeconds * 0.5f);
            arrowvampxpNew.canBePickedUp = 2;

            if (!world.isRemote) {
                if (!player.capabilities.isCreativeMode)
                    ElectricItem.manager.use(itemStack, ConfigWI.vampBowShotEnergyCost, player);

                world.spawnEntityInWorld(arrowvampxpNew);
            }
        } else {
            ArrowVampEUNew arrowvampeuNew = new ArrowVampEUNew(world, player, inUseSeconds * 2.0F);

            if (inUseSeconds == 1.0F)
                arrowvampeuNew.setIsCritical(true);

            int enchpow = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
            if (arrowvampeuNew.getIsCritical()) {
                enchpow += 3;
            }

            if (enchpow > 0) {
                arrowvampeuNew.setDamage(arrowvampeuNew.getDamage() + (double) enchpow * 0.5 + 0.5);
            }

            int enchpunch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack);
            if (arrowvampeuNew.getIsCritical()) {
                ++enchpunch;
            }

            if (enchpunch > 0) {
                arrowvampeuNew.setKnockbackStrength(enchpunch);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) > 0) {
                arrowvampeuNew.setFire(100);
            }

            world.playSoundAtEntity(player, "random.bow", 1.0f,
                    1.0f / (itemRand.nextFloat() * 0.4f + 1.2f) + inUseSeconds * 0.5f);
            arrowvampeuNew.canBePickedUp = 2;

            if (!world.isRemote) {
                if (!player.capabilities.isCreativeMode)
                    ElectricItem.manager.use(itemStack, ConfigWI.vampBowShotEnergyCost, player);

                world.spawnEntityInWorld(arrowvampeuNew);
            }
        }
    }

    public int getItemEnchantability() {

        return 0;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {

        return false;
    }

    @Override
    public boolean canProvideEnergy(ItemStack arg0) {

        return false;
    }

    @Override
    public Item getChargedItem(ItemStack arg0) {

        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack arg0) {

        return this;
    }

    @Override
    public double getMaxCharge(ItemStack arg0) {

        return this.maxenergy;
    }

    @Override
    public int getTier(ItemStack arg0) {

        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack arg0) {

        return this.transferlimit;
    }

}
