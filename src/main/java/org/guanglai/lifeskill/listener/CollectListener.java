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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.guanglai.lifeskill.LifeSkill;
import org.guanglai.lifeskill.record.PositionRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CollectListener implements Listener {

    private BukkitTask finalTask;
    private BukkitTask releaseTask;
    private BukkitTask actionBarTask;

    private final Map<UUID, Long> actionbarMap;

    public CollectListener() {
        actionbarMap = new HashMap<>();
    }

    @EventHandler
    public void collect(PlayerInteractEvent e) {
        for (int j = 0; j < LifeSkill.instance.getConfigManager().getPositions().size(); j++) {
            PositionRecord position = LifeSkill.instance.getConfigManager().getPositions().get(j);
            long time = position.time();
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getLocation().equals(position.location())) {
                Player p = e.getPlayer();
                if (LifeSkill.instance.getUserDataManager().getPlayerCoolDown(p, position.location()) == 0L || (System.currentTimeMillis() - LifeSkill.instance.getUserDataManager().getPlayerCoolDown(p, position.location())) >= position.coolDown()) {
                    BukkitScheduler scheduler = Bukkit.getScheduler();
                    if (finalTask == null) {
                        actionbarMap.put(p.getUniqueId(), System.currentTimeMillis());
                        finalTask = scheduler.runTaskLater(LifeSkill.instance, () -> {
                            p.sendMessage( ChatColor.GREEN + "你成功採集到 " + ChatColor.RESET + LifeSkill.getMMO(position.type(), position.id()).getItemMeta().getDisplayName());
                            LifeSkill.instance.getUserDataManager().incPlayerCollection(p, position.type(), position.id(), position.amount());
                            LifeSkill.instance.getUserDataManager().setPlayerCoolDown(p, position.location(), System.currentTimeMillis());
                        }, time / 1000 * 20);
                        actionBarTask = scheduler.runTaskTimer(LifeSkill.instance, () -> {
                            StringBuilder processBar = new StringBuilder();
                            String barChat = "█";
                            for (long i = time / 10; i <= time; i += time / 10) {
                                processBar.append(System.currentTimeMillis() - actionbarMap.get(p.getUniqueId()) >= i ? ChatColor.GREEN : ChatColor.WHITE).append(barChat);
                            }
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(processBar.toString()));
                        }, 0L, time / 1000 * 20 / 10);
                    }
                    if (releaseTask != null && scheduler.isQueued(releaseTask.getTaskId())) {
                        scheduler.cancelTask(releaseTask.getTaskId());
                    }
                    releaseTask = scheduler.runTaskLater(LifeSkill.instance, () -> {
                        if (finalTask != null && scheduler.isQueued(finalTask.getTaskId())) {
                            scheduler.cancelTask(finalTask.getTaskId());
                            scheduler.cancelTask(actionBarTask.getTaskId());
                            p.sendMessage(ChatColor.GRAY + "取消採集");
                        }
                        finalTask = null;
                        scheduler.cancelTask(actionBarTask.getTaskId());
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
                    }, 6L);
                } else {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("還需要" + (position.coolDown() - (System.currentTimeMillis() - LifeSkill.instance.getUserDataManager().getPlayerCoolDown(p, position.location()))) / 1000 + "秒"));
                }
            }
        }
    }
}
