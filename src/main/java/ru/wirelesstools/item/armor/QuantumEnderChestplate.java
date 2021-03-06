package ru.wirelesstools.item.armor;

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
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;

import java.util.List;

public class QuantumEnderChestplate extends ItemArmor
		implements IElectricItem, IMetalArmor, ISpecialArmor, IPrivateArmor {

	public double maxCharge;
	protected double transferLimit;
	protected int tier;

	public QuantumEnderChestplate(String name) {
		super(ArmorMaterial.DIAMOND, 0, 1); // 1 = chestplate
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(27);
		this.setCreativeTab(MainWI.tabwi);
		this.maxCharge = 15000000.0;
		this.transferLimit = 100000.0;
		this.tier = 4;
	}

	public EnumRarity getRarity(ItemStack itemstack) {

		return EnumRarity.epic;
	}

	@SideOnly(value = Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(Reference.PathTex + "itemArmorEnderQuantumChestplate");
	}

	@SideOnly(value = Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if(player.worldObj.provider.dimensionId == 1) {
				return Reference.PathTex + "textures/armor/enderquantum_charge.png";
			}
			else {
				return Reference.PathTex + "textures/armor/enderquantum_1.png";
			}
		}
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
			byte toggleTimer = nbt.getByte("toggleTimer");
			if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")) == null) {
				this.setArmorOwner(stack, player);
			}

			if (NBTUtil.func_152459_a(nbt.getCompoundTag("ownerGameProfile")).equals(player.getGameProfile())) {
				if (world.provider.dimensionId == 1)
					ElectricItem.manager.charge(stack, ConfigWI.enderChargeArmorValue, Integer.MAX_VALUE, true, false);

				boolean jetpack = nbt.getBoolean("jetpack");
				boolean hoverMode = nbt.getBoolean("hoverMode");
				boolean jetpackUsed = false;
				if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player)
						&& toggleTimer == 0) {
					toggleTimer = 10;
					hoverMode = !hoverMode;
					nbt.setBoolean("hoverMode", hoverMode);
					if (hoverMode) {
						IC2.platform.messagePlayer(player, "Quantum Hover Mode enabled.");
					} else {
						IC2.platform.messagePlayer(player, "Quantum Hover Mode disabled.");
					}
				}

				if (IC2.keyboard.isBoostKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player)
						&& toggleTimer == 0) {
					toggleTimer = 10;
					jetpack = !jetpack;
					nbt.setBoolean("jetpack", jetpack);
					if (jetpack) {
						IC2.platform.messagePlayer(player, "Quantum Jetpack enabled.");
					} else {
						IC2.platform.messagePlayer(player, "Quantum Jetpack disabled.");
					}
				}

				if (jetpack && (IC2.keyboard.isJumpKeyDown(player) || hoverMode && player.motionY < -0.03)) {
					jetpackUsed = this.useJetpack(player, hoverMode);
				}

				if (toggleTimer > 0) {
					toggleTimer--;
					nbt.setByte("toggleTimer", toggleTimer);
				}

				if (jetpackUsed)
					player.inventoryContainer.detectAndSendChanges();

				if (player.isBurning()) {
					player.extinguish();
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

	protected boolean useJetpack(EntityPlayer player, boolean hoverMode) {
		int worldHeight;
		double y;
		ItemStack jetpack = player.inventory.armorInventory[2];
		if (ElectricItem.manager.getCharge(jetpack) == 0.0)
			return false;
		float power = 1.0f;
		float dropPercentage = 0.05f;
		if (ElectricItem.manager.getCharge(jetpack) / this.getMaxCharge(jetpack) <= (double) dropPercentage) {
			power = (float) ((double) power * (ElectricItem.manager.getCharge(jetpack)
					/ (this.getMaxCharge(jetpack) * (double) dropPercentage)));
		}
		if (IC2.keyboard.isForwardKeyDown(player)) {
			float forwardpower;
			float retruster = 3.5f;
			if (hoverMode) {
				retruster = 0.5f;
			}
			if ((forwardpower = power * retruster * 2.0f) > 0.0f) {
				player.moveFlying(0.0f, 0.4f * forwardpower, 0.02f);
			}
		}
		if ((y = player.posY) > (double) ((worldHeight = IC2.getWorldHeight(player.worldObj)) - 25)) {
			if (y > (double) worldHeight) {
				y = worldHeight;
			}
			power = (float) ((double) power * (((double) worldHeight - y) / 25.0));
		}
		double prevmotion = player.motionY;
		player.motionY = Math.min(player.motionY + (double) (power * 0.2f), 0.6);
		if (hoverMode) {
			float maxHoverY = -0.025f;
			if (IC2.keyboard.isSneakKeyDown(player)) {
				maxHoverY = -0.1f;
			}
			if (IC2.keyboard.isJumpKeyDown(player)) {
				maxHoverY = 0.1f;
			}
			if (player.motionY > (double) maxHoverY) {
				player.motionY = maxHoverY;
				if (prevmotion > player.motionY) {
					player.motionY = prevmotion;
				}
			}
		}
		double consume = 8.0;
		if (hoverMode) {
			consume = 10.0;
		}
		ElectricItem.manager.discharge(jetpack, consume, Integer.MAX_VALUE, true, false, false);
		player.fallDistance = 0.0f;
		player.distanceWalkedModified = 0.0f;
		IC2.platform.resetPlayerInAirTime(player);
		return true;
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

		return 1.1;
	}

	private double getBaseAbsorptionRatio() {
		return 0.4;
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

}
