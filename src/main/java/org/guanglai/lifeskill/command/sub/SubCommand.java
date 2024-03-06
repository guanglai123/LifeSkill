package org.guanglai.lifeskill.command.sub;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public abstract boolean onCommand(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public abstract String getUsage();

    public abstract String getDescription();

    public abstract String getPermission();
}
