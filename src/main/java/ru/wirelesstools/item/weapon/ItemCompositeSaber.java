package ru.wirelesstools.item.weapon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

import java.util.*;

public class ItemCompositeSaber extends ItemTool implements IElectricItem {

	private IIcon[] textures;
	public double maxenergy;
	protected int tier;
	private boolean isLooting5;
	private final double useamount;
	private final Set<Object> whitelist = new HashSet<>(Arrays.asList(new Object[] { Blocks.web, Material.plants,
			Material.vine, Material.coral, Material.leaves, Material.gourd }));

	public ItemCompositeSaber(String name, boolean isLootingV, double energymax, int tier, double useAmount) {
		super(0.0F, ToolMaterial.EMERALD, new HashSet());
		this.setUnlocalizedName(name);
		this.maxenergy = energymax;
		this.tier = tier;
		this.isLooting5 = isLootingV;
		this.useamount = useAmount;
		this.setMaxDamage(27);
		this.setCreativeTab(MainWI.tabwi);
	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
		info.add(StatCollector.translateToLocal("press.rmb.saber"));
		info.add(StatCollector.translateToLocal("item.lootsaber.energy.drain") + ": "
				+ String.valueOf((int) this.useamount) + " Eu");
	}

	public boolean canHarvestBlock(Block block, ItemStack stack) {
		Material material = block.getMaterial();
		if (this.whitelist.contains(block) && this.whitelist.contains(material)) {

			return true;
		}

		return super.canHarvestBlock(block, stack);
	}

	@SideOnly(value = Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {

		return this.isLooting5 ? EnumRarity.rare : EnumRarity.uncommon;
	}

	@SideOnly(value = Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
		ItemStack stack1 = new ItemStack(this);
		ItemStack stack2 = new ItemStack(this);
		ElectricItem.manager.charge(stack1, 0.0, Integer.MAX_VALUE, true, false);
		ElectricItem.manager.charge(stack2, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
		itemList.add(stack1);
		itemList.add(stack2);
	}

	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {

		return false;
	}

	public int getItemEnchantability() {

		return 0;
	}

	public boolean isRepairable() {

		return false;
	}

	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {

		return false;
	}

	@Override
	public boolean canProvideEnergy(ItemStack var1) {

		return false;
	}

	@Override
	public Item getChargedItem(ItemStack var1) {

		return this;
	}

	@Override
	public Item getEmptyItem(ItemStack var1) {

		return this;
	}

	@Override
	public double getMaxCharge(ItemStack var1) {

		return this.maxenergy;
	}

	@Override
	public int getTier(ItemStack var1) {

		return this.tier;
	}

	@Override
	public double getTransferLimit(ItemStack var1) {

		return 256.0;
	}

	@SideOnly(value = Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {

		return true;
	}

	public boolean isFull3D() {

		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityplayer) {
		if (!IC2.platform.isSimulating()) {
			return itemStack;
		}
		NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
		if (nbtData.getBoolean("active")) {
			nbtData.setBoolean("active", false);
			this.updateAttributes(nbtData, itemStack);
		} else if (ElectricItem.manager.canUse(itemStack, 4.0)) {
			nbtData.setBoolean("active", true);
			this.updateAttributes(nbtData, itemStack);
		}

		return super.onItemRightClick(itemStack, world, entityplayer);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
		NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
		if (!nbtData.getBoolean("active")) {

			return true;
		}

		if (IC2.platform.isSimulating()) {

			this.drainSaber(stack, this.useamount, source);
		}

		return true;
	}

	protected void drainSaber(ItemStack itemStack, double amount, EntityLivingBase entity) {
		if (!ElectricItem.manager.use(itemStack, amount, entity)) {
			NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
			nbtData.setBoolean("active", false);
			this.updateAttributes(nbtData, itemStack);
		}

	}

	private void updateAttributes(NBTTagCompound nbtData, ItemStack stack) {
		boolean active = nbtData.getBoolean("active");
		double damage = active ? 10.0 : (double) ToolMaterial.EMERALD.getDamageVsEntity();
		Map<Integer, Integer> enchantmentmapsaber = EnchantmentHelper.getEnchantments(stack);
		if (active) {
			if (this.isLooting5) {

				enchantmentmapsaber.put(Integer.valueOf(Enchantment.looting.effectId), Integer.valueOf(5));
			} else {

				enchantmentmapsaber.put(Integer.valueOf(Enchantment.looting.effectId), Integer.valueOf(3));
			}

			EnchantmentHelper.setEnchantments(enchantmentmapsaber, stack);

		} else {
			enchantmentmapsaber.remove(Integer.valueOf(Enchantment.looting.effectId));
			EnchantmentHelper.setEnchantments(enchantmentmapsaber, stack);
		}
		NBTTagCompound entry = new NBTTagCompound();
		entry.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		entry.setLong("UUIDMost", field_111210_e.getMostSignificantBits());
		entry.setLong("UUIDLeast", field_111210_e.getLeastSignificantBits());
		entry.setString("Name", "Tool modifier");
		entry.setDouble("Amount", damage);
		entry.setInteger("Operation", 0);
		NBTTagList list = new NBTTagList();
		list.appendTag(entry);
		nbtData.setTag("AttributeModifiers", list);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.textures = new IIcon[2];

		this.textures[0] = iconRegister.registerIcon(Reference.PathTex + "itemCompositeLootingSaber_off");
		this.textures[1] = iconRegister.registerIcon(Reference.PathTex + "itemCompositeLootingSaber_active");
	}

	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(ItemStack itemStack, int pass) {
		NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
		if (nbtData.getBoolean("active")) {

			return this.textures[1];
		}

		return this.textures[0];
	}

}
