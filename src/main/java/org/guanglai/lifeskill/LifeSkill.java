package org.guanglai.lifeskill;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.guanglai.lifeskill.command.CommandManager;
import org.guanglai.lifeskill.gui.listener.GuiListener;
import org.guanglai.lifeskill.listener.CollectListener;
import org.guanglai.lifeskill.manager.ConfigManager;
import org.guanglai.lifeskill.manager.UserDataManager;

public final class LifeSkill extends JavaPlugin {

    public static LifeSkill instance;

    private ConfigManager configManager;
    private UserDataManager userDataManager;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getCommand("lifeskill").setExecutor(new CommandManager());
        getServer().getPluginManager().registerEvents(new CollectListener(), this);
        configManager = new ConfigManager();
        userDataManager = new UserDataManager();
    }

    public static ItemStack getMMO(String type, String id) {
        MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), id);
        ItemStack item = mmoitem.newBuilder().build();
        return item;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public UserDataManager getUserDataManager() {
        return userDataManager;
    }
}
