package org.guanglai.lifeskill.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.guanglai.lifeskill.command.sub.HelpCmd;
import org.guanglai.lifeskill.command.sub.OpenBackPackCmd;
import org.guanglai.lifeskill.command.sub.SubCommand;

import java.util.*;

public class CommandManager implements TabExecutor {

    private static CommandManager instance;

    private final Map<String, SubCommand> commands = new HashMap<>();

    public CommandManager() {
        instance = this;

        commands.put("open", new OpenBackPackCmd());
        commands.put("help", new HelpCmd());
    }

    public static CommandManager getInstance() {
        return instance;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "請打/lifeskill help 獲得更多資訊");
            return true;
        }

        SubCommand command = commands.get(args[0]);

        if (command == null) {
            sender.sendMessage(ChatColor.RED + "此指令不存在");
            return true;
        }

        if (!sender.hasPermission(command.getPermission())) {
            sender.sendMessage(ChatColor.RED + "你沒有權限使用此指令");
            return true;
        }

        String[] subCmdArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCmdArgs, 0, subCmdArgs.length);

        if (!command.onCommand(sender, subCmdArgs)) {
            sender.sendMessage(ChatColor.RED + "因為某種原因 你不能使用此指令");
            return true;
        }

        return true;
    }

    public Collection<SubCommand> getCommands() {
        return commands.values();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = null;
        final String typed = args[0].toLowerCase();

        if (args.length == 1) {
            for (Map.Entry<String, SubCommand> entry : commands.entrySet()) {
                final String name = entry.getKey();
                final SubCommand subCommand = entry.getValue();

                if (name.startsWith(typed) && sender.hasPermission(subCommand.getPermission())) {

                    if (toReturn == null) {
                        toReturn = new LinkedList<>();
                    }

                    toReturn.add(name);
                }
            }
        }

        if (args.length > 1) {
            final String subName = args[0];
            final SubCommand subCommand = commands.get(subName);

            if (subCommand != null) {
                toReturn = subCommand.onTabComplete(sender, args);
            }
        }

        return toReturn == null ? Collections.emptyList() : toReturn;
    }
}
