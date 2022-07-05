package ru.wirelesstools.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import ru.wirelesstools.tiles.TileXPSenderElectric;

import java.util.List;

public class ExperienceUtils {

    public static int getPlayerXP(EntityPlayer player) {
        return (int) ((float) getExperienceForLevelNew(player.experienceLevel)
                + player.experience * (float) player.xpBarCap());
    }
    
    public static void consumeXPFromPlayer(EntityPlayer player, int amount) {
        ExperienceUtils.addPlayerXP(player, -amount);
    }

    public static void addPlayerXP(EntityPlayer player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevelNew(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
    }

    @Deprecated
    public static int getExperienceForLevel(int level) {
        return level == 0 ? 0
                : (level > 0 && level < 16 ? level * 17
                : (level > 15 && level < 31
                ? (int) (1.5D * Math.pow(level, 2.0D) - 29.5D * (double) level + 360.0D)
                : (int) (3.5D * Math.pow(level, 2.0D) - 151.5D * (double) level + 2220.0D)));
    }

    public static int getExperienceForLevelNew(int level) {
        if (level == 0)
            return 0;
        else if (level > 0 && level < 16)
            return level * 17;
        else if (level > 15 && level < 31)
            return (int) (1.5D * Math.pow(level, 2.0D) - 29.5D * (double) level + 360.0D);
        else
            return (int) (3.5D * Math.pow(level, 2.0D) - 151.5D * (double) level + 2220.0D);
    }

    public static int getXpToNextLevel(int level) {
        int levelXP = getLevelForExperience(level);
        int nextXP = getExperienceForLevelNew(level + 1);
        return nextXP - levelXP;
    }

    public static int getLevelForExperience(int experience) {
        int i;
        for (i = 0; getExperienceForLevelNew(i) <= experience; ++i) {
        }

        return i - 1;
    }

    public static void sendXPToPlayersAround(TileXPSenderElectric tile, int x, int y, int z, int points) {
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(tile.xCoord - x, tile.yCoord - y, tile.zCoord - z,
                tile.xCoord + x, tile.yCoord + y, tile.zCoord + z);
        List<EntityPlayer> list = tile.getWorldObj().getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
        for (EntityPlayer player : list) {
            if (player != null)
                ExperienceUtils.addPlayerXP(player, points);
        }
    }
}
