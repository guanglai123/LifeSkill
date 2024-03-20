package org.guanglai.lifeskill.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.guanglai.lifeskill.LifeSkill;

public abstract class PluginInventory implements InventoryHolder {

    protected final Player player;

    protected int page = 1;

    protected PluginInventory(Player player) {
        this.player = player;
    }

    @Override
    public abstract Inventory getInventory();

    public abstract void whenClicked(InventoryClickEvent event);

    public void open() {
        if (Bukkit.isPrimaryThread()) {
            player.openInventory(getInventory());
        } else {
            Bukkit.getScheduler().runTask(LifeSkill.instance, () -> player.openInventory(getInventory()));
        }
    }
}
