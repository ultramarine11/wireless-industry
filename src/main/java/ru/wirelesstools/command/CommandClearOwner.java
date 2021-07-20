package ru.wirelesstools.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import ru.wirelesstools.item.armor.IPrivateArmor;

import java.util.ArrayList;
import java.util.List;

public class CommandClearOwner extends CommandBase {

    public static final String NAME_COMMAND = "clo";
    public static final String ALIAS_1 = "cleararmorowner";

    public static final String USAGE_COMMAND = "/" + NAME_COMMAND + " " + "<player>";

    @Override
    public String getCommandName() {

        return NAME_COMMAND;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return USAGE_COMMAND;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if (commandSender instanceof EntityPlayer) {
            if (args.length > 1) {
                throw new WrongUsageException(this.getCommandUsage(commandSender));
            }
            switch (args.length) {
                case 0: {
                    EntityPlayerMP senderplayer = CommandBase.getCommandSenderAsPlayer(commandSender);
                    for (ItemStack stack : senderplayer.inventory.mainInventory) {
                        if (stack == null)
                            continue;
                        if (stack.getItem() instanceof IPrivateArmor) {
                            IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                            if (armor.getArmorOwner(stack) == null) {
                                senderplayer.addChatMessage(new ChatComponentTranslation(StatCollector.translateToLocal("command.wi.msg.no.owner")));
                            } else {
                                armor.clearOwner(stack);
                                senderplayer.addChatMessage(new ChatComponentTranslation(
                                        StatCollector.translateToLocal("command.wi.msg.cleared.owner")));
                            }
                        }
                    }
                    break;
                }
                case 1: {
                    EntityPlayerMP playermp = CommandBase.getPlayer(commandSender, args[0]);
                    EntityPlayerMP playersender = CommandBase.getCommandSenderAsPlayer(commandSender);
                    if (playersender.getGameProfile().equals(playermp.getGameProfile())) {
                        for (ItemStack stack : playersender.inventory.mainInventory) {
                            if (stack == null)
                                continue;
                            if (stack.getItem() instanceof IPrivateArmor) {
                                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                                armor.clearOwner(stack);
                                playersender.addChatMessage(
                                        new ChatComponentTranslation(StatCollector.translateToLocal("command.wi.msg.cleared.owner")));
                            }
                        }
                    } else {
                        boolean success = false;
                        for (ItemStack stack : playermp.inventory.mainInventory) {
                            if (stack == null)
                                continue;
                            if (stack.getItem() instanceof IPrivateArmor) {
                                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                                armor.clearOwner(stack);
                                if (!success)
                                    success = true;
                                playermp.addChatMessage(
                                        new ChatComponentTranslation(StatCollector.translateToLocal("command.wi.msg.cleared.from.inv")));
                            }
                        }
                        if (success)
                            playersender.addChatMessage(
                                    new ChatComponentTranslation(StatCollector.translateToLocal("command.wi.msg.cleared.owner.from")
                                            + ": " + playermp.getGameProfile().getName()));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 2;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames())
                : null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {

        // ServerConfigurationManager.func_152596_g(GameProfile p_152596_1_) отвечает за
        // то, может ли этот игрок использовать команды
        return commandSender instanceof EntityPlayerMP && MinecraftServer.getServer().getConfigurationManager()
                .func_152596_g(((EntityPlayerMP) commandSender).getGameProfile());
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliaseslist = new ArrayList<>();
        aliaseslist.add(ALIAS_1);

        return aliaseslist;
    }

}
