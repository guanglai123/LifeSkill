package org.guanglai.lifeskill.command.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.guanglai.lifeskill.gui.BackPack;

import java.util.Collections;
import java.util.List;

public class OpenBackPackCmd extends SubCommand {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player p) {
            new BackPack(p).open();
        } else {
            sender.sendMessage(ChatColor.RED + "只有玩家才可以使用此指令");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "open";
    }

    @Override
    public String getDescription() {
        return "打開素材背包";
    }

    @Override
    public String getPermission() {
        return "LifeSkill.open";
    }
}
