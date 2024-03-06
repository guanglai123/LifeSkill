package org.guanglai.lifeskill.manager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.guanglai.lifeskill.LifeSkill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDataManager {

    private final File PLUGIN_DIR = new File("plugins", "LifeSkill");

    private final File USERDATA_DIR = new File(PLUGIN_DIR, "userdata");

    public void incPlayerCollection(Player player, String type, String id, int amount) {
        try {
            checkPlayerData(player);
            File playerData = new File(USERDATA_DIR, player.getUniqueId() + ".yml");
            FileConfiguration playerFile = YamlConfiguration.loadConfiguration(playerData);
            ConfigurationSection collections = playerFile.getConfigurationSection("collections");
            boolean hasCollection = false;
            for (String key : collections.getKeys(false)) {
                ConfigurationSection collection = collections.getConfigurationSection(key);
                if (collection.getString("type").equals(type) && collection.getString("id").equals(id)) {
                    collection.set("amount", collection.getInt("amount") + amount);
                    hasCollection = true;
                    break;
                }
            }
            if (!hasCollection) {
                String s = String.valueOf(collections.getKeys(false).size() + 1);
                collections.createSection(s);
                ConfigurationSection collection = collections.getConfigurationSection(s);
                collection.set("type", type);
                collection.set("id", id);
                collection.set("amount", amount);
            }
            playerFile.save(playerData);
        } catch (IOException e) {
            System.out.println(ChatColor.RED + "更新玩家素材失敗");
        }
    }

    public void setPlayerCoolDown(Player player, Location location, Long coolDown) {
        try {
            checkPlayerData(player);
            File playerData = new File(USERDATA_DIR, player.getUniqueId() + ".yml");
            FileConfiguration playerFile = YamlConfiguration.loadConfiguration(playerData);
            ConfigurationSection coolDowns = playerFile.getConfigurationSection("cool-downs");
            boolean hasCoolDown = false;
            for (String key : coolDowns.getKeys(false)) {
                ConfigurationSection collection = coolDowns.getConfigurationSection(key);
                if (collection.get("location").equals(location)) {
                    collection.set("cool-down", coolDown);
                    hasCoolDown = true;
                    break;
                }
            }
            if (!hasCoolDown) {
                String s = String.valueOf(coolDowns.getKeys(false).size() + 1);
                coolDowns.createSection(s);
                ConfigurationSection collection = coolDowns.getConfigurationSection(s);
                collection.set("location", location);
                collection.set("cool-down", coolDown);
            }
            playerFile.save(playerData);
        } catch (IOException e) {
            System.out.println(ChatColor.RED + "更新玩家冷卻時間失敗");
        }
    }

    public List<ItemStack> getPlayerCollections(Player player) {
        List<ItemStack> collectionList = new ArrayList<>();
        File playerData = new File(USERDATA_DIR, player.getUniqueId() + ".yml");
        if (playerData.exists()) {
            FileConfiguration playerFile = YamlConfiguration.loadConfiguration(playerData);
            ConfigurationSection collections = playerFile.getConfigurationSection("collections");
            for (String key : collections.getKeys(false)) {
                ConfigurationSection collection = collections.getConfigurationSection(key);
                ItemStack is = LifeSkill.getMMO(collection.getString("type"), collection.getString("id"));
                ItemMeta im = is.getItemMeta();
                List<String> lore = im.getLore();
                lore.add(ChatColor.GOLD + "數量: " + ChatColor.YELLOW + collection.getInt("amount") + " 個");
                im.setLore(lore);
                is.setItemMeta(im);
                collectionList.add(is);
            }
            return collectionList;
        } else {
            return Collections.emptyList();
        }
    }

    public Long getPlayerCoolDown(Player player, Location location) {
        File playerData = new File(USERDATA_DIR, player.getUniqueId() + ".yml");
        if (playerData.exists()) {
            FileConfiguration playerFile = YamlConfiguration.loadConfiguration(playerData);
            ConfigurationSection coolDowns = playerFile.getConfigurationSection("cool-downs");
            for (String key : coolDowns.getKeys(false)) {
                ConfigurationSection coolDown = coolDowns.getConfigurationSection(key);
                if (coolDown.get("location").equals(location)) {
                    return coolDown.getLong("cool-down");
                }
            }
        }
        return 0L;
    }

    private void checkPlayerData(Player player) {
        if (USERDATA_DIR.exists()) {
            File playerData = new File(USERDATA_DIR, player.getUniqueId() + ".yml");
            try {
                if (playerData.createNewFile()) {
                    FileConfiguration playerFile = YamlConfiguration.loadConfiguration(playerData);
                    playerFile.createSection("collections");
                    playerFile.createSection("cool-downs");
                    playerFile.save(playerData);
                }
            } catch (IOException e) {
                System.out.println(ChatColor.RED + "建立新玩家數據失敗");
            }
        } else {
            System.out.println(ChatColor.RED + "玩家數據資料夾不存在");
        }
    }
}