package ru.wirelesstools.item.armor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;

import java.util.List;

public class QuantumEnderBoots extends ItemArmor implements IElectricItem, IMetalArmor, ISpecialArmor, IPrivateArmor {

    public double maxCharge;
    protected double transferLimit;
    protected int tier;

    private float jumpCharge;

    public QuantumEnderBoots(String name) {
        super(ArmorMaterial.DIAMOND, 0, 3); // 3 = boots
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(27);
        this.setCreativeTab(MainWI.tabwi);
        this.maxCharge = 15000000.0;
        this.transferLimit = 100000.0;
        this.tier = 4;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public EnumRarity getRarity(ItemStack itemstack) {

        return EnumRarity.epic;
    }

    @SideOnly(value = Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(Reference.PathTex + "itemArmorEnderQuantumBoots");
    }

    @SideOnly(value = Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.PathTex + "textures/armor/enderquantum_1.png";
    }

    @SideOnly(value = Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stack = new ItemStack(this, 1);
        ElectricItem.manager.charge(stack, 2.147483647E9, Integer.MAX_VALUE, true, false);
        list.add(stack);
        ItemStack stack1 = new ItemStack(this, 1);
        ElectricItem.manager.charge(stack1, 0.0D, Integer.MAX_VALUE, true, false);
        list.add(stack1);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if (player.isSneaking()) {
                if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null) {
                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.GOLD + StatCollector.translateToLocal("chat.message.no.owner")));
                } else if (!NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile"))
                        .equals(player.getGameProfile())) {
                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.DARK_RED
                                    + StatCollector.translateToLocal("chat.message.you.cannot.clear.owner")));
                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.DARK_RED
                                    + StatCollector.translateToLocal("chat.message.owner.can.clear.owner")));
                } else {
                    nbt.removeTag("ownerGameProfile");
                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.DARK_GREEN
                                    + StatCollector.translateToLocal("chat.message.owner.successfully.cleared")));
                }
            }
        }
        return stack;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        if (!world.isRemote) {
            if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null) {
                this.setArmorOwner(stack, player);
            }

            if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).equals(player.getGameProfile())) {
                if (world.provider.dimensionId == 1)
                    ElectricItem.manager.charge(stack, ConfigWI.enderChargeArmorValue, Integer.MAX_VALUE, true, false);


                boolean wasOnGround = nbt.hasKey("wasOnGround") ? nbt.getBoolean("wasOnGround") : true;
                if (wasOnGround && !player.onGround && IC2.keyboard.isJumpKeyDown(player)
                        && IC2.keyboard.isBoostKeyDown(player)) {
                    ElectricItem.manager.use(stack, 4000.0, null);
                    player.inventoryContainer.detectAndSendChanges();
                }

                if (player.onGround != wasOnGround) {
                    nbt.setBoolean("wasOnGround", player.onGround);
                }
            }
        } else {
            if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) != null
                    && NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).equals(player.getGameProfile())) {
                if (ElectricItem.manager.canUse(stack, 4000.0) && player.onGround)
                    this.jumpCharge = 1.0F;

                if (player.motionY >= 0.0 && this.jumpCharge > 0.0f && !player.isInWater()) {
                    if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player)) {
                        if (this.jumpCharge == 1.0F) {
                            player.motionX *= 3.5;
                            player.motionZ *= 3.5;
                        }
                        player.motionY += this.jumpCharge * 0.3F;
                        this.jumpCharge *= 0.75;
                    } else if (this.jumpCharge < 1.0F) {
                        this.jumpCharge = 0.0F;
                    }
                }
            }
        }
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null) {
            list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("info.eqarmor.noowner1"));
            list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("info.eqarmor.noowner2"));
        } else if (!NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).equals(player.getGameProfile())) {
            list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("info.eqarmor.incorrectowner1"));
            list.add(EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("info.eqarmor.incorrectowner2"));
            list.add(EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.ITALIC.toString() + StatCollector.translateToLocal("info.eqarmor.owner.is") + ": "
                    + NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).getName());
        } else {
            list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("info.eqarmor.correctowner1"));
            list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("info.eqarmor.correctowner2"));
            list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("info.eqarmor.you.can.clear"));
            if (player.worldObj.provider.dimensionId == 1) {
                list.add(EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC.toString()
                        + StatCollector.translateToLocal("info.eqarmor.is.charging.ender") + ": "
                        + String.valueOf(ConfigWI.enderChargeArmorValue) + " EU/t");
            } else {
                list.add(EnumChatFormatting.GOLD.toString() + EnumChatFormatting.ITALIC.toString()
                        + StatCollector.translateToLocal("info.eqarmor.go.to.ender.dim"));
            }
        }
    }

    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if (player != null) {
                NBTTagCompound ownerNbt = new NBTTagCompound();
                NBTUtil.func_152460_a(ownerNbt, player.getGameProfile());
                nbt.setTag("ownerGameProfile", ownerNbt);
            }
        }
    }

    @SubscribeEvent
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IC2.platform.isSimulating() && event.entity instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.entity;
            ItemStack armor = entity.getEquipmentInSlot(1);
            if (armor != null && armor.getItem() == this) {
                if (entity instanceof EntityPlayer) {
                    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(armor);
                    if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) != null
                            && NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile"))
                            .equals(((EntityPlayer) entity).getGameProfile())) {
                        int fallDamage = Math.max((int) event.distance - 10, 0);
                        double energyCost = this.getEnergyPerDamage() * fallDamage;
                        if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                            ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false, false);
                            event.setCanceled(true);
                        }
                    }
                } else {
                    int fallDamage = Math.max((int) event.distance - 10, 0);
                    double energyCost = this.getEnergyPerDamage() * fallDamage;
                    if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                        ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false, false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public boolean isRepairable() {

        return false;
    }

    public int getItemEnchantability() {

        return 0;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {

        return false;
    }

    public double getDamageAbsorptionRatio() {

        return 1.0;
    }

    private double getBaseAbsorptionRatio() {
        return 0.15;
    }

    public int getEnergyPerDamage() {

        return 15000;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
                                         int slot) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(armor);
        double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        int energyPerDamage = this.getEnergyPerDamage();
        int damageLimit = Integer.MAX_VALUE;
        if (energyPerDamage > 0) {
            damageLimit = (int) Math.min(damageLimit, 25.0 * ElectricItem.manager.getCharge(armor) / energyPerDamage);
        }
        if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null
                || (player instanceof EntityPlayer && !NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile"))
                .equals(((EntityPlayer) player).getGameProfile()))) {
            return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
        } else {
            if (source == DamageSource.fall) {

                return new ISpecialArmor.ArmorProperties(10, 1.0, damageLimit);
            } else {

                return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
            }
        }
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(armor);
        if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null
                || !NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).equals(player.getGameProfile())) {

            return 0;
        } else {

            return (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio());
        }
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

        ElectricItem.manager.discharge(stack, damage * this.getEnergyPerDamage(), Integer.MAX_VALUE, true, false,
                false);
    }

    @Override
    public boolean isMetalArmor(ItemStack arg0, EntityPlayer arg1) {

        return true;
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

        return this.maxCharge;
    }

    @Override
    public int getTier(ItemStack arg0) {

        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack arg0) {

        return this.transferLimit;
    }

}
