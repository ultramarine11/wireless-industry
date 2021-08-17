package ru.wirelesstools.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ru.wirelesstools.LogHelperWI;
import ru.wirelesstools.config.ConfigWI;

public class NEIRegisterWIConfig implements IConfigureNEI {

    public NEIRegisterWIConfig() {

    }

    @Override
    public void loadConfig() {
        if(ConfigWI.isModLogEnabled)
            LogHelperWI.info("Loading NEI compatibility...");
        API.registerRecipeHandler(new NeiPFPConverter());
        API.registerUsageHandler(new NeiPFPConverter());
    }

    @Override
    public String getName() {
        return "Wireless Industry";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
