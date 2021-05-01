package ru.wirelesstools.item.armor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.utils.MiscUtils;

public class QuantumChestplateWirelessCharge extends ItemArmor implements IElectricItem, IMetalArmor, ISpecialArmor {

	public double maxCharge;
	protected double transferLimit;
	protected int tier;

	public QuantumChestplateWirelessCharge(String name) {
		super(ArmorMaterial.DIAMOND, 0, 1);
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.maxCharge = 500000000.0D;
		this.transferLimit = 500000.0D;
		this.tier = 4;
		this.setMaxDamage(27);
		this.setCreativeTab(MainWI.tabwi);
	}

	public EnumRarity getRarity(ItemStack itemstack) {

		return EnumRarity.epic;
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		boolean active = nbt.getBoolean("active");
		byte toggleTimer = nbt.getByte("toggleTimer");
		if (IC2.keyboard.isModeSwitchKeyDown(player) && !player.isSneaking() && toggleTimer == 0) {
			toggleTimer = 10;
			active = !active;
			if (!world.isRemote) {
				nbt.setBoolean("active", active);
				if (active) {
					player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.DARK_GREEN
							+ StatCollector.translateToLocal("chat.message.wirelesscharge.on"), new Object[0]));
				} else {
					player.addChatMessage(new ChatComponentTranslation(
							EnumChatFormatting.DARK_RED
									+ StatCollector.translateToLocal("chat.message.wirelesscharge.off"),
							new Object[0]));
				}
			}
		}

		if (!world.isRemote) {
			if (active)
				this.checkPlayers(player, world, stack);

			boolean jetpack = nbt.getBoolean("jetpack");
			boolean hoverMode = nbt.getBoolean("hoverMode");
			boolean jetpackUsed = false;
			if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
				toggleTimer = 10;
				hoverMode = !hoverMode;
				nbt.setBoolean("hoverMode", hoverMode);
				if (hoverMode) {
					IC2.platform.messagePlayer(player, "Quantum Hover Mode enabled.", new Object[0]);
				} else {
					IC2.platform.messagePlayer(player, "Quantum Hover Mode disabled.", new Object[0]);
				}

			}

			if (IC2.keyboard.isBoostKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
				toggleTimer = 10;
				jetpack = !jetpack;
				nbt.setBoolean("jetpack", jetpack);
				if (jetpack) {
					IC2.platform.messagePlayer(player, "Quantum Jetpack enabled.", new Object[0]);
				} else {
					IC2.platform.messagePlayer(player, "Quantum Jetpack disabled.", new Object[0]);
				}

			}

			if (jetpack && (IC2.keyboard.isJumpKeyDown(player) || hoverMode && player.motionY < -0.03)) {
				jetpackUsed = this.useJetpack(player, hoverMode);
			}

			if (jetpackUsed) {

				player.inventoryContainer.detectAndSendChanges();
			}
		}

		if (IC2.platform.isSimulating() && toggleTimer > 0) {
			toggleTimer--;
			nbt.setByte("toggleTimer", toggleTimer);
		}

		if (player.isBurning())
			player.extinguish();

	}

	protected void checkPlayers(EntityPlayer player, World world, ItemStack thisarmor) {
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(player.posX - 15.0, player.posY - 15.0,
				player.posZ - 15.0, player.posX + 15.0, player.posY + 15.0, player.posZ + 15.0);
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, axisalignedbb);
		for (Entity entityinlist : list) {
			if (entityinlist instanceof EntityPlayer) {
				EntityPlayer player1 = (EntityPlayer) entityinlist;
				if (player1 != null) {
					this.checkInvPlayer(player1, thisarmor);
				}
			}
		}
	}

	protected void checkInvPlayer(EntityPlayer player, ItemStack thisarmor) {
		for (ItemStack current : player.inventory.armorInventory) {
			if (current == null)
				continue;
			if (current.getItem() instanceof QuantumChestplateWirelessCharge)
				continue;
			if (current.getItem() instanceof IElectricItem)
				MiscUtils.chargeEUItemFromArmor(current, thisarmor);
		}

		for (ItemStack current : player.inventory.mainInventory) {
			if (current == null)
				continue;
			if (current.getItem() instanceof IElectricItem)
				MiscUtils.chargeEUItemFromArmor(current, thisarmor);
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

	@SideOnly(value = Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		String isonoff = nbt.getBoolean("active") ? "info.yes" : "info.no";
		list.add(StatCollector.translateToLocal("info.wirelesscharge.mode") + ": "
				+ StatCollector.translateToLocal(isonoff));
		list.add(StatCollector.translateToLocal("info.wirelesscharge.about"));
		list.add(StatCollector.translateToLocal("info.wirelesscharge.radius") + ": " + String.valueOf(15) + " "
				+ StatCollector.translateToLocal("info.wirelesscharge.blocks"));
		list.add(StatCollector.translateToLocal("info.equip.and.press.key") + " " + "IC2 Mode Switch Key" + " "
				+ StatCollector.translateToLocal("info.to.switch"));
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