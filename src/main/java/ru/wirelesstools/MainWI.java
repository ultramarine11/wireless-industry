package ru.wirelesstools;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gravisuite.GraviSuite;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import ru.wirelesstools.blocks.*;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.entityarrow.ArrowVampEUNew;
import ru.wirelesstools.entityarrow.ArrowVampXPNew;
import ru.wirelesstools.eventhandler.HandlerRegistry;
import ru.wirelesstools.fluidmachines.BlockExpGen;
import ru.wirelesstools.fluidmachines.ItemBlockEG;
import ru.wirelesstools.fluidmachines.TextureHooks;
import ru.wirelesstools.fluidmachines.TileXPGenPublic;
import ru.wirelesstools.handlerwireless.WirelessChargerHandler;
import ru.wirelesstools.handlerwireless.WirelessTransfer;
import ru.wirelesstools.handlerwireless.WirelessTransmitHandler;
import ru.wirelesstools.item.ItemEnderModule;
import ru.wirelesstools.item.ItemPlayerModule;
import ru.wirelesstools.item.ItemTransformerKit;
import ru.wirelesstools.item.ItemWirelessModule;
import ru.wirelesstools.item.armor.*;
import ru.wirelesstools.item.tool.ElectricDescaler;
import ru.wirelesstools.item.tool.LuckyVajra;
import ru.wirelesstools.item.weapon.ItemSaberLoot3;
import ru.wirelesstools.item.weapon.ItemSaberLoot5;
import ru.wirelesstools.item.weapon.VampireQuantumBow;
import ru.wirelesstools.itemblock.*;
import ru.wirelesstools.proxy.ClientProxy;
import ru.wirelesstools.proxy.ServerProxy;
import ru.wirelesstools.tiles.*;
import ru.wirelesstools.utils.RecipeUtil;

import java.io.File;

@Mod(modid = Reference.IDNAME, name = Reference.NAME_MOD, version = "0.7.9.6", dependencies = "required-after:IC2;after:OpenBlocks;after:GraviSuite;after:CoFHCore;after:DraconicEvolution")
public class MainWI {

    @SidedProxy(clientSide = "ru.wirelesstools.proxy.ClientProxy", serverSide = "ru.wirelesstools.proxy.ServerProxy")
    public static ServerProxy proxy;

    public static Item saber5;
    public static Item saber3;

    public static Item quantumVampBowEu;
    public static Item descaler;

    public static Item luckyVajra;
    public static Block iridMach;
    public static Item wirelessChestPlate;
    public static Item wirelessEuRfHelmet;
    public static Block pfpConverter;

    public static Item enderQuantumHelmet;
    public static Item enderQuantumChest;
    public static Item enderQuantumLegs;
    public static Item enderQuantumBoots;

    public static final EnumRarity RARITY_RF = EnumHelper.addRarity("RF Wirelessly", EnumChatFormatting.GOLD,
            "Wireless RF Transmission");
    public static final EnumRarity RARITY_EU = EnumHelper.addRarity("Vampire_EU", EnumChatFormatting.DARK_AQUA,
            "EU Vampire");
    public static final EnumRarity RARITY_CHESTPLATE_WIRELESS = EnumHelper.addRarity("EU Wireless", EnumChatFormatting.RED,
            "Wireless EU Transmission");

    public static Block blockxpsender;

    public static Item playermodule;

    public static Item transformkit_upgrade;
    public static Item transformkit_changeowner;

    public static Block blockvajracharger;
    public static Block blockwirelessreceiverpersonal;
    public static Block armorcharger;
    public static Block wirelessasppersonal;
    public static Block wirelesshsppersonal;
    public static Block wirelessuhsppersonal;
    public static Block wirelessqsppersonal;
    public static Block wirelessspsppersonal;
    public static Block wirelessprotonsppersonal;
    public static Block wirelesssingsppersonal;
    public static Block wirelessabssppersonal;
    public static Block wirelessphotonicsppersonal;
    public static Block wirelessneutronsppersonal;
    public static Block wirelessadronsppersonal;
    public static Block wirelessbarionsppersonal;

    public static Block blockwirelesschargerpublic;
    public static Block blockwirelesschargerprivate;

    public static Block blockcreativepedestal;

    public static Block wirelessmachinescharger;

    public static Block expgen;

    public static Block blockwirelessqgen;

    public static Block blockmattercollector;

    public static Item endermodule;
    public static Item wirelessmodule;
    public static TickHandlerWI th;
    public static CreativeTabWI tabwi = new CreativeTabWI();

    public static class FluidXP {
        public static Fluid xpJuice = new Fluid("xpjuicewv");
    }

    @Instance(Reference.IDNAME)
    public static MainWI instance = new MainWI();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigWI.loadConfigWI(event);

        WirelessTransfer.transmithandler = new WirelessTransmitHandler();
        WirelessTransfer.chargeplayerhandler = new WirelessChargerHandler();

        descaler = new ElectricDescaler();
        luckyVajra = new LuckyVajra(ToolMaterial.EMERALD);
        saber3 = new ItemSaberLoot3();
        saber5 = new ItemSaberLoot5();
        quantumVampBowEu = new VampireQuantumBow("qvampboweu");

        wirelessChestPlate = new QuantumChestplateWirelessCharge("wirelesschargingchestplate");
        wirelessEuRfHelmet = new ItemSolarWirelessEURFHelmet();


        enderQuantumHelmet = new QuantumEnderHelmet("eqHelmet");
        enderQuantumChest = new QuantumEnderChestplate("eqChestplate");
        enderQuantumLegs = new QuantumEnderLeggings("eqLeggings");
        enderQuantumBoots = new QuantumEnderBoots("eqBoots");

        wirelessmodule = new ItemWirelessModule();
        endermodule = new ItemEnderModule();
        playermodule = new ItemPlayerModule();

        transformkit_upgrade = new ItemTransformerKit(0);
        transformkit_changeowner = new ItemTransformerKit(1);

        blockvajracharger = new BlockVajraCharger("vajracharger", Material.rock);
        armorcharger = new BlockArmorCharger("creativearmorcharger", Material.rock);

        blockxpsender = new BlockXPSenderElectric("electricxpsender");

        wirelessasppersonal = new BlockWirelessASP("wirelessAdvancedPanelPersonal");
        wirelesshsppersonal = new BlockWirelessHSP("wirelessHybridPanelPersonal");
        wirelessuhsppersonal = new BlockWirelessUHSP("wirelessUltimatePanelPersonal");
        wirelessqsppersonal = new BlockWirelessQSP("wirelessQuantumPanelPersonal");
        wirelessspsppersonal = new BlockWirelessSpSPPersonal("wirelessSpectralPanelPersonal", Material.rock);
        wirelessprotonsppersonal = new BlockWirelessProtonSP("wirelessProtonPanelPersonal");
        wirelesssingsppersonal = new BlockWirelessSingSPPersonal("wirelessSingularPanelPersonal", Material.rock);
        wirelessabssppersonal = new BlockWirelessAbsSPPersonal("wirelessAbsorbtionPanelPersonal", Material.rock);
        wirelessphotonicsppersonal = new BlockWirelessPhSPPersonal("wirelessPhotonicPanelPersonal", Material.rock);
        wirelessneutronsppersonal = new BlockWirelessNeuSPPersonal("wirelessNeutronSPPersonal", Material.rock);
        wirelessadronsppersonal = new BlockWirelessAdronSP("wirelessAdronPanelPersonal");
        wirelessbarionsppersonal = new BlockWirelessBarionSP("wirelessBarionPanelPersonal");
        blockwirelessreceiverpersonal = new BlockWStoragePersonal("wirelessStoragePersonal1Tier", Material.rock);

        blockwirelesschargerpublic = new BlockWirelessChargerPublic("wirelesschargerpublic");
        blockwirelesschargerprivate = new BlockWirelessChargerPrivate("wirelesschargerprivate");

        blockcreativepedestal = new BlockCreativePedestal("creativepedestal", Material.rock);

        wirelessmachinescharger = new BlockMachinesCharger("machinescharger");

        blockwirelessqgen = new BlockWirelessQuantumGenerator("wirelessqgen");

        blockmattercollector = new BlockMatterCollector("mattercollector");

        expgen = new BlockExpGen("expGen", Material.rock);
        iridMach = new BlockIridiumMachine();

        pfpConverter = new BlockPFPConverter("pfpconverter");

        if(FluidRegistry.isFluidRegistered("xpjuice")) {
            FluidXP.xpJuice = FluidRegistry.getFluid("xpjuice");
        } else {
            FluidRegistry.registerFluid(FluidXP.xpJuice);
            FluidXP.xpJuice.setIcons(TextureHooks.Icons.xpJuiceStill, TextureHooks.Icons.xpJuiceFlowing);
        }

        GameRegistry.registerBlock(wirelessasppersonal, ItemBlockWASP.class, "WASPPersonal");
        GameRegistry.registerBlock(wirelesshsppersonal, ItemBlockWHSP.class, "WHSPPersonal");
        GameRegistry.registerBlock(wirelessuhsppersonal, ItemBlockWUHSP.class, "WUHSPPersonal");
        GameRegistry.registerBlock(wirelessqsppersonal, ItemBlockWQSP.class, "WQSPPersonal");
        GameRegistry.registerBlock(wirelessspsppersonal, ItemBlockWSpSPPersonal.class, "WSpSPPersonal");
        GameRegistry.registerBlock(wirelessprotonsppersonal, ItemBlockWProtonSP.class, "WProtonSPPersonal");
        GameRegistry.registerBlock(wirelesssingsppersonal, ItemBlockWSingSPPersonal.class, "WSingSPPersonal");
        GameRegistry.registerBlock(wirelessabssppersonal, ItemBlockWAbsSPPersonal.class, "WAbsSPPersonal");
        GameRegistry.registerBlock(wirelessphotonicsppersonal, ItemBlockWPhSPPersonal.class, "WPhSPPersonal");
        GameRegistry.registerBlock(wirelessneutronsppersonal, ItemBlockNeuSPPersonal.class, "WNeuSPPersonal");
        GameRegistry.registerBlock(wirelessbarionsppersonal, ItemBlockWBarSP.class, "WBarionSPPersonal");
        GameRegistry.registerBlock(wirelessadronsppersonal, ItemBlockWAdronSP.class, "WAdronSPPersonal");
        GameRegistry.registerBlock(blockwirelessreceiverpersonal, ItemBlockWirelessStoragePersonal.class,
                "WRes1Personal");

        GameRegistry.registerBlock(pfpConverter, ItemBlockPFPConverter.class, "PFPConverter");

        GameRegistry.registerItem(luckyVajra, "LuckyVajra");
        GameRegistry.registerItem(saber5, "LootSaber5");
        GameRegistry.registerItem(saber3, "LootSaber3");
        GameRegistry.registerItem(quantumVampBowEu, "QuantumVampBow");

        GameRegistry.registerItem(wirelessmodule, "WirelessModule");
        GameRegistry.registerItem(endermodule, "EnderModule");
        GameRegistry.registerItem(playermodule, "PlayerModule");

        GameRegistry.registerItem(transformkit_upgrade, "KitUpgradeModule");
        GameRegistry.registerItem(transformkit_changeowner, "KitChangeownerModule");

        GameRegistry.registerItem(descaler, "Electric Descaler");

        GameRegistry.registerItem(wirelessEuRfHelmet, "WirelessHelmet");

        GameRegistry.registerItem(wirelessChestPlate, "WirelessChestPlate");

        GameRegistry.registerItem(enderQuantumHelmet, "QuantumEnderHelmet");
        GameRegistry.registerItem(enderQuantumChest, "QuantumEnderChestplate");
        GameRegistry.registerItem(enderQuantumLegs, "QuantumEnderLeggings");
        GameRegistry.registerItem(enderQuantumBoots, "QuantumEnderBoots");

        GameRegistry.registerBlock(blockwirelesschargerpublic, ItemBlockWChPublic.class, "WChargerPublic");
        GameRegistry.registerBlock(blockwirelesschargerprivate, ItemBlockWChPrivate.class, "WChargerPrivate");

        GameRegistry.registerBlock(blockcreativepedestal, ItemBlockCreativePedestal.class, "CreativePedestal");

        GameRegistry.registerBlock(wirelessmachinescharger, ItemBlockMachCharger.class, "MachinesCharger");

        GameRegistry.registerBlock(blockwirelessqgen, ItemBlockWQGen.class, "WirelessQGen");
        GameRegistry.registerBlock(blockmattercollector, ItemBlockLiquidMatterCollector.class, "LiquidMatterCollector");

        GameRegistry.registerBlock(blockxpsender, ItemBlockXPSender.class, "ExpSender1");

        GameRegistry.registerBlock(expgen, ItemBlockEG.class, "ExpGen");
        GameRegistry.registerBlock(iridMach, ItBlIridMach.class, "IridMach");
        GameRegistry.registerBlock(blockvajracharger, ItemBlockVCh.class, "WCh");
        GameRegistry.registerBlock(armorcharger, ItemBlockArmorCharger.class, "ArCh");

        GameRegistry.registerTileEntity(TileVajraCharger.class, "TileVajraCharger");
        GameRegistry.registerTileEntity(TileXPGenPublic.class, "TileEG");

        GameRegistry.registerTileEntity(TileWirelessStoragePersonal1.class, "TileStorageWireless1Personal");

        GameRegistry.registerTileEntity(TileMachinesCharger.class, "TileEntityMachinesCharger");

        GameRegistry.registerTileEntity(WirelessQGen.class, "TileEntityWirelessQGen");

        GameRegistry.registerTileEntity(TileXPSender.class, "TileEntityXPSender");

        GameRegistry.registerTileEntity(TileLiquidMatterCollector.class, "TileEntityLiquidMatterCollector");

        GameRegistry.registerTileEntity(PFPConvertorTile.class, "TileEntityPFPConverter");

        GameRegistry.registerTileEntity(TileWirelessASP.class, "TileWirelessASPPersonal");
        GameRegistry.registerTileEntity(TileWirelessHSP.class, "TileWirelessHSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessUHSP.class, "TileWirelessUHSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessQSP.class, "TileWirelessQSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessSpSPPersonal.class, "TileWirelessSpectralSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessProtonSP.class, "TileWirelessProtonSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessSingSPPersonal.class, "TileWirelessSingularSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessDifractSPPersonal.class, "TileWirelessAbsSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessPhSPPersonal.class, "TileWirelessPhotonicSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessNeutronSPPersonal.class, "TileWirelessNeutronSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessBarionSP.class, "TileWirelessBarionSPPersonal");
        GameRegistry.registerTileEntity(TileWirelessAdronSP.class, "TileWirelessAdronSPPersonal");

        GameRegistry.registerTileEntity(TileEntityWirelessChargerPrivate.class, "TileWChargerPrivate");
        GameRegistry.registerTileEntity(TileEntityWirelessChargerPublic.class, "TileWChargerPublic");

        HandlerRegistry.register();

        proxy.initRecipes();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        proxy.serverStart(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        th = new TickHandlerWI();

        ServerProxy.Init();
        ClientProxy.Init();

        EntityRegistry.registerGlobalEntityID(ArrowVampEUNew.class, "ArrowVampEUNew",
                EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerGlobalEntityID(ArrowVampXPNew.class, "ArrowVampXPNew",
                EntityRegistry.findGlobalUniqueEntityId());
    }

    @EventHandler
    public void afterModsLoaded(FMLPostInitializationEvent event) {
        OreDictionary.registerOre("helmetwireless", wirelessEuRfHelmet);
        OreDictionary.registerOre("modulewirelessindustry", wirelessmodule);
        OreDictionary.registerOre("iridiummachinecasing", iridMach);
        OreDictionary.registerOre("luckyvajra", luckyVajra);
        OreDictionary.registerOre("saberloot3", saber3);
        OreDictionary.registerOre("saberloot5", saber5);
        OreDictionary.registerOre("chestplatewireless", wirelessChestPlate);
        OreDictionary.registerOre("vajrachargerblock", blockvajracharger);
        OreDictionary.registerOre("wirelessreceiverpersonal", blockwirelessreceiverpersonal);
        OreDictionary.registerOre("armorchargercreative", armorcharger);

        OreDictionary.registerOre("aspwireless", wirelessasppersonal);
        OreDictionary.registerOre("hspwireless", wirelesshsppersonal);
        OreDictionary.registerOre("uhspwireless", wirelessuhsppersonal);
        OreDictionary.registerOre("qspwireless", wirelessqsppersonal);
        OreDictionary.registerOre("spectralspwireless", wirelessspsppersonal);
        OreDictionary.registerOre("protonspwireless", wirelessprotonsppersonal);
        OreDictionary.registerOre("singularspwireless", wirelesssingsppersonal);
        OreDictionary.registerOre("difractspwireless", wirelessabssppersonal);
        OreDictionary.registerOre("photonicspwireless", wirelessphotonicsppersonal);
        OreDictionary.registerOre("neutronspwireless", wirelessneutronsppersonal);

        OreDictionary.registerOre("wirelesschargerpublic", blockwirelesschargerpublic);
        OreDictionary.registerOre("wirelesschargerprivate", blockwirelesschargerprivate);

        // if(!Loader.isModLoaded("industrialupgrade")) {

        GameRegistry.addRecipe(new ItemStack(wirelessEuRfHelmet, 1, OreDictionary.WILDCARD_VALUE), // OreDictionary.WILDCARD_VALUE
                new Object[] {"C C", "BAB", "C C", 'A',
                        RecipeUtil.copyWithWildCard(!OreDictionary.getOres("helmetSpectral").isEmpty()
                                ? OreDictionary.getOres("helmetSpectral").get(0)
                                : new ItemStack(IC2Items.getItem("quantumHelmet").getItem(), 1,
                                OreDictionary.WILDCARD_VALUE)),
                        'B', new ItemStack(wirelessmodule), 'C',
                        RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("lapotronCrystal").getItem(), 1,
                                OreDictionary.WILDCARD_VALUE))});

        GameRegistry.addRecipe(new ItemStack(wirelessasppersonal, 1),
                new Object[] {" A ", " B ", " C ", Character.valueOf('A'), new ItemStack(wirelessmodule),
                        Character.valueOf('B'), IC2Items.getItem("solarPanel"), Character.valueOf('C'),
                        new ItemStack(iridMach)});

        GameRegistry.addRecipe(new ItemStack(wirelesshsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessasppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessuhsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelesshsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessqsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessuhsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessspsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessqsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessprotonsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessspsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelesssingsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessprotonsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessabssppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelesssingsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessphotonicsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessabssppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessneutronsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessphotonicsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessbarionsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessneutronsppersonal)});

        GameRegistry.addRecipe(new ItemStack(wirelessadronsppersonal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), new ItemStack(wirelessbarionsppersonal)});

        GameRegistry.addRecipe(new ItemStack(pfpConverter, 1),
                new Object[] {"DCD",
                        "BAB",
                        "DCD", Character.valueOf('A'), IC2Items.getItem("replicator"),
                        Character.valueOf('B'), new ItemStack(iridMach),
                        Character.valueOf('C'), IC2Items.getItem("mfsUnit"),
                        Character.valueOf('D'), Items.nether_star});

        GameRegistry.addRecipe(new ItemStack(descaler, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"AAA",
                        "BCB",
                        " D ", 'A', IC2Items.getItem("rubber"),
                        'B', IC2Items.getItem("advancedAlloy"),
                        'C', RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("electricWrench").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                        'D', RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("reBattery").getItem(), 1, OreDictionary.WILDCARD_VALUE))});

        GameRegistry.addRecipe(new ItemStack(saber3, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"ACF", "ACF", "BDE", 'A', new ItemStack(Items.dye, 1, 4), 'B', Items.diamond, 'C',
                        IC2Items.getItem("advancedAlloy"), 'D', IC2Items.getItem("electronicCircuit"), 'E',
                        RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("reBattery").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                        'F', Items.redstone});

        GameRegistry.addRecipe(new ItemStack(saber5, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"ACF", "BDF", "AEG", 'A', Items.glowstone_dust,
                        'B', IC2Items.getItem("iridiumPlate"), 'C', Items.emerald, 'D',
                        RecipeUtil.copyWithWildCard(new ItemStack(saber3, 1, OreDictionary.WILDCARD_VALUE)), 'E',
                        IC2Items.getItem("advancedCircuit"), 'F', Items.diamond, 'G', RecipeUtil.copyWithWildCard(
                        new ItemStack(IC2Items.getItem("energyCrystal").getItem(), 1, OreDictionary.WILDCARD_VALUE))});

        // }

        GameRegistry.addRecipe(new ItemStack(iridMach, 1), new Object[] {" A ", "ABA", " A ", Character.valueOf('A'),
                IC2Items.getItem("iridiumPlate"), Character.valueOf('B'), IC2Items.getItem("advancedMachine")});

        GameRegistry.addRecipe(new ItemStack(blockvajracharger, 1),
                new Object[] {" A ", "BCB", " A ", Character.valueOf('A'), IC2Items.getItem("advancedMachine"),
                        Character.valueOf('B'), Blocks.redstone_block, Character.valueOf('C'),
                        !OreDictionary.getOres("storageMFSU").isEmpty() ? OreDictionary.getOres("storageMFSU").get(0)
                                : IC2Items.getItem("mfsUnit")});

        GameRegistry.addRecipe(new ItemStack(blockwirelesschargerpublic, 1),
                new Object[] {" A ", "BCB", " A ", Character.valueOf('A'), Items.ender_pearl, Character.valueOf('B'),
                        new ItemStack(wirelessmodule), Character.valueOf('C'), new ItemStack(iridMach)});

        GameRegistry.addShapelessRecipe(new ItemStack(blockwirelesschargerprivate, 1),
                new ItemStack(blockwirelesschargerpublic), Items.book, Blocks.emerald_block);

        // TODO подумать над интеграцией с industrial upgrade
        if(Loader.isModLoaded("GraviSuite")) {
            GameRegistry.addRecipe(new ItemStack(wirelessChestPlate, 1, OreDictionary.WILDCARD_VALUE),
                    new Object[] {"C C", "BAB", "C C", 'A',
                            RecipeUtil.copyWithWildCard(
                                    new ItemStack(GraviSuite.graviChestPlate, 1, OreDictionary.WILDCARD_VALUE)),
                            'B', new ItemStack(wirelessmodule), 'C',
                            RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("lapotronCrystal").getItem(), 1,
                                    OreDictionary.WILDCARD_VALUE))});

            GameRegistry.addRecipe(new ItemStack(luckyVajra, 1, OreDictionary.WILDCARD_VALUE),
                    new Object[] {"ABA", "CDE", "FBF", 'A', IC2Items.getItem("iridiumPlate"), 'B',
                            RecipeUtil.copyWithWildCard(new ItemStack(
                                    IC2Items.getItem("iridiumDrill").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                            'C', Blocks.lapis_block, 'D',
                            RecipeUtil.copyWithWildCard(new ItemStack(GraviSuite.vajra, 1,
                                    OreDictionary.WILDCARD_VALUE)),
                            'E', Blocks.emerald_block, 'F', IC2Items.getItem("advancedCircuit")});
        } else {
            GameRegistry.addRecipe(new ItemStack(wirelessChestPlate, 1, OreDictionary.WILDCARD_VALUE),
                    new Object[] {"C C", "BAB", "C C", 'A',
                            RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("quantumBodyarmor").getItem(), 1,
                                    OreDictionary.WILDCARD_VALUE)),
                            'B', new ItemStack(wirelessmodule), 'C',
                            RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("lapotronCrystal").getItem(), 1,
                                    OreDictionary.WILDCARD_VALUE))});

            GameRegistry.addRecipe(new ItemStack(luckyVajra, 1, OreDictionary.WILDCARD_VALUE),
                    new Object[] {"ABA", "CDE", "FBF", 'A', IC2Items.getItem("iridiumPlate"), 'B',
                            RecipeUtil.copyWithWildCard(new ItemStack(
                                    IC2Items.getItem("miningLaser").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                            'C', Blocks.lapis_block, 'D',
                            RecipeUtil.copyWithWildCard(new ItemStack(IC2Items.getItem("iridiumDrill").getItem(), 1,
                                    OreDictionary.WILDCARD_VALUE)),
                            'E', Blocks.emerald_block, 'F', IC2Items.getItem("advancedCircuit")});
        }

        GameRegistry.addRecipe(new ItemStack(wirelessmodule, 1),
                new Object[] {"ABA", "CDC", "ABA", Character.valueOf('A'), IC2Items.getItem("iridiumOre"),
                        Character.valueOf('B'), Blocks.redstone_block, Character.valueOf('C'),
                        IC2Items.getItem("advancedCircuit"), Character.valueOf('D'), Items.ender_pearl});

        GameRegistry.addRecipe(new ItemStack(endermodule, 1),
                new Object[] {"DAD", "BCB", "DAD", Character.valueOf('A'), Items.ender_pearl, Character.valueOf('B'),
                        IC2Items.getItem("advancedCircuit"), Character.valueOf('C'),
                        IC2Items.getItem("iridiumPlate"), Character.valueOf('D'), Items.redstone});

        GameRegistry.addRecipe(new ItemStack(transformkit_upgrade, 1),
                new Object[] {" A ", "ABA", " A ",
                        Character.valueOf('A'), IC2Items.getItem("wrench"),
                        Character.valueOf('B'), IC2Items.getItem("iridiumPlate")});

        GameRegistry.addRecipe(new ItemStack(blockmattercollector, 1),/////////////////
                new Object[] {" A ", "ABA", " A ",
                        Character.valueOf('A'), IC2Items.getItem("cell"),
                        Character.valueOf('B'), IC2Items.getItem("massFabricator")});

        GameRegistry.addRecipe(new ItemStack(playermodule, 1),
                new Object[] {" A ", "BCB", " A ", Character.valueOf('A'), Items.paper, Character.valueOf('B'),
                        IC2Items.getItem("advancedCircuit"), Character.valueOf('C'),
                        IC2Items.getItem("advancedAlloy")});

        GameRegistry.addRecipe(new ItemStack(quantumVampBowEu, 1),
                new Object[] {" A ", "BCB", " A ", Character.valueOf('A'), IC2Items.getItem("advancedCircuit"),
                        Character.valueOf('B'), new ItemStack(endermodule), Character.valueOf('C'), Items.bow});

        GameRegistry.addRecipe(new ItemStack(enderQuantumHelmet, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"   ", "BAB", "   ", Character.valueOf('A'), RecipeUtil.copyWithWildCard(
                        new ItemStack(IC2Items.getItem("quantumHelmet").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                        Character.valueOf('B'), new ItemStack(endermodule)});

        GameRegistry.addRecipe(new ItemStack(enderQuantumChest, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"   ", "BAB", "   ", Character.valueOf('A'), RecipeUtil.copyWithWildCard(
                        new ItemStack(IC2Items.getItem("quantumBodyarmor").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                        Character.valueOf('B'), new ItemStack(endermodule)});

        GameRegistry.addRecipe(new ItemStack(enderQuantumLegs, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"   ", "BAB", "   ", Character.valueOf('A'), RecipeUtil.copyWithWildCard(
                        new ItemStack(IC2Items.getItem("quantumLeggings").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                        Character.valueOf('B'), new ItemStack(endermodule)});

        GameRegistry.addRecipe(new ItemStack(enderQuantumBoots, 1, OreDictionary.WILDCARD_VALUE),
                new Object[] {"   ", "BAB", "   ", Character.valueOf('A'), RecipeUtil.copyWithWildCard(
                        new ItemStack(IC2Items.getItem("quantumBoots").getItem(), 1, OreDictionary.WILDCARD_VALUE)),
                        Character.valueOf('B'), new ItemStack(endermodule)});

        GameRegistry.addRecipe(new ItemStack(blockwirelessreceiverpersonal, 1),
                new Object[] {"A A", "CDC", "A A", Character.valueOf('A'), new ItemStack(wirelessmodule),
                        Character.valueOf('C'), IC2Items.getItem("glassFiberCableItem"), Character.valueOf('D'),
                        !OreDictionary.getOres("storageMFSU").isEmpty() ? OreDictionary.getOres("storageMFSU").get(0)
                                : IC2Items.getItem("mfsUnit")});

        GameRegistry.addRecipe(new ItemStack(wirelessmachinescharger, 1),
                new Object[] {"AAA", "ABA", "AAA", Character.valueOf('A'), new ItemStack(wirelessmodule),
                        Character.valueOf('B'),
                        !OreDictionary.getOres("storageMFSU").isEmpty() ? OreDictionary.getOres("storageMFSU").get(0)
                                : IC2Items.getItem("mfsUnit")});

        GameRegistry.addRecipe(new ItemStack(expgen, 1),
                new Object[] {"BAB", "BCB", "BAB", Character.valueOf('A'), new ItemStack(iridMach),
                        Character.valueOf('B'), IC2Items.getItem("cell"), Character.valueOf('C'),
                        !OreDictionary.getOres("mechanismAdvMatter").isEmpty()
                                ? OreDictionary.getOres("mechanismAdvMatter").get(0)
                                : IC2Items.getItem("massFabricator")});
        GameRegistry.addRecipe(new ItemStack(blockxpsender, 1),////////
                new Object[] {"BAB", "BCB", "BAB", Character.valueOf('A'), new ItemStack(expgen),
                        Character.valueOf('B'), Blocks.redstone_block, Character.valueOf('C'),
                        !OreDictionary.getOres("mechanismAdvMatter").isEmpty()
                                ? OreDictionary.getOres("mechanismAdvMatter").get(0)
                                : IC2Items.getItem("teleporter")});

        GameRegistry.addRecipe(new ItemStack(blockcreativepedestal, 1),
                new Object[] {" A ", "AAA", " A ", Character.valueOf('A'), Blocks.bedrock});

    }

}
