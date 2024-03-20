package org.guanglai.lifeskill.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.guanglai.lifeskill.LifeSkill;
import org.guanglai.lifeskill.record.PositionRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final File PLUGIN_DIR = new File("plugins", "LifeSkill");
    private final File POSITION_DIR = new File(PLUGIN_DIR, "position");
    private final File POSITION_FILE = new File(POSITION_DIR, "position.yml");
    private final File USERDATA_DIR = new File(PLUGIN_DIR, "userdata");
    private FileConfiguration position;

    public ConfigManager() {
        copyDefaultConfig();

        position = YamlConfiguration.loadConfiguration(POSITION_FILE);
    }

    public List<PositionRecord> getPositions() {
        List<PositionRecord> positionRecords = new ArrayList<>();
        for (String key : position.getKeys(false)) {
            ConfigurationSection section = position.getConfigurationSection(key);
            Location location = new Location(Bukkit.getWorld(section.getString("world")), section.getInt("x"), section.getInt("y"), section.getInt("z"));
            String type = section.getString("type");
            String id = section.getString("id");
            int amount = section.getInt("amount");
            Long time = section.getLong("time");
            Long coolDown = section.getLong("cool-down");
            positionRecords.add(new PositionRecord(location, type, id, amount, time, coolDown));
        }
        return positionRecords;
    }

    private void copyDefaultConfig() {
        PLUGIN_DIR.mkdirs();
        POSITION_DIR.mkdirs();
        USERDATA_DIR.mkdirs();

        try {
            if (!POSITION_FILE.exists()) {
                Files.copy(LifeSkill.instance.getResource("position.yml"), POSITION_FILE.toPath());
            }
        } catch (IOException e) {
            System.out.println("複製錯誤");
        }
    }
}
