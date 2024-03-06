package org.guanglai.lifeskill.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.guanglai.lifeskill.gui.PluginInventory;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof PluginInventory inventory) {
            inventory.whenClicked(event);
        }
    }
}
