package org.guanglai.lifeskill.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.guanglai.lifeskill.command.CommandManager;

import java.util.Collections;
import java.util.List;

public class HelpCmd extends SubCommand{

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "所有指令:");
        for (SubCommand command : CommandManager.getInstance().getCommands()) {
            sender.sendMessage(ChatColor.AQUA + "/lifeskill " + command.getUsage() + ChatColor.GRAY + " - " + command.getDescription());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "列出所有指令";
    }

    @Override
    public String getPermission() {
        return "LifeSkill.help";
    }
}
