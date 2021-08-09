package ru.wirelesstools;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import ru.wirelesstools.config.ConfigWI;

public class LogHelperWI {

    public static void info(String infomessage) {
        toLog(Level.INFO, infomessage);
    }

    public static void error(String infomessage) {
        toLog(Level.ERROR, infomessage);
    }

    public static void warning(String infomessage) {
        toLog(Level.WARN, infomessage);
    }

    static void toLog(Level logLevel, String msg) {
        if(ConfigWI.isModLogEnabled)
            FMLLog.log(Reference.NAME_MOD, logLevel, msg);
    }
}
