package org.guanglai.lifeskill.listener;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.guanglai.lifeskill.LifeSkill;
import org.guanglai.lifeskill.record.PositionRecord;
import org.guanglai.lifeskill.util.CollectionActionBar;
import org.guanglai.lifeskill.util.CollectionActionBarType;

public class Collection implements Listener {
    private BukkitTask finalTask;
    private BukkitTask releaseTask;

    @EventHandler
    public void collect(PlayerInteractEvent e) {
        for (PositionRecord position : LifeSkill.instance.getConfigManager().getPositions()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getLocation().equals(position.location())) {
                Player player = e.getPlayer();
                if (LifeSkill.instance.getUserDataManager().getPlayerCoolDown(player, position.location()) == 0L || (System.currentTimeMillis() - LifeSkill.instance.getUserDataManager().getPlayerCoolDown(player, position.location())) >= position.coolDown()) {
                    if (finalTask == null) {
                        finalTask = Bukkit.getScheduler().runTaskLater(LifeSkill.instance, () -> {
                            player.sendMessage(ChatColor.GREEN + "你成功採集到 " + ChatColor.YELLOW + position.amount() + " 個 " + ChatColor.RESET + LifeSkill.getMMO(position.type(), position.id()).getItemMeta().getDisplayName());
                            LifeSkill.instance.getUserDataManager().incPlayerCollection(player, position.type(), position.id(), position.amount());
                            LifeSkill.instance.getUserDataManager().setPlayerCoolDown(player, position.location(), System.currentTimeMillis());
                        }, position.time() / 1000 * 20);
                        CollectionActionBarType type = CollectionActionBarType.safeValueOf(LifeSkill.instance.getConfig().getString("collection-action-bar"));
                        CollectionActionBar.send(player, position.time(), type);
                    }
                    if (releaseTask != null && Bukkit.getScheduler().isQueued(releaseTask.getTaskId())) {
                        Bukkit.getScheduler().cancelTask(releaseTask.getTaskId());
                    }
                    releaseTask = Bukkit.getScheduler().runTaskLater(LifeSkill.instance, () -> {
                        if (finalTask != null && Bukkit.getScheduler().isQueued(finalTask.getTaskId())) {
                            Bukkit.getScheduler().cancelTask(finalTask.getTaskId());
                            CollectionActionBar.cancelTask(player);
                            player.sendMessage(ChatColor.GRAY + "取消採集");
                        }
                        finalTask = null;
                        CollectionActionBar.cancelTask(player);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                    }, 6L);
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("還需要" + (position.coolDown() - (System.currentTimeMillis() - LifeSkill.instance.getUserDataManager().getPlayerCoolDown(player, position.location()))) / 1000 + "秒"));
                }
            }
        }
    }
}
