package ru.wirelesstools.config;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import ru.wirelesstools.LogHelperWI;
import ru.wirelesstools.Reference;

import java.io.File;

public class ConfigWI {

    public static int waspgenday;
    public static int waspgennight;
    public static int waspoutput;
    public static int wasptier;
    public static int waspstorage;
    public static int wasptransfer;

    public static int whspgenday;
    public static int whspgennight;
    public static int whspoutput;
    public static int whsptier;
    public static int whspstorage;
    public static int whsptransfer;

    public static int wuhspgenday;
    public static int wuhspgennight;
    public static int wuhspoutput;
    public static int wuhsptier;
    public static int wuhspstorage;
    public static int wuhsptransfer;

    public static int wqspgenday;
    public static int wqspgennight;
    public static int wqspoutput;
    public static int wqsptier;
    public static int wqspstorage;
    public static int wqsptransfer;

    public static int wspspgenday;
    public static int wspspgennight;
    public static int wspspoutput;
    public static int wspsptier;
    public static int wspspstorage;
    public static int wspsptransfer;

    public static int wpspgenday;
    public static int wpspgennight;
    public static int wpspoutput;
    public static int wpsptier;
    public static int wpspstorage;
    public static int wpsptransfer;

    public static int wsingspgenday;
    public static int wsingspgennight;
    public static int wsingspoutput;
    public static int wsingsptier;
    public static int wsingspstorage;
    public static int wsingsptransfer;

    public static int wdifrspgenday;
    public static int wdifrspgennight;
    public static int wdifrspoutput;
    public static int wdifrsptier;
    public static int wdifrspstorage;
    public static int wdifrsptransfer;

    public static int wphspgenday;
    public static int wphspgennight;
    public static int wphspoutput;
    public static int wphsptier;
    public static int wphspstorage;
    public static int wphsptransfer;

    public static int wneuspgenday;
    public static int wneuspgennight;
    public static int wneuspoutput;
    public static int wneusptier;
    public static int wneuspstorage;
    public static int wneusptransfer;

    public static int wadronspgenday;
    public static int wadronspgennight;
    public static int wadronspoutput;
    public static int wadronsptier;
    public static int wadronspstorage;
    public static int wadronsptransfer;

    public static int wbarionspgenday;
    public static int wbarionspgennight;
    public static int wbarionspoutput;
    public static int wbarionsptier;
    public static int wbarionspstorage;
    public static int wbarionsptransfer;

    public static int wstorageoutput;
    public static int wstoragemaxstorage;
    public static int wstoragetier;

    public static int maxstorageofchargers;
    public static int tierofchargers;

    public static int enderChargeArmorValue;

    public static int EuRfSolarHelmetGenDay;
    public static int EuRfSolarHelmetGenNight;

    public static int EUToRF_Multiplier;

    public static int stolenEnergyEUFromArmor;

    public static int wirelessqgentier;
    public static int wirelessqgenoutput;
    public static int wirelessqgentransfer;

    public static int secondsXPSender;
    public static int amountXPsent;
    public static int maxstorageXPSender;
    public static int energyperxppointXPSender;
    public static int tierXPSender;

    public static int vampBowShotEnergyCost;
    public static int vampBowMaxCharge;
    public static int vampBowXPVampiredAmount;

    public static int maxVajraCharge;
    public static int vajraEnergyPerOperation;
    public static int vajraFortuneEnchantmentlevel;

    public static int helmetChargingRadius;
    public static int chestplateChargingRadius;

    public static boolean isServerJoinedChatMsgEnabled;
    public static boolean isModLogEnabled;
    public static boolean isIU_EuRf_priority;
    public static boolean enableWeaponsChatMsgs;
    public static boolean enableWICommands;

    public static int permissionLevelCommandChangeOwner;
    public static int permissionLevelCommandClearOwner;

    public static double machinesChargerMaxEnergyDouble;
    public static int machinesChargerMaxEnergyRF;
    public static int machinesChargerTier;

    public static int matterCollectorCapacity;

    public static int vajraChargerMaxStorage;
    public static int tierVajraCharger;
    public static boolean enableVajraDischargingAfterEnchant;
    public static int vajraEnchantDischargeEnergyAmount;

    public static void loadConfigWI(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        int EuRfCrossConfig = 0;
        int EuRf_WI_Config;
        try {
            config.load();
            EuRf_WI_Config = config.get(categoryENERGYBALANCE, "Eu-Rf Multiplier", 4, "EU to RF multiplier related to WI mod, not less than 1", 1, Integer.MAX_VALUE).getInt(4);
            ConfigWI.EuRfSolarHelmetGenDay = config.get(categoryTOOLS_ARMOR, "Day Generation", 4096, "Wireless Helmet day generation (EU/t)").getInt(4096);
            ConfigWI.EuRfSolarHelmetGenNight = config.get(categoryTOOLS_ARMOR, "Night Generation", 1024, "Wireless Helmet night generation (EU/t)")
                    .getInt(1024);

            int helmetChargingRadiuslocal = config.get(categoryTOOLS_ARMOR, "Helmet radius", 15, "Wireless Helmet charging radius (blocks)").getInt(15);
            ConfigWI.helmetChargingRadius = Math.max(1, helmetChargingRadiuslocal);
            int chestplateChargingRadiuslocal = config.get(categoryTOOLS_ARMOR, "Chestplate radius", 15, "Wireless Chestplate charging radius (blocks)").getInt(15);
            ConfigWI.chestplateChargingRadius = Math.max(1, chestplateChargingRadiuslocal);

            ConfigWI.waspgenday = config.get("Advanced Solar", "Day Generation", 10).getInt(10);
            ConfigWI.waspgennight = config.get("Advanced Solar", "Night Generation", 5).getInt(5);
            ConfigWI.waspoutput = config.get("Advanced Solar", "Output", 20).getInt(20);
            ConfigWI.wasptier = config.get("Advanced Solar", "Tier", 1).getInt(1);
            ConfigWI.waspstorage = config.get("Advanced Solar", "Storage", 3200).getInt(3200);
            ConfigWI.wasptransfer = config.get("Advanced Solar", "Wireless Transfer", 10).getInt(10);

            ConfigWI.whspgenday = config.get("Hybrid Solar", "Day Generation", 50).getInt(50);
            ConfigWI.whspgennight = config.get("Hybrid Solar", "Night Generation", 25).getInt(25);
            ConfigWI.whspoutput = config.get("Hybrid Solar", "Output", 100).getInt(100);
            ConfigWI.whsptier = config.get("Hybrid Solar", "Tier", 2).getInt(2);
            ConfigWI.whspstorage = config.get("Hybrid Solar", "Storage", 20000).getInt(20000);
            ConfigWI.whsptransfer = config.get("Hybrid Solar", "Wireless Transfer", 50).getInt(50);

            ConfigWI.wuhspgenday = config.get("Ultimate Solar", "Day Generation", 250).getInt(250);
            ConfigWI.wuhspgennight = config.get("Ultimate Solar", "Night Generation", 125).getInt(125);
            ConfigWI.wuhspoutput = config.get("Ultimate Solar", "Output", 500).getInt(500);
            ConfigWI.wuhsptier = config.get("Ultimate Solar", "Tier", 3).getInt(3);
            ConfigWI.wuhspstorage = config.get("Ultimate Solar", "Storage", 200000).getInt(200000);
            ConfigWI.wuhsptransfer = config.get("Ultimate Solar", "Wireless Transfer", 250).getInt(250);

            ConfigWI.wqspgenday = config.get("Quantum Solar", "Day Generation", 1250).getInt(1250);
            ConfigWI.wqspgennight = config.get("Quantum Solar", "Night Generation", 625).getInt(625);
            ConfigWI.wqspoutput = config.get("Quantum Solar", "Output", 2500).getInt(2500);
            ConfigWI.wqsptier = config.get("Quantum Solar", "Tier", 4).getInt(4);
            ConfigWI.wqspstorage = config.get("Quantum Solar", "Storage", 1000000).getInt(1000000);
            ConfigWI.wqsptransfer = config.get("Quantum Solar", "Wireless Transfer", 1250).getInt(1250);

            ConfigWI.wspspgenday = config.get("Spectral Solar", "Day Generation", 6250).getInt(6250);
            ConfigWI.wspspgennight = config.get("Spectral Solar", "Night Generation", 3125).getInt(3125);
            ConfigWI.wspspoutput = config.get("Spectral Solar", "Output", 12500).getInt(12500);
            ConfigWI.wspsptier = config.get("Spectral Solar", "Tier", 5).getInt(5);
            ConfigWI.wspspstorage = config.get("Spectral Solar", "Storage", 5000000).getInt(5000000);
            ConfigWI.wspsptransfer = config.get("Spectral Solar", "Wireless Transfer", 6250).getInt(6250);

            ConfigWI.wpspgenday = config.get("Proton Solar", "Day Generation", 31250).getInt(31250);
            ConfigWI.wpspgennight = config.get("Proton Solar", "Night Generation", 15625).getInt(15625);
            ConfigWI.wpspoutput = config.get("Proton Solar", "Output", 62500).getInt(62500);
            ConfigWI.wpsptier = config.get("Proton Solar", "Tier", 6).getInt(6);
            ConfigWI.wpspstorage = config.get("Proton Solar", "Storage", 50000000).getInt(50000000);
            ConfigWI.wpsptransfer = config.get("Proton Solar", "Wireless Transfer", 31250).getInt(31250);

            ConfigWI.wsingspgenday = config.get("Singular Solar", "Day Generation", 156250).getInt(156250);
            ConfigWI.wsingspgennight = config.get("Singular Solar", "Night Generation", 78125).getInt(78125);
            ConfigWI.wsingspoutput = config.get("Singular Solar", "Output", 312500).getInt(312500);
            ConfigWI.wsingsptier = config.get("Singular Solar", "Tier", 7).getInt(7);
            ConfigWI.wsingspstorage = config.get("Singular Solar", "Storage", 100000000).getInt(100000000);
            ConfigWI.wsingsptransfer = config.get("Singular Solar", "Wireless Transfer", 156250).getInt(156250);

            ConfigWI.wdifrspgenday = config.get("Diffraction Solar", "Day Generation", 781250).getInt(781250);
            ConfigWI.wdifrspgennight = config.get("Diffraction Solar", "Night Generation", 781250).getInt(781250);
            ConfigWI.wdifrspoutput = config.get("Diffraction Solar", "Output", 1562500).getInt(1562500);
            ConfigWI.wdifrsptier = config.get("Diffraction Solar", "Tier", 8).getInt(8);
            ConfigWI.wdifrspstorage = config.get("Diffraction Solar", "Storage", 400000000).getInt(400000000);
            ConfigWI.wdifrsptransfer = config.get("Diffraction Solar", "Wireless Transfer", 781250).getInt(781250);

            ConfigWI.wphspgenday = config.get("Photon Solar", "Day Generation", 3906250).getInt(3906250);
            ConfigWI.wphspgennight = config.get("Photon Solar", "Night Generation", 3906250).getInt(3906250);
            ConfigWI.wphspoutput = config.get("Photon Solar", "Output", 7812500).getInt(7812500);
            ConfigWI.wphsptier = config.get("Photon Solar", "Tier", 9).getInt(9);
            ConfigWI.wphspstorage = config.get("Photon Solar", "Storage", 800000000).getInt(800000000);
            ConfigWI.wphsptransfer = config.get("Photon Solar", "Wireless Transfer", 3906250).getInt(3906250);

            ConfigWI.wneuspgenday = config.get("Neutron Solar", "Day Generation", 19631250).getInt(19631250);
            ConfigWI.wneuspgennight = config.get("Neutron Solar", "Night Generation", 19631250).getInt(19631250);
            ConfigWI.wneuspoutput = config.get("Neutron Solar", "Output", 39262500).getInt(39262500);
            ConfigWI.wneusptier = config.get("Neutron Solar", "Tier", 10).getInt(10);
            ConfigWI.wneuspstorage = config.get("Neutron Solar", "Storage", 1200000000).getInt(1200000000);
            ConfigWI.wneusptransfer = config.get("Neutron Solar", "Wireless Transfer", 19631250).getInt(19631250);

            ConfigWI.wbarionspgenday = config.get("Barion Solar", "Day Generation", 98000000).getInt(98000000);
            ConfigWI.wbarionspgennight = config.get("Barion Solar", "Night Generation", 98000000).getInt(98000000);
            ConfigWI.wbarionspoutput = config.get("Barion Solar", "Output", 196000000).getInt(196000000);
            ConfigWI.wbarionsptier = config.get("Barion Solar", "Tier", 11).getInt(11);
            ConfigWI.wbarionspstorage = config.get("Barion Solar", "Storage", 1800000000).getInt(1800000000);
            ConfigWI.wbarionsptransfer = config.get("Barion Solar", "Wireless Transfer", 98000000).getInt(98000000);

            ConfigWI.wadronspgenday = config.get("Adron Solar", "Day Generation", 490000000).getInt(490000000);
            ConfigWI.wadronspgennight = config.get("Adron Solar", "Night Generation", 490000000).getInt(490000000);
            ConfigWI.wadronspoutput = config.get("Adron Solar", "Output", 1000000000).getInt(1000000000);
            ConfigWI.wadronsptier = config.get("Adron Solar", "Tier", 12).getInt(12);
            ConfigWI.wadronspstorage = config.get("Adron Solar", "Storage", 2145000000).getInt(2145000000);
            ConfigWI.wadronsptransfer = config.get("Adron Solar", "Wireless Transfer", 490000000).getInt(490000000);


            ConfigWI.wstorageoutput = config.get("Wireless Receiver", "Output", 16384).getInt(16384);
            ConfigWI.wstoragemaxstorage = config.get("Wireless Receiver", "Storage", 200000000)
                    .getInt(200000000);
            ConfigWI.wstoragetier = config.get("Wireless Receiver", "Tier", 4).getInt(4);

            ConfigWI.wirelessqgenoutput = config.get("Wireless Quantum Generator", "Output", 32768).getInt(32768);
            ConfigWI.wirelessqgentier = config.get("Wireless Quantum Generator", "Tier", 4).getInt(4);
            ConfigWI.wirelessqgentransfer = config.get("Wireless Quantum Generator", "Wireless transfer limit", 32768)
                    .getInt(32768);

            ConfigWI.maxstorageofchargers = config
                    .get(categoryWIRELESSCHARGER, "MaxStorage", 100000000, "Maximum storage of wireless player chargers (EU)")
                    .getInt(100000000);
            ConfigWI.tierofchargers = config.get(categoryWIRELESSCHARGER, "Tier", 10, "Tier of wireless player chargers").getInt(10);

            int secondslocal = config.get(categoryXPSENDER, "Interval", 1, "Delay (in seconds) to send XP to players, not less than 1").getInt(1);
            ConfigWI.secondsXPSender = Math.max(1, secondslocal);

            int xpgivenlocal = config.get(categoryXPSENDER, "XP amount", 2, "XP amount given to player per 1 operation").getInt(2);
            ConfigWI.amountXPsent = Math.max(1, xpgivenlocal);

            ConfigWI.maxstorageXPSender = config.get(categoryXPSENDER, "MaxStorage", 40000000, "Maximum storage of energy")
                    .getInt(40000000);
            ConfigWI.energyperxppointXPSender = config.get(categoryXPSENDER, "EU per point", 25000, "EU energy consumed per 1 XP point")
                    .getInt(25000);

            ConfigWI.tierXPSender = config.get(categoryXPSENDER, "Tier", 8, "Tier of XP sender").getInt();

            int stolenEUlocal = config
                    .get(categoryVAMPIREWEAPONS, "EU Stolen", 120000, "Stolen amount of EU energy from one armor part")
                    .getInt(120000);
            ConfigWI.stolenEnergyEUFromArmor = Math.max(0, stolenEUlocal);

            int vbowenergycost = config.get(categoryVAMPIREWEAPONS, "Cost", 1000, "EU cost per vamp bow shot").getInt(1000);
            ConfigWI.vampBowShotEnergyCost = Math.max(0, vbowenergycost);

            ConfigWI.vampBowMaxCharge = config.get(categoryVAMPIREWEAPONS, "MaxCharge", 300000, "Maximum charge (EU) of vampire bow").getInt(300000);

            int vbowxpvampired = config.get(categoryVAMPIREWEAPONS, "XP Vampired", 4, "XP points vampired per hit").getInt(4);
            if(vbowxpvampired < 0)
                ConfigWI.vampBowXPVampiredAmount = 0;
            else
                ConfigWI.vampBowXPVampiredAmount = Math.min(vbowxpvampired, 20);


            ConfigWI.enderChargeArmorValue = config.getInt("Self-Recharge", categoryTOOLS_ARMOR, 8,
                    0, 16, "Ender Quamtum Armor self-charging EU/t amount, not more than 16");

            int vajramaxchargelocal = config
                    .get(categoryTOOLS_ARMOR, "MaxCharge", 6000000, "Maximum charge of Lucky Vajra, not less than 3M EU").getInt();
            ConfigWI.maxVajraCharge = Math.max(3000000, vajramaxchargelocal);

            vajraFortuneEnchantmentlevel = config.getInt("FortuneLevel", categoryTOOLS_ARMOR, 5,
                    1, Integer.MAX_VALUE, "The Fortune enchantment level of Lucky Vajra");

            int energyperoperationlocal = config
                    .get(categoryTOOLS_ARMOR, "Break cost", 3000, "Vajra energy using per block break, not more than half of maxcharge").getInt();
            ConfigWI.vajraEnergyPerOperation = Math.min(energyperoperationlocal, ConfigWI.maxVajraCharge / 2);

            ConfigWI.isServerJoinedChatMsgEnabled = config.get(categoryOTHER, "EnableJoinMsg", false, "Determines if the message of available WI commands is shown when player joined server").getBoolean();
            ConfigWI.isModLogEnabled = config.get(categoryOTHER, "Logging", true, "Enables console logging of Wireless Industry mod").getBoolean();
            ConfigWI.isIU_EuRf_priority = config.get(categoryENERGYBALANCE, "IU priority", true, "If true, the RF/EU multiplier will be loaded from Industrial Upgrade config instead of WI config").getBoolean();
            enableWICommands = config.get(categoryOTHER, "EnableModCommands", true, "Enables commands /clo and /stow").getBoolean();

            ConfigWI.enableWeaponsChatMsgs = config.get(categoryVAMPIREWEAPONS, "ChatMsgs", true, "Enables player receiving info chat messages (e.g. when shooter absorbes XP from enemy who is hit by vampiric arrow)").getBoolean();
            ConfigWI.permissionLevelCommandClearOwner = config.get(categoryCOMMANDS, "permissionLevelCLO", 2, "Required permission level for /clo command")
                    .getInt();
            ConfigWI.permissionLevelCommandChangeOwner = config.get(categoryCOMMANDS, "permissionLevelSTOW", 2, "Required permission level for /stow command")
                    .getInt();

            ConfigWI.machinesChargerMaxEnergyDouble = config.get(categoryWIRELESSCHARGER, "MaxStorageMChEU", 10000000000.0, "Maximum storage of wireless machines charger (EU)").getDouble();
            ConfigWI.machinesChargerMaxEnergyRF = config.get(categoryWIRELESSCHARGER, "MaxStorageMChRF", 2145000000, "Maximum storage of wireless machines charger (RF)")
                    .getInt();
            ConfigWI.machinesChargerTier = config.get(categoryWIRELESSCHARGER, "TierMCh", 14, "Tier of wireless machines charger")
                    .getInt();

            ConfigWI.matterCollectorCapacity = config.get(categoryOTHER, "TankCapacity", 1000000, "The capacity of internal tank (mB) of Liquid Matter Collector")
                    .getInt();

            ConfigWI.vajraChargerMaxStorage = config.get(categoryOTHER, "MaxStorageVCh", 30000000, "Maximum storage of Instant Vajra Charger")
                    .getInt();
            ConfigWI.tierVajraCharger = config.get(categoryOTHER, "TierVCh", 5, "Tier of Instant Vajra Charger")
                    .getInt();

            ConfigWI.enableVajraDischargingAfterEnchant = config.get(categoryTOOLS_ARMOR, "EnchDischarge", false, "Enables Lucky Vajra discharging some amount of energy after enchanted").getBoolean();
            ConfigWI.vajraEnchantDischargeEnergyAmount = config.get(categoryTOOLS_ARMOR, "DischargeAmount", 100000, "The amount of energy for which Lucky Vajra is discharged")
                    .getInt();


            if(ConfigWI.isModLogEnabled)
                LogHelperWI.info("Finished reading " + Reference.NAME_MOD + " config");
        } catch(Exception e) {
            System.out.println("{" + Reference.NAME_MOD + "} " + "error occurred while reading config file");
            throw new RuntimeException(e);
        } finally {
            if(config.hasChanged()) {
                config.save();
            }
        }

        try {
            Configuration iu_config = new Configuration(new File(event.getModConfigurationDirectory(), IU_config));
            iu_config.load();
            EuRfCrossConfig = iu_config.get("general", "coefficient rf", 4).getInt(4);
        } catch(Exception e) {
            if(ConfigWI.isModLogEnabled) {
                LogHelperWI.error("CANNOT read \\\"coefficient rf\\\" value from " + IU_config);
                LogHelperWI.warning("Setting the value of RF/EU multiplier from WI config...");
            }
        } finally {
            if(EuRf_WI_Config < 1) EuRf_WI_Config = 1;

            if(ConfigWI.isIU_EuRf_priority)
                ConfigWI.EUToRF_Multiplier = EuRfCrossConfig == 0 ? EuRf_WI_Config : EuRfCrossConfig;
            else
                ConfigWI.EUToRF_Multiplier = EuRf_WI_Config;

            if(ConfigWI.isModLogEnabled)
                LogHelperWI.info("RF/EU multiplier was loaded from Industrial Upgrade config, " + ConfigWI.EUToRF_Multiplier
                        + " RF = 1 EU");
        }

    }

    static final String categoryOTHER = "Other";
    static final String categoryXPSENDER = "XP Sender";
    static final String categoryVAMPIREWEAPONS = "Vampire Weapons";
    static final String categoryWIRELESSCHARGER = "Wireless Chargers";
    static final String categoryENERGYBALANCE = "Energy balance";
    static final String categoryTOOLS_ARMOR = "Tools and Armor";
    static final String categoryCOMMANDS = "Commands";

    static final String IU_config = "industrialupgrade.cfg";

}
