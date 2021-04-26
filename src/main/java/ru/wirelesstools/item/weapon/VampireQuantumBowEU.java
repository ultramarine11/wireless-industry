package ru.wirelesstools.item.weapon;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.entity.arrow.ArrowVampEUNew;

public class VampireQuantumBowEU extends ItemBow implements IElectricItem {

	private IIcon[] qbowEU = new IIcon[4];

	protected double maxenergy;
	protected int tier;
	protected double transferlimit;

	public VampireQuantumBowEU(String name) {
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setMaxDamage(27);
		this.setCreativeTab(MainWI.tabwi);
		this.maxenergy = 400000.0D;
		this.tier = 3;
		this.transferlimit = 40000.0;
	}

	@SideOnly(value = Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {

		return true;
	}

	@SideOnly(value = Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List info, boolean par4) {
		info.add(StatCollector.translateToLocal("info.qboweu.about1"));
		info.add(StatCollector.translateToLocal("info.qboweu.about2"));
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		this.qbowEU[0] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_standby");
		this.qbowEU[1] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_0");
		this.qbowEU[2] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_1");
		this.qbowEU[3] = reg.registerIcon(Reference.PathTex + "quantumbow_eu_steal_2");
	}

	public EnumAction getItemUseAction(ItemStack par1ItemStack) {

		return EnumAction.bow;
	}

	// 72000 тиков = 3600 секунд = 1 час можно держать лук заряженным
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {

		return 72000;
	}

	public ItemStack onFoodEaten(ItemStack stack, World par2World, EntityPlayer par3EntityPlayer) {

		return stack;
	}

	public boolean onBlockDestroyed(ItemStack par1ItemStack, World world, Block block, int par4, int par5, int par6,
			EntityLivingBase par7EntityLiving) {

		return true;
	}

	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int n) {

		return this.qbowEU[0];
	}

	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		if (usingItem != null
				&& Item.getIdFromItem(usingItem.getItem()) == Item.getIdFromItem(MainWI.quantumVampBowEu)) {
			int k = this.getMaxItemUseDuration(stack) - useRemaining;
			if (k > 17) {

				return this.qbowEU[3];
			}

			if (k > 13) {

				return this.qbowEU[2];
			}

			if (k > 0) {

				return this.qbowEU[1];
			}
		}

		return this.qbowEU[0];
	}

	public EnumRarity getRarity(ItemStack itemstack) {

		return EnumRarity.rare;
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
		ItemStack chargedbow = new ItemStack(this, 1);
		ElectricItem.manager.charge(chargedbow, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
		itemList.add(chargedbow);
		ItemStack dischargedbow = new ItemStack(this, 1);
		ElectricItem.manager.charge(dischargedbow, 0.0, Integer.MAX_VALUE, true, false);
		itemList.add(dischargedbow);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!(player.capabilities.isCreativeMode) && !(ElectricItem.manager.canUse(itemStack, 400.0D))) {

			return itemStack;
		}
		ArrowNockEvent event = new ArrowNockEvent(player, itemStack);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.result;
		}

		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));

		return itemStack;
	}

	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount) {
		int inUseDuration = this.getMaxItemUseDuration(itemStack) - itemInUseCount;
		ArrowLooseEvent event = new ArrowLooseEvent(player, itemStack, inUseDuration);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}

		inUseDuration = event.charge;
		float inUseSeconds = (float) inUseDuration / 20.0F;
		inUseSeconds = (inUseSeconds * inUseSeconds + inUseSeconds * 2.0F) / 3.0F;

		if (inUseSeconds < 0.1F) {
			return;
		}

		if (inUseSeconds > 1.0F) {
			inUseSeconds = 1.0F;
		}

		ArrowVampEUNew arrowvampeuNew = new ArrowVampEUNew(world, player, inUseSeconds * 2.0F);

//		ArrowVampEU arrowvampeu = new ArrowVampEU(world, player, inUseSeconds * 2.0F);

		if (inUseSeconds == 1.0F) {
			arrowvampeuNew.setIsCritical(true);
		}

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

			ElectricItem.manager.use(itemStack, 1000.0, player);
			world.spawnEntityInWorld(arrowvampeuNew);
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
