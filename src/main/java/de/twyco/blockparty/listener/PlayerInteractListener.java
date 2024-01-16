package de.twyco.blockparty.listener;

import de.twyco.blockparty.Blockparty;
import de.twyco.blockparty.WaveManager;
import de.twyco.blockparty.invetorys.SettingsInventory;
import de.twyco.blockparty.items.*;
import de.twyco.blockparty.items.PauseItem;
import de.twyco.blockparty.items.PlayAreaSelectionTool;
import de.twyco.blockparty.items.StopGame;
import de.twyco.stegisagt.GameStatus;
import de.twyco.stegisagt.Stegisagt;
import de.twyco.stegisagt.Util.Config;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerInteractListener implements Listener {

    private final Blockparty instance;
    private boolean stopGame;
    private boolean stopMinDelay;

    public PlayerInteractListener() {
        instance = Blockparty.getInstance();
        stopGame = false;
        stopMinDelay = false;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (Stegisagt.getGameStatus().equals(GameStatus.CLOSED)) {
            Action action = event.getAction();
            if (!action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }
            Player player = event.getPlayer();
            if (!player.isOp()) {
                return;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.getItemMeta() == null) {
                return;
            }
            if (is.getType().equals(new PlayAreaSelectionTool().getType())) {
                if (!is.getItemMeta().equals(new PlayAreaSelectionTool().getItemMeta())) {
                    return;
                }
                Block block = event.getClickedBlock();
                Location location = block.getLocation();
                Config config = instance.getBPConfig();
                boolean leftClick = action.equals(Action.LEFT_CLICK_BLOCK);
                if (leftClick) {//ERSTE SETZEN
                    config.getFileConfiguration().set("Blockparty.PlayArea.Location.1", location);
                    config.save();
                    player.sendMessage(instance.getPrefix() + ChatColor.GREEN +
                            "Du hast die " + ChatColor.YELLOW + "1" + ChatColor.GREEN + " Play Area Position gesetzt.");
                } else {//ZWEITE SETZEN
                    config.getFileConfiguration().set("Blockparty.PlayArea.Location.2", location);
                    config.save();
                    player.sendMessage(instance.getPrefix() + ChatColor.GREEN +
                            "Du hast die " + ChatColor.YELLOW + "2" + ChatColor.GREEN + " Play Area Position gesetzt.");
                }
                Location firstLocation = config.getFileConfiguration().getLocation("Blockparty.PlayArea.Location.1");
                Location sndLocation = config.getFileConfiguration().getLocation("Blockparty.PlayArea.Location.2");
                if (firstLocation == null || sndLocation == null) {
                    return;
                }
                if(firstLocation.getY() != sndLocation.getY()){
                    config.getFileConfiguration().set("Blockparty.PlayArea.Location.1", null);
                    config.getFileConfiguration().set("Blockparty.PlayArea.Location.2", null);
                    config.save();
                    player.sendMessage(instance.getPrefix() + ChatColor.RED +
                            "Die Locations wurde gelöscht.");
                    return;
                }
                saveArea(config, firstLocation, sndLocation);
                player.sendMessage(instance.getPrefix() + ChatColor.GREEN +
                        "Du hast die gesamte Play Area gespeichert.");
            }
        } else if (Stegisagt.getGameStatus().equals(GameStatus.PLAYING_BLOCKPARTY)) {
            Player player = event.getPlayer();
            if (!instance.isModPlayer(player)) {
                return;
            }
            if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                return;
            }
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.getItemMeta() == null) {
                return;
            }
            if (is.getType().equals(new StopGame().getType())) {
                if (is.getItemMeta().equals(new StopGame().getItemMeta())) {
                    if (stopGame && stopMinDelay) {
                        stopGame = false;
                        instance.stopGame();
                        player.sendMessage(instance.getPrefix() + ChatColor.RED + "Das Spiel wurde beendet!");
                    } else {
                        stopGame = true;
                        stopMinDelay = false;
                        instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> stopMinDelay = true, 5L);
                        instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> {
                            if (stopGame) {
                                stopGame = false;
                                player.sendMessage(instance.getPrefix() + ChatColor.RED + "zum Abbrechen Doppelklicken");
                            }
                        }, 20L);
                    }
                }
            } else if (is.getType().equals(new GameSettings().getType())) {
                player.openInventory(new SettingsInventory().getInventory());
                event.setCancelled(true);
            } else if (is.getType().equals(new PauseItem().getType())) {
                if(WaveManager.isPause()){
                    WaveManager.resumeWave();
                }else {
                    WaveManager.pauseWave();
                }
                player.getInventory().setItemInMainHand(new PauseItem());
                event.setCancelled(true);
            }
        }
    }

    private void saveArea(Config config, Location firstLocation, Location sndLocation) {
        config.getFileConfiguration().set("Blockparty.Background.Blocks", null);
        ArrayList<Block> area = new ArrayList<>();
        World world = firstLocation.getWorld();
        if (world == null) {
            return;
        }
        int minX = (int) Math.min(firstLocation.getX(), sndLocation.getX());
        int minY = (int) Math.min(firstLocation.getY(), sndLocation.getY());
        int minZ = (int) Math.min(firstLocation.getZ(), sndLocation.getZ());
        int maxX = (int) Math.max(firstLocation.getX(), sndLocation.getX());
        int maxY = (int) Math.max(firstLocation.getY(), sndLocation.getY());
        int maxZ = (int) Math.max(firstLocation.getZ(), sndLocation.getZ());
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    area.add(block);
                }
            }
        }
        int counter = 0;
        config.getFileConfiguration().set("Blockparty.Background.Blocks.counter", counter);
        for (Block block : area){
            config.getFileConfiguration().set("Blockparty.Background.Blocks." + counter + ".Location", block.getLocation());
            config.getFileConfiguration().set("Blockparty.Background.Blocks." + counter + ".BlockData", block.getBlockData().getAsString());
            counter++;
        }
        config.getFileConfiguration().set("Blockparty.Background.Blocks.counter", counter);
        config.save();
    }

}
