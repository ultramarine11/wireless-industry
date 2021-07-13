package ru.wirelesstools.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.tiles.TileVajraCharger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LuckyVajra extends ItemTool implements IElectricItem {

	public double maxCharge;
	protected int tier;
	protected int energyPerOperation;
	protected double transferLimit;

	public LuckyVajra(ToolMaterial material) {
		super(0.0F, material, new HashSet());
		this.setUnlocalizedName("wirelessvajra");
		this.setTextureName(Reference.PathTex + "itemVajraLucky");
		this.setCreativeTab(MainWI.tabwi);
		this.tier = 3;
		this.maxCharge = ConfigWI.maxVajraCharge;
		this.transferLimit = 1000000.0D;
		this.efficiencyOnProperMaterial = 20000F; //effective power
		this.energyPerOperation = ConfigWI.vajraEnergyPerOperation;
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

	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k,
			int side, float a, float b, float c) {
		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(i, j, k);
			if (te instanceof TileVajraCharger) {
				TileVajraCharger tilewch = (TileVajraCharger) te;
				if (tilewch.getStored() > 0) {
					int sentEnergy = (int) ElectricItem.manager.charge(itemstack, tilewch.getStored(),
							Integer.MAX_VALUE, false, false);
					tilewch.addEnergy(-sentEnergy);
					entityplayer.addChatMessage(new ChatComponentTranslation(
							EnumChatFormatting.BLUE + StatCollector.translateToLocal("chat.message.luckyvajra.charged")
									+ " " + String.valueOf(sentEnergy) + " EU"));
				}
				return true;
			}
			return false;
		}
		return false;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		if (!world.isRemote) {
			if (player.isSneaking()) {
				boolean modenew = !nbt.getBoolean("vajramode");
				nbt.setBoolean("vajramode", modenew);
				Map<Integer, Integer> enchantmentMaplocal = EnchantmentHelper.getEnchantments(stack);
				if (modenew) {
					enchantmentMaplocal.put(Integer.valueOf(Enchantment.fortune.effectId), Integer.valueOf(5));
					EnchantmentHelper.setEnchantments(enchantmentMaplocal, stack);
					player.addChatMessage(new ChatComponentTranslation(
							EnumChatFormatting.AQUA + StatCollector.translateToLocal("chat.message.fortune.active")));
				} else {
					enchantmentMaplocal.remove(Integer.valueOf(Enchantment.fortune.effectId));
					EnchantmentHelper.setEnchantments(enchantmentMaplocal, stack);
					player.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.DARK_PURPLE
							+ StatCollector.translateToLocal("chat.message.fortune.none")));
				}
			}
		}
		return stack;
	}

	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker) {
		if (ElectricItem.manager.use(itemstack, (this.energyPerOperation * 2), attacker)) {

			entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 25.0F);
		} else {

			entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 1.0F);
		}

		return false;
	}

	public boolean canHarvestBlock(Block block, ItemStack stack) {

		return block != Blocks.bedrock;
	}

	public float getDigSpeed(ItemStack tool, Block block, int meta) {
		if (!ElectricItem.manager.canUse(tool, this.energyPerOperation)) {
			return 1.0F;
		}

		if (canHarvestBlock(block, tool)) {
			return this.efficiencyOnProperMaterial;
		}

		return 1.0F;
	}

	public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int xPos, int yPos, int zPos,
			EntityLivingBase entityliving) {
		if (block.getBlockHardness(world, xPos, yPos, zPos) != 0.0D) {
			if (entityliving != null) {
				ElectricItem.manager.use(itemstack, this.energyPerOperation, entityliving);
			} else {
				ElectricItem.manager.discharge(itemstack, this.energyPerOperation, this.tier, true, false, false);
			}
		}
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

	@SideOnly(value = Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			info.add(I18n.format("about.ench1") + " " + EnumChatFormatting.AQUA
					+ I18n.format("tooltip.fortune"));
			info.add(I18n.format("about.ench2"));
		} else {
			info.add(EnumChatFormatting.ITALIC + I18n.format("press.lshift"));
		}
	}

	protected ItemStack getItemStack(double charge) {
		ItemStack st = new ItemStack(this);
		ElectricItem.manager.charge(st, charge, 2147483647, true, false);

		return st;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List itemList) {
		itemList.add(getItemStack(Double.POSITIVE_INFINITY));
		itemList.add(getItemStack(0.0D));
	}

}
