package org.guanglai.lifeskill.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.guanglai.lifeskill.LifeSkill;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class CollectionActionBar {
    private static final Map<UUID, BukkitTask> playerTasks = new HashMap<>();

    public static void send(Player player, long collectionTime, CollectionActionBarType type) {
        long start = System.currentTimeMillis();
        switch (type) {
            case COUNTDOWN -> {
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        long elapsedTime = System.currentTimeMillis() - start;
                        float remainingTime = (collectionTime - elapsedTime) / 1000f;
                        if (remainingTime > 0) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + String.format("%.1f", remainingTime) + "秒"));
                        } else {
                            cancelTask(player);
                        }
                    }
                }.runTaskTimer(LifeSkill.instance, 0L, 2L);
                playerTasks.put(player.getUniqueId(), task);
            }
            case PROGRESS -> {
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        long elapsedTime = System.currentTimeMillis() - start;
                        if (elapsedTime <= collectionTime) {
                            StringBuilder progress = new StringBuilder();
                            for (int i = 0; i < 10; i++) {
                                progress.append(elapsedTime > collectionTime * i / 10 ? ChatColor.GREEN : ChatColor.WHITE).append("█");
                            }
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(progress.toString()));
                        } else {
                            cancelTask(player);
                        }
                    }
                }.runTaskTimer(LifeSkill.instance, 0L, collectionTime / 1000 * 20 / 10);
                playerTasks.put(player.getUniqueId(), task);
            }
            case NONE -> {

            }
            case INVALID -> LifeSkill.instance.getLogger().log(Level.SEVERE, "config 裡面的 collection-action-bar 設定錯誤");
        }
    }

    public static void cancelTask(Player player) {
        BukkitTask task = playerTasks.get(player.getUniqueId());
        if (task != null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
            task.cancel();
            playerTasks.remove(player.getUniqueId());
        }
    }
}
