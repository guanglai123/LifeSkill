package org.guanglai.lifeskill;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.guanglai.lifeskill.command.CommandManager;
import org.guanglai.lifeskill.gui.listener.GuiListener;
import org.guanglai.lifeskill.listener.Collection;
import org.guanglai.lifeskill.manager.ConfigManager;
import org.guanglai.lifeskill.manager.UserDataManager;

public final class LifeSkill extends JavaPlugin {
    public static LifeSkill instance;

    private final ConfigManager configManager;
    private final UserDataManager userDataManager;

    public LifeSkill() {
        instance = this;
        configManager = new ConfigManager();
        userDataManager = new UserDataManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getCommand("lifeskill").setExecutor(new CommandManager());
        getServer().getPluginManager().registerEvents(new Collection(), this);
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public UserDataManager getUserDataManager() {
        return userDataManager;
    }

    public static ItemStack getMMO(String type, String id) {
        MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), id);
        return mmoitem.newBuilder().build();
    }
}
