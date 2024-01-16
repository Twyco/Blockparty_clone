package de.twyco.blockparty;

import de.twyco.blockparty.commands.BlockpartyCommand;
import de.twyco.blockparty.items.GameSettings;
import de.twyco.blockparty.items.PauseItem;
import de.twyco.blockparty.items.StopGame;
import de.twyco.blockparty.listener.*;
import de.twyco.stegisagt.GameStatus;
import de.twyco.stegisagt.Stegisagt;
import de.twyco.stegisagt.Util.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Blockparty extends JavaPlugin {

    private static Blockparty instance;
    private static final String prefix = ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "[" + ChatColor.BOLD + ChatColor.GOLD + "Blockparty" +
            ChatColor.BOLD + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    private Config bpConfig;
    private ArrayList<Player> playingPlayers;
    private ArrayList<Player> allPlayers;
    private ArrayList<Player> modPlayers;
    private PlayArea playArea;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Blockparty plugin wird geladen...");
        instance = this;
        bpConfig = new Config("Config.yml", getDataFolder());
        playArea = new PlayArea();
        registerCommands();
        registerListener();
        resetGame();
        WaveManager.reset();
    }

    @Override
    public void onDisable() {
        resetGame();
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("blockparty").setExecutor(new BlockpartyCommand());
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

    public static Blockparty getInstance() {
        return instance;
    }

    public Config getBPConfig() {
        return bpConfig;
    }

    public String getPrefix() {
        return prefix;
    }

    ////////////////////////

    public ArrayList<Player> getPlayingPlayers() {
        return playingPlayers;
    }

    public boolean isPlayingPlayer(Player player) {
        return playingPlayers.contains(player);
    }

    public void addPlayingPlayer(Player player) {
        playingPlayers.add(player);
    }

    public void removePlayingPlayer(Player player) {
        playingPlayers.remove(player);
    }

    ////////////////////////

    public ArrayList<Player> getAllPlayersPlayers() {
        return allPlayers;
    }

    public void addAllPlayer(Player player) {
        allPlayers.add(player);
    }

    public void removeAllPlayer(Player player) {
        allPlayers.remove(player);
    }

    ////////////////////////

    public ArrayList<Player> getModPlayers() {
        return modPlayers;
    }

    public boolean isModPlayer(Player player) {
        return modPlayers.contains(player);
    }

    public void addModPlayer(Player player) {
        modPlayers.add(player);
    }

    ////////////////////////

    public static void startGame(ArrayList<Player> players, ArrayList<Player> mods) {
        Location spawn = instance.getPlayArea().getSpawn();
        Stegisagt.setGameStatus(GameStatus.PLAYING_BLOCKPARTY);
        instance.setListenerSettings();
        for (Player player : players) {
            player.getInventory().clear();
            instance.addPlayingPlayer(player);
            instance.addAllPlayer(player);
            player.teleport(spawn, PlayerTeleportEvent.TeleportCause.PLUGIN);
            player.setGameMode(GameMode.ADVENTURE);
        }
        for (Player player : mods) {
            instance.addModPlayer(player);
            instance.addAllPlayer(player);
            player.getInventory().setItem(6, new PauseItem());
            player.getInventory().setItem(7, new GameSettings());
            player.getInventory().setItem(8, new StopGame());
            player.teleport(spawn, PlayerTeleportEvent.TeleportCause.PLUGIN);
            player.setGameMode(GameMode.CREATIVE);
        }
    }

    public void stopGame() {
        WaveManager.stop();
        WaveManager.removeAllFromBossBar();
        Stegisagt.setGameStatus(GameStatus.PLAYING);
        for (Player player : getInstance().getModPlayers()) {
            Stegisagt.getInstance().giveModItems(player);
            teleportBack(player);
        }
        for (Player player : getInstance().getPlayingPlayers()) {
            player.getInventory().clear();
            teleportBack(player);
        }
        instance.resetGame();
    }

    private void teleportBack(Player player) {
        Stegisagt.getInstance().setPlayerVisibility(true);
        player.sendTitle("", ChatColor.RED + "Teleport in 5 Sekunden.", 20, 30, 20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable() {
            @Override
            public void run() {
                if (Stegisagt.isDead(player.getUniqueId())) {
                    Stegisagt.revivePlayer(player);
                }
                player.setGameMode(GameMode.SURVIVAL);
                Stegisagt.teleportToAliveOrDead(player);
            }
        }, 5 * 20L);
    }

    public static void killPlayer(Player player) {
        instance.removePlayingPlayer(player);
        instance.removeAllPlayer(player);
        WaveManager.removePlayerFromBossBar(player);
    }

    private void resetGame() {
        playingPlayers = new ArrayList<>();
        allPlayers = new ArrayList<>();
        modPlayers = new ArrayList<>();
        WaveManager.reset();
    }

    public void setListenerSettings() {
        Stegisagt.getInstance().setPvp(false);
        Stegisagt.getInstance().setFallDamage(false);
        Stegisagt.getInstance().setHunger(false);
        Stegisagt.getInstance().setBuildPlace(false);
        Stegisagt.getInstance().setBuildBreak(false);
        Stegisagt.getInstance().setBlockDrop(false);
        Stegisagt.getInstance().setPlayerCollision(false);
        Stegisagt.getInstance().setEntityDrop(false);
        Stegisagt.getInstance().setPlayerVisibility(false);
    }

    public PlayArea getPlayArea() {
        return playArea;
    }
}
