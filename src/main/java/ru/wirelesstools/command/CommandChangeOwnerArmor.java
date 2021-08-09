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
import ru.wirelesstools.config.ConfigWI;
import ru.wirelesstools.item.armor.IPrivateArmor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandChangeOwnerArmor extends CommandBase {

    public static final String NAME_COMMAND = "stow";
    public static final String ALIAS_1 = "setarmorowner";

    public static final String USAGE_COMMAND = "/" + NAME_COMMAND + " " + "<player_target>" + " " + "<player_new_owner>";

    @Override
    public String getCommandName() {
        return NAME_COMMAND;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return USAGE_COMMAND;
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        switch(args.length) {
            case 1:
            case 2:
                return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }

    public List<String> getCommandAliases() {
        List<String> aliaseslist = new ArrayList<>();
        aliaseslist.add(ALIAS_1);

        return aliaseslist;
    }

    public int getRequiredPermissionLevel() {
        return ConfigWI.permissionLevelCommandChangeOwner;
    }

    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
        // ServerConfigurationManager.func_152596_g(GameProfile p_152596_1_) отвечает за
        // то, может ли этот игрок использовать команды
        return commandSender instanceof EntityPlayerMP && MinecraftServer.getServer().getConfigurationManager()
                .func_152596_g(((EntityPlayerMP) commandSender).getGameProfile());
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if(commandSender instanceof EntityPlayer) {
            if(args.length > 2) {
                throw new WrongUsageException(this.getCommandUsage(commandSender));
            }
            boolean found_armor;

            EntityPlayerMP player_sender_command = CommandBase.getCommandSenderAsPlayer(commandSender);
            EntityPlayerMP player_mp_command_target = CommandBase.getPlayer(commandSender, args[0]);
            String player_str_mp_new_owner = args[1];

            if(player_str_mp_new_owner.equalsIgnoreCase("me")) {
                System.out.println(player_str_mp_new_owner);
                found_armor = this.checkPlayerTargetInv(player_mp_command_target, player_sender_command);
            } else {
                found_armor = this.checkPlayerTargetInv(player_mp_command_target, CommandBase.getPlayer(commandSender, args[1]));
            }

            if(found_armor) {
                player_sender_command.addChatMessage(new ChatComponentTranslation(
                        StatCollector.translateToLocal("command.wi.msg.changed.armor.owner")));
            } else {
                player_sender_command.addChatMessage(new ChatComponentTranslation(
                        StatCollector.translateToLocal("command.wi.msg.no.private.armor")));
            }
        }

    }

    private boolean checkPlayerTargetInv(EntityPlayerMP targetPlayer, EntityPlayerMP newOwner) {
        boolean found_armor = false;
        LinkedList<ItemStack> list = new LinkedList<>();
        list.addAll(Arrays.asList(targetPlayer.inventory.armorInventory));
        list.addAll(Arrays.asList(targetPlayer.inventory.mainInventory));
        ItemStack[] allInventory = list.toArray(new ItemStack[] {});

        /*for(ItemStack stack : targetPlayer.inventory.armorInventory) {
            if(stack == null)
                continue;
            if(stack.getItem() instanceof IPrivateArmor) {
                if(!found_armor)
                    found_armor = true;
                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                armor.setArmorOwner(stack, newOwner);
            }
        }

        for(ItemStack stack : targetPlayer.inventory.mainInventory) {
            if(stack == null)
                continue;
            if(stack.getItem() instanceof IPrivateArmor) {
                if(!found_armor)
                    found_armor = true;
                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                armor.setArmorOwner(stack, newOwner);
            }
        }*/

        for(ItemStack stack : allInventory) {
            if(stack == null)
                continue;
            if(stack.getItem() instanceof IPrivateArmor) {
                if(!found_armor)
                    found_armor = true;
                IPrivateArmor armor = (IPrivateArmor) stack.getItem();
                armor.setArmorOwner(stack, newOwner);
            }
        }

        return found_armor;
    }

}
