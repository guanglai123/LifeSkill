package org.guanglai.lifeskill.gui;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.guanglai.lifeskill.LifeSkill;

import java.util.List;

public class BackPack extends PluginInventory {

    private final int[] collectionSlots = {10, 12, 14, 16, 28, 30, 32, 34};
    private final int[] glassSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 13, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29, 31, 33, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};

    public BackPack(Player player) {
        super(player);
    }

    @Override
    public Inventory getInventory() {

        int min = (page - 1) * collectionSlots.length;
        int max = page * collectionSlots.length;
        int a = 0;

        Inventory inv = Bukkit.createInventory(this, 54, ChatColor.of("#8B4513") + "" + ChatColor.BOLD + "素材背包");

        List<ItemStack> collectionList = LifeSkill.instance.getUserDataManager().getPlayerCollections(player);

        for (int i = min; i < Math.min(max, collectionList.size()); i++) {
            inv.setItem(collectionSlots[a++], collectionList.get(i));
        }

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName("");
        glass.setItemMeta(glassMeta);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.GREEN + "下一頁");
        next.setItemMeta(nextMeta);

        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(ChatColor.GREEN + "上一頁");
        previous.setItemMeta(previousMeta);

        for (int glassSlot : glassSlots) {
            inv.setItem(glassSlot, glass);
        }

        inv.setItem(45, page > 1 ? previous : null);
        inv.setItem(53, max >= collectionList.size() ? null : next);

        return inv;
    }

    @Override
    public void whenClicked(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "下一頁")) {
            page++;
            open();
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "上一頁")) {
            page--;
            open();
        }
    }
}
