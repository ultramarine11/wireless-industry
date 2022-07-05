package ru.wirelesstools.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.item.armor.IPrivateArmor;
import ru.wirelesstools.utils.HelperUtils;

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
        if(commandSender instanceof EntityPlayer) {
            if(args.length > 1)
                throw new WrongUsageException(this.getCommandUsage(commandSender));

            switch(args.length) {
                case 0: {
                    EntityPlayerMP senderplayer = CommandBase.getCommandSenderAsPlayer(commandSender);
                    for(ItemStack stack : senderplayer.inventory.mainInventory) {
                        if(stack == null)
                            continue;
                        if(stack.getItem() instanceof IPrivateArmor) {
                            IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                            if(armor.getArmorOwner(stack) == null) {
                                HelperUtils.sendMessageToPlayer(senderplayer, "command.wi.msg.no.owner");
                            } else {
                                armor.clearOwner(stack);
                                HelperUtils.sendMessageToPlayer(senderplayer, "command.wi.msg.cleared.owner");
                            }
                        }
                    }
                    break;
                }
                case 1: {
                    EntityPlayerMP playermp = CommandBase.getPlayer(commandSender, args[0]);
                    EntityPlayerMP playersender = CommandBase.getCommandSenderAsPlayer(commandSender);
                    if(playersender.getGameProfile().equals(playermp.getGameProfile())) {
                        for(ItemStack stack : playersender.inventory.mainInventory) {
                            if(stack == null)
                                continue;
                            if(stack.getItem() instanceof IPrivateArmor) {
                                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                                armor.clearOwner(stack);
                                HelperUtils.sendMessageToPlayer(playersender, "command.wi.msg.cleared.owner");
                            }
                        }
                    } else {
                        boolean success = false;
                        for(ItemStack stack : playermp.inventory.mainInventory) {
                            if(stack == null)
                                continue;
                            if(stack.getItem() instanceof IPrivateArmor) {
                                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                                armor.clearOwner(stack);
                                if(!success)
                                    success = true;
                                HelperUtils.sendMessageToPlayer(playermp, "command.wi.msg.cleared.from.inv");
                            }
                        }
                        if(success)
                            HelperUtils.sendChatMessageMulti(playersender, "command.wi.msg.cleared.owner.from", new ChatComponentText(": " + playermp.getGameProfile().getName()));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return ConfigWI.permissionLevelCommandClearOwner;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return args.length == 1 ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames())
                : null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
        // ServerConfigurationManager.func_152596_g(GameProfile p_152596_1_) �������� ��
        // ��, ����� �� ���� ����� ������������ �������
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
