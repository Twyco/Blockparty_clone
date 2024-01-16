package de.twyco.blockparty.listener;

import de.twyco.blockparty.WaveManager;
import de.twyco.blockparty.invetorys.SettingsInventory;
import de.twyco.blockparty.items.*;
import de.twyco.stegisagt.GameStatus;
import de.twyco.stegisagt.Stegisagt;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        if (!event.getView().getTitle().equals(new SettingsInventory().getTitle())) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        player.playSound(player, Sound.UI_BUTTON_CLICK, 2f, 1.25f);
        if (!Stegisagt.getGameStatus().equals(GameStatus.PLAYING_BLOCKPARTY)) {
            return;
        }
        for (int i = 1; i < 8; i++) {
            if (itemStack.getType().equals(new WaveItem(i).getType()) &&
                    itemMeta.equals(new WaveItem(i).getItemMeta())) { //Wave
                WaveManager.playWave(i);
                return;
            }

        }
        if (itemStack.getType().equals(new On().getType()) &&
                itemMeta.equals(new On().getItemMeta())) {
            WaveManager.setAutoplay(false);
            player.openInventory(new SettingsInventory().getInventory());
        } else if (itemStack.getType().equals(new Off().getType()) &&
                itemMeta.equals(new Off().getItemMeta())) {
            WaveManager.setAutoplay(true);
            player.openInventory(new SettingsInventory().getInventory());

        }
    }

}
