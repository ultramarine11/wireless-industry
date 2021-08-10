package ru.wirelesstools.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.item.tool.ItemDebug;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import org.lwjgl.input.Keyboard;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.utils.MiscUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuantumChestplateWirelessCharge extends ItemArmor implements IElectricItem, IMetalArmor, ISpecialArmor {

    public double maxCharge;
    protected double transferLimit;
    protected int tier;
    protected static final List<Integer> listpotionsid = new ArrayList<>();

    public QuantumChestplateWirelessCharge(String name) {
        super(ArmorMaterial.DIAMOND, 0, 1);
        this.setUnlocalizedName(name);
        //  this.setMaxStackSize(1);
        this.maxCharge = 500000000.0D;
        this.transferLimit = 500000.0D;
        this.tier = 4;
        this.setMaxDamage(27);
        this.setCreativeTab(MainWI.tabwi);
        listpotionsid.add(Potion.poison.id);
        listpotionsid.add(Potion.wither.id);
        listpotionsid.add(Potion.weakness.id);
        listpotionsid.add(Potion.hunger.id);
        listpotionsid.add(Potion.confusion.id);
        listpotionsid.add(Potion.blindness.id);
        listpotionsid.add(Potion.harm.id);
        listpotionsid.add(Potion.moveSlowdown.id);
        listpotionsid.add(IC2Potion.radiation.id);
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return MainWI.RARITY_CHESTPLATE_WIRELESS;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        boolean active = nbt.getBoolean("active");
        boolean buffmode = nbt.getBoolean("potionbuffs");
        byte toggleTimer = nbt.getByte("toggleTimer");
        if(IC2.keyboard.isModeSwitchKeyDown(player) && !player.isSneaking() && toggleTimer == 0) {
            toggleTimer = 10;
            active = !active;
            if(!world.isRemote) {
                nbt.setBoolean("active", active);
                if(active) {
                    MiscUtils.sendColoredMessageToPlayer(player, "chat.message.wirelesscharge.on", EnumChatFormatting.DARK_GREEN);
                } else {
                    MiscUtils.sendColoredMessageToPlayer(player, "chat.message.wirelesscharge.off", EnumChatFormatting.DARK_RED);
                }
            }
        }

        if(IC2.keyboard.isBoostKeyDown(player) && player.isSneaking() && toggleTimer == 0) {
            toggleTimer = 10;
            buffmode = !buffmode;
            if(!world.isRemote) {
                nbt.setBoolean("potionbuffs", buffmode);
                if(buffmode) {
                    MiscUtils.sendColoredMessageToPlayer(player, "chat.message.chestplate.buffs1.on", EnumChatFormatting.DARK_AQUA);
                    MiscUtils.sendColoredMessageToPlayer(player, "chat.message.chestplate.buffs2.on", EnumChatFormatting.DARK_AQUA);
                } else {
                    MiscUtils.sendColoredMessageToPlayer(player, "chat.message.chestplate.buffs.off", EnumChatFormatting.YELLOW);
                }
            }
        }

        if(!world.isRemote) {
            if(active)
                this.checkPlayers(player, world, stack);

            if(buffmode) {
                for(PotionEffect effect : new LinkedList<PotionEffect>(player.getActivePotionEffects())) {
                    int potionid = effect.getPotionID();
                    if(listpotionsid.contains(Integer.valueOf(potionid))) {
                        if(ElectricItem.manager.canUse(stack, 100)) {
                            player.removePotionEffect(potionid);
                            ElectricItem.manager.discharge(stack, 100, Integer.MAX_VALUE, true,
                                    false, false);
                        }
                    }
                }

                if(ElectricItem.manager.canUse(stack, 4)) {
                    player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, 0, true));
                    player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200, 1, true));
                    player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0, true));
                    player.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, 0, true));
                    ElectricItem.manager.discharge(stack, 4, Integer.MAX_VALUE, true,
                            false, false);
                }
            }

            boolean jetpack = nbt.getBoolean("jetpack");
            boolean hoverMode = nbt.getBoolean("hoverMode");
            boolean jetpackUsed = false;
            if(IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                toggleTimer = 10;
                hoverMode = !hoverMode;
                nbt.setBoolean("hoverMode", hoverMode);
                if(hoverMode) {
                    IC2.platform.messagePlayer(player, "Quantum Hover Mode enabled.");
                } else {
                    IC2.platform.messagePlayer(player, "Quantum Hover Mode disabled.");
                }
            }

            if(IC2.keyboard.isBoostKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                toggleTimer = 10;
                jetpack = !jetpack;
                nbt.setBoolean("jetpack", jetpack);
                if(jetpack) {
                    IC2.platform.messagePlayer(player, "Quantum Jetpack enabled.");
                } else {
                    IC2.platform.messagePlayer(player, "Quantum Jetpack disabled.");
                }
            }

            if(jetpack && (IC2.keyboard.isJumpKeyDown(player) || hoverMode && player.motionY < -0.03)) {
                jetpackUsed = this.useJetpack(player, hoverMode);
            }

            if(jetpackUsed)
                player.inventoryContainer.detectAndSendChanges();
        }

        if(IC2.platform.isSimulating() && toggleTimer > 0) {
            toggleTimer--;
            nbt.setByte("toggleTimer", toggleTimer);
        }

        if(player.isBurning())
            player.extinguish();
    }

    protected void checkPlayers(EntityPlayer player, World world, ItemStack thisarmor) {
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(player.posX - ConfigWI.chestplateChargingRadius, player.posY - ConfigWI.chestplateChargingRadius,
                player.posZ - ConfigWI.chestplateChargingRadius, player.posX + ConfigWI.chestplateChargingRadius, player.posY + ConfigWI.chestplateChargingRadius, player.posZ + ConfigWI.chestplateChargingRadius);
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, axisalignedbb);
        for(Entity entityinlist : list) {
            if(entityinlist instanceof EntityPlayer) {
                this.checkInvPlayer((EntityPlayer) entityinlist, thisarmor);
            }
        }
    }

    protected void checkInvPlayer(EntityPlayer player, ItemStack thisarmor) {
        for(ItemStack current : player.inventory.armorInventory) {
            if(current == null || current.getItem() instanceof ItemDebug)
                continue;
            if(current.getItem() instanceof QuantumChestplateWirelessCharge)
                continue;
            if(current.getItem() instanceof IElectricItem)
                MiscUtils.chargeEUItemFromArmor(current, thisarmor);
        }

        for(ItemStack current : player.inventory.mainInventory) {
            if(current == null || current.getItem() instanceof ItemDebug)
                continue;
            if(current.getItem() instanceof IElectricItem)
                MiscUtils.chargeEUItemFromArmor(current, thisarmor);
        }
    }

    protected boolean useJetpack(EntityPlayer player, boolean hoverMode) {
        int worldHeight;
        double y;
        ItemStack jetpack = player.inventory.armorInventory[2];
        if(ElectricItem.manager.getCharge(jetpack) == 0.0)
            return false;
        float power = 1.0f;
        float dropPercentage = 0.05f;
        if(ElectricItem.manager.getCharge(jetpack) / this.getMaxCharge(jetpack) <= (double) dropPercentage) {
            power = (float) ((double) power * (ElectricItem.manager.getCharge(jetpack)
                    / (this.getMaxCharge(jetpack) * (double) dropPercentage)));
        }
        if(IC2.keyboard.isForwardKeyDown(player)) {
            float forwardpower;
            float retruster = 3.5f;
            if(hoverMode) {
                retruster = 0.5f;
            }
            if((forwardpower = power * retruster * 2.0f) > 0.0f) {
                player.moveFlying(0.0f, 0.4f * forwardpower, 0.02f);
            }
        }
        if((y = player.posY) > (double) ((worldHeight = IC2.getWorldHeight(player.worldObj)) - 25)) {
            if(y > (double) worldHeight) {
                y = worldHeight;
            }
            power = (float) ((double) power * (((double) worldHeight - y) / 25.0));
        }
        double prevmotion = player.motionY;
        player.motionY = Math.min(player.motionY + (double) (power * 0.2f), 0.6);
        if(hoverMode) {
            float maxHoverY = -0.025f;
            if(IC2.keyboard.isSneakKeyDown(player)) {
                maxHoverY = -0.1f;
            }
            if(IC2.keyboard.isJumpKeyDown(player)) {
                maxHoverY = 0.1f;
            }
            if(player.motionY > (double) maxHoverY) {
                player.motionY = maxHoverY;
                if(prevmotion > player.motionY) {
                    player.motionY = prevmotion;
                }
            }
        }
        double consume = 8.0;
        if(hoverMode)
            consume = 10.0;
        ElectricItem.manager.discharge(jetpack, consume, Integer.MAX_VALUE, true, false, false);
        player.fallDistance = 0.0f;
        player.distanceWalkedModified = 0.0f;
        IC2.platform.resetPlayerInAirTime(player);
        return true;
    }

    @SideOnly(value = Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        list.add(StatCollector.translateToLocal("info.wirelesscharge.about"));
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            String isonoff = nbt.getBoolean("active") ? EnumChatFormatting.GREEN
                    + StatCollector.translateToLocal("info.yes") : EnumChatFormatting.RED
                    + StatCollector.translateToLocal("info.no");
            String isBuffOnOff = nbt.getBoolean("potionbuffs") ? EnumChatFormatting.DARK_AQUA
                    + StatCollector.translateToLocal("info.buffs.yes") : EnumChatFormatting.YELLOW
                    + StatCollector.translateToLocal("info.buffs.no");
            list.add(StatCollector.translateToLocal("info.wirelesscharge.mode") + ": "
                    + isonoff);
            list.add(StatCollector.translateToLocal("info.wirelesscharge.radius") + ": "
                    + EnumChatFormatting.DARK_GREEN + String.valueOf(ConfigWI.chestplateChargingRadius) + " "
                    + StatCollector.translateToLocal("info.wirelesscharge.blocks"));
            list.add(isBuffOnOff);
            list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("info.press.modeswitch.key")
                    + " " + StatCollector.translateToLocal("info.to.switch"));
            list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("info.press.boost.key")
                    + " " + StatCollector.translateToLocal("info.and.sneak") + " "
                    + StatCollector.translateToLocal("info.to.switch.buff.mode"));
        } else {
            list.add(EnumChatFormatting.ITALIC + I18n.format("press.lshift"));
        }
    }

    public int getEnergyPerDamage() {
        return 5000;
    }

    public double getDamageAbsorptionRatio() {
        return 1.1;
    }

    private double getBaseAbsorptionRatio() {
        return 0.4;
    }

    public boolean isRepairable() {
        return false;
    }

    public int getItemEnchantability() {
        return 0;
    }

    @SideOnly(value = Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack stack = new ItemStack(this, 1);
        ElectricItem.manager.charge(stack, 0.0, Integer.MAX_VALUE, true, false);
        list.add(stack);
        ItemStack stack1 = new ItemStack(this, 1);
        ElectricItem.manager.charge(stack1, 2.147483647E9, Integer.MAX_VALUE, true, false);
        list.add(stack1);
    }

    @SideOnly(value = Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(Reference.PathTex + "wirelesschestplate");
    }

    @SideOnly(value = Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return Reference.PathTex + "textures/armor/armor_WirelessChestPlate1710.png";
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
                                         int slot) {
        double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        int energyPerDamage = this.getEnergyPerDamage();
        int damageLimit = (int) (energyPerDamage > 0
                ? 25.0 * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                : 0.0);
        return new ArmorProperties(0, absorptionRatio, damageLimit);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio());
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
        return true;
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
