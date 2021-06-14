package ru.wirelesstools.item.armor;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuantumEnderHelmet extends ItemArmor implements IElectricItem, IMetalArmor, ISpecialArmor, IPrivateArmor {

    public double maxCharge;
    protected double transferLimit;
    protected int tier;

    protected static final Map<Integer, Integer> potionRemovalCost = new HashMap<>();

    public QuantumEnderHelmet(String name) {
        super(ArmorMaterial.DIAMOND, 0, 0); // 0 = helmet
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(27);
        this.setCreativeTab(MainWI.tabwi);
        this.maxCharge = 15000000.0;
        this.transferLimit = 100000.0;
        this.tier = 4;
        potionRemovalCost.put(Potion.poison.id, 100);
        potionRemovalCost.put(Potion.wither.id, 100);
        potionRemovalCost.put(Potion.weakness.id, 100);
        potionRemovalCost.put(Potion.hunger.id, 200);
        potionRemovalCost.put(Potion.confusion.id, 200);
        potionRemovalCost.put(Potion.blindness.id, 200);
        potionRemovalCost.put(IC2Potion.radiation.id, 200);
    }

    public EnumRarity getRarity(ItemStack itemstack) {

        return EnumRarity.epic;
    }

    @SideOnly(value = Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(Reference.PathTex + "itemArmorEnderQuantumHelmet");
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
                            EnumChatFormatting.GOLD + StatCollector.translateToLocal("chat.message.no.owner")
                    ));
                } else if (!NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile"))
                        .equals(player.getGameProfile())) {

                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.DARK_RED
                                    + StatCollector.translateToLocal("chat.message.you.cannot.clear.owner")
                    ));
                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.DARK_RED
                                    + StatCollector.translateToLocal("chat.message.owner.can.clear.owner")
                    ));
                } else {

                    nbt.removeTag("ownerGameProfile");
                    player.addChatMessage(new ChatComponentTranslation(
                            EnumChatFormatting.DARK_GREEN
                                    + StatCollector.translateToLocal("chat.message.owner.successfully.cleared")
                    ));
                }

            }
        }
        return stack;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (!world.isRemote) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null) {
                NBTTagCompound ownerNbt = new NBTTagCompound();
                NBTUtil.func_152460_a(ownerNbt, player.getGameProfile());
                nbt.setTag("ownerGameProfile", ownerNbt);
            }

            if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).equals(player.getGameProfile())) {
                if (world.provider.dimensionId == 1) {
                    ElectricItem.manager.charge(stack, ConfigWI.enderChargeArmorValue, Integer.MAX_VALUE, true, false);
                }
                byte toggleTimer = nbt.getByte("toggleTimer");
                byte nightvisionmode = nbt.getByte("NightVisMode");

                if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    nightvisionmode++;
                    if (nightvisionmode > 2) {
                        nightvisionmode = 0;
                    }
                    nbt.setByte("NightVisMode", nightvisionmode);
                    switch (nightvisionmode) {
                        case 0:
                            player.addChatMessage(new
                                    ChatComponentTranslation(EnumChatFormatting.RED +
                                    StatCollector.translateToLocal("chat.message.nightvision.off")));
                            break;
                        case 1:
                            player.addChatMessage(new
                                    ChatComponentTranslation(EnumChatFormatting.DARK_AQUA +
                                    StatCollector.translateToLocal("chat.message.nightvision.auto")));
                            break;
                        case 2:
                            player.addChatMessage(new
                                    ChatComponentTranslation(EnumChatFormatting.GREEN +
                                    StatCollector.translateToLocal("chat.message.nightvision.on")));
                            break;
                    }
                }

                if (nightvisionmode == 1) {
                    int x = MathHelper.floor_double(player.posX);
                    int z = MathHelper.floor_double(player.posZ);
                    int y = MathHelper.floor_double(player.posY);
                    int skylight = player.worldObj.getBlockLightValue(x, y, z);
                    if (skylight > 8) {
                        IC2.platform.removePotion(player, Potion.nightVision.id);
                    } else {
                        player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, 0, true));
                    }
                    player.inventoryContainer.detectAndSendChanges();
                } else if (nightvisionmode == 2) {
                    player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 300, 0, true));
                    player.inventoryContainer.detectAndSendChanges();
                }

                if (toggleTimer > 0) {
                    toggleTimer--;
                    nbt.setByte("toggleTimer", toggleTimer);
                }

                if (!player.getActivePotionEffects().isEmpty()) {
                    for (Object effect : new LinkedList(player.getActivePotionEffects())) {
                        int id = ((PotionEffect) effect).getPotionID();
                        Integer cost = potionRemovalCost.get(id);
                        if (cost == null || !ElectricItem.manager.canUse(stack,
                                (double) (cost = Integer.valueOf(cost * (((PotionEffect) effect).getAmplifier() + 1)))
                                        .intValue()))
                            continue;

                        ElectricItem.manager.use(stack, (double) cost.intValue(), player);
                        player.removePotionEffect(id);
                    }
                }

                if (ElectricItem.manager.canUse(stack, 1000.0) && player.getFoodStats().needFood()) {
                    for (int i = 0; i < player.inventory.mainInventory.length; i++) {
                        if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() instanceof ItemFood) {
                            ItemStack stack1 = player.inventory.mainInventory[i];
                            ItemFood can = (ItemFood) stack1.getItem();
                            stack1 = can.onEaten(stack1, world, player);
                            if (stack1.stackSize <= 0) {
                                player.inventory.mainInventory[i] = null;
                            }
                            ElectricItem.manager.use(stack, 1000.0, null);
                            player.inventoryContainer.detectAndSendChanges();
                        }
                    }
                } else if (player.getFoodStats().getFoodLevel() <= 0) {

                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }

                int airLevel = player.getAir();
                if (ElectricItem.manager.canUse(stack, 1000.0) && airLevel < 100) {
                    player.setAir(airLevel + 200);
                    ElectricItem.manager.use(stack, 1000.0, player);
                    player.inventoryContainer.detectAndSendChanges();
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
            return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
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

    @Override
    public void clearOwner(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        nbt.removeTag("ownerGameProfile");
    }

    @Override
    public GameProfile getArmorOwner(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

        return NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile"));
    }

}
