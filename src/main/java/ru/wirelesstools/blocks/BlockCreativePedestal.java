package ru.wirelesstools.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import ru.wirelesstools.MainWI;
import ru.wirelesstools.Reference;

public class BlockCreativePedestal extends Block {

    public BlockCreativePedestal(String unlocalizedName, Material mat) {
        super(mat);
        this.setBlockName(unlocalizedName);
        this.setCreativeTab(MainWI.tabwi);
        this.setBlockTextureName(Reference.PathTex + "blockCreativePedestal2");
        this.setHardness(2.0F);
        this.setResistance(5.0F);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
                                    float subY, float subZ) {
        if(!world.isRemote) {
            if(!player.capabilities.isCreativeMode) {
                player.setGameType(GameType.CREATIVE);
                MinecraftServer.getServer().getConfigurationManager()
                        .sendChatMsg(new ChatComponentText(EnumChatFormatting.DARK_RED + player.getGameProfile().getName() + " ")
                                .appendSibling(new ChatComponentTranslation("chat.server.message.used.pedestal.creative")
                                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED))));
            } else {
                player.setGameType(GameType.SURVIVAL);
                MinecraftServer.getServer().getConfigurationManager()
                        .sendChatMsg(new ChatComponentText(EnumChatFormatting.YELLOW + player.getGameProfile().getName() + " ")
                                .appendSibling(new ChatComponentTranslation("chat.server.message.used.pedestal.survival")
                                        .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW))));
            }
        }
        return true;
    }
}
