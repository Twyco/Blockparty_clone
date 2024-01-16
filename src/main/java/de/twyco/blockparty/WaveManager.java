package de.twyco.blockparty;

import de.twyco.blockparty.items.ColorItem;
import de.twyco.blockparty.items.GameSettings;
import de.twyco.blockparty.items.PauseItem;
import de.twyco.blockparty.items.StopGame;
import de.twyco.stegisagt.Stegisagt;
import de.twyco.stegisagt.Util.Config;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Random;

public class WaveManager {

    private static final Blockparty instance = Blockparty.getInstance();
    private static BukkitTask bukkitTask;
    private static BossBar bossBar;
    private static boolean pause;
    private static boolean stop;
    private static boolean autoplay;
    private static final Material[] coloredBlocks = new Material[]{Material.BLACK_CONCRETE, Material.ORANGE_CONCRETE,
            Material.BLUE_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.GREEN_CONCRETE, Material.LIME_CONCRETE,
            Material.PURPLE_CONCRETE, Material.RED_CONCRETE, Material.YELLOW_CONCRETE};
    private static ArrayList<Material> usedColors;
    private static Material chosenColor;
    private static long time = 0;
    private static long maxTime = 0;
    private static long sndMaxTime = 0;
    private static long trdMaxTime = 0;
    private static int wave = 0;
    private static int round = 0;
    private static int maxRound = 0;

    public static void reset() {
        setBackground();
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
        removeAllFromBossBar();
        bukkitTask = null;
        bossBar = null;
        pause = false;
        stop = false;
        autoplay = true;
        chosenColor = null;
        time = 0;
        maxTime = 0;
        wave = 0;
        round = 0;
        maxRound = 0;
        sndMaxTime = 0;
        trdMaxTime = 0;
    }

    /////////////////////WAVE FUNCTIONS/////////////////////

    public static void playNextWave() {
        if (wave < 7) {
            playWave(wave + 1);
            return;
        }
        stop();
    }

    public static void pauseWave() {
        pause = true;
        showPauseBossBar();
    }

    public static void stop() {
        removeAllFromBossBar();
        stop = true;
        reset();
    }

    public static void resumeWave() {
        if (!pause) {
            return;
        }
        pause = false;
        showPauseBossBar();
    }

    public static void playWave(int wave) {
        stop();
        stop = false;
        WaveManager.wave = wave;
        showWaveStartBossBar();
        setBossBarProgress(100, 100);
        time = 5 * 20L;
        setBackground();
        for(Player player : instance.getModPlayers()){
            player.getInventory().setItem(6, new PauseItem());
        }
        bukkitTask = Bukkit.getScheduler().runTaskTimer(instance, () -> {
            if (time <= 0) {
                removeAllFromBossBar();
                bukkitTask.cancel();
                waveStepTwo();
            } else {
                if (!isPause()) {
                    showWaveStartBossBar();
                    double countdown = time;
                    countdown /= 20;
                    countdown *= 10;
                    countdown = (int) countdown;
                    countdown /= 10;
                    bossBar.setTitle(ChatColor.RED + ChatColor.BOLD.toString() + "Wave " + ChatColor.BLUE + ChatColor.BOLD + wave
                            + ChatColor.RED + ChatColor.BOLD + " startet in: " + ChatColor.BLUE + ChatColor.BOLD + countdown);
                    setBossBarProgress(time, 100);
                    time--;
                }
                if (stop) {
                    removeAllFromBossBar();
                    bukkitTask.cancel();
                }
            }
        }, 0L, 1L);
    }

    public static void waveStepTwo() {
        switch (wave) {
            /*
                maxRound = Anzahle der runden in einer Wave
                maxTime = Die Zeit in der die patterns "gemixt" werden
                sndMaxTime = Die Zeit, bis die Blöcke verschwien
                trdMaxTime = Die Zeit, die man wartet bis die blöcke weiderkommen
             */
            case 1 -> {
                maxRound = 5;
                maxTime = (long) (3D * 20L);
                sndMaxTime = (long) (5D * 20L);
                trdMaxTime = (long) (3D * 20L);
            }
            case 2 -> {
                maxRound = 6;
                maxTime = (long) (4D * 20L);
                sndMaxTime = (long) (4D * 20L);
                trdMaxTime = (long) (3D * 20L);
            }
            case 3 -> {
                maxRound = 7;
                maxTime = (long) (3D * 20L);
                sndMaxTime = (long) (3D * 20L);
                trdMaxTime = (long) (3D * 20L);
            }
            case 4 -> {
                maxRound = 7;
                maxTime = (long) (2.5D * 20L);
                sndMaxTime = (long) (2.5D * 20L);
                trdMaxTime = (long) (3D * 20L);
            }
            case 5 -> {
                maxRound = 8;
                maxTime = (long) (1.5D * 20L);
                sndMaxTime = (long) (1.5D * 20L);
                trdMaxTime = (long) (2D * 20L);
            }
            case 6 -> {
                maxRound = 9;
                maxTime = (long) (1D * 20L);
                sndMaxTime = (long) (1.25D * 20L);
                trdMaxTime = (long) (1.5D * 20L);
            }
            case 7 -> {
                maxRound = 10;
                maxTime = 0;
                sndMaxTime = (long) (1D * 20L);
                trdMaxTime = (long) (1.5D * 20L);
            }
        }
        round = 1;
        playRound();
    }

    public static void playWaveWon() {
        for (Player player : instance.getPlayingPlayers()) {
            player.getInventory().clear();
        }
        time = 5 * 20L;
        showWaveEndBossBar();
        setBackground();
        bukkitTask = Bukkit.getScheduler().runTaskTimer(instance, () -> {
            if (time <= 0) {
                bukkitTask.cancel();
                if (autoplay) {
                    playNextWave();
                }
            } else {
                if (!isPause()) {
                    showWaveEndBossBar();
                    setBossBarProgress(time, 5 * 20L);
                    time--;
                }
            }
            if (stop) {
                removeAllFromBossBar();
                bukkitTask.cancel();
            }
        }, 0L, 1L);
    }

    public static void playRound() {
        if (round > maxRound) {
            playWaveWon();
            return;
        }
        for(Player player : instance.getPlayingPlayers()){
            player.getInventory().clear();
        }
        time = maxTime;
        showTimerBossBar();
        randomPattern();
        bukkitTask = Bukkit.getScheduler().runTaskTimer(instance, () -> {
            if (time <= 0) {
                chooseColor();
                showColor();
                bukkitTask.cancel();
                playRoundStepTwo();
            } else {
                if (!isPause()) {
                    if (time % 15 == 0) {
                        usedColors = new ArrayList<>();
                        randomPattern();
                    }
                    showTimerBossBar();
                    setBossBarProgress(time, maxTime);
                    time--;
                }
            }
            if (stop) {
                removeAllFromBossBar();
                bukkitTask.cancel();
            }
        }, 0L, 1L);
    }

    public static void playRoundStepTwo() {

        time = sndMaxTime;
        showTimer2BossBar();
        bukkitTask = Bukkit.getScheduler().runTaskTimer(instance, () -> {
            if (time <= 0) {
                for (Player player : instance.getAllPlayersPlayers()) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, SoundCategory.BLOCKS, 1F, 2F);
                }
                removeColors();
                bukkitTask.cancel();
                playRoundStepThree();
            } else {
                if (!isPause()) {
                    showTimer2BossBar();
                    setBossBarProgress(time, sndMaxTime);
                    if (time % 20 == 0) {
                        for (Player player : instance.getAllPlayersPlayers()) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, SoundCategory.BLOCKS, 1F, 1F);
                        }
                    }
                    time--;
                }
            }
            if (stop) {
                removeAllFromBossBar();
                bukkitTask.cancel();
            }
        }, 0L, 1L);
    }

    public static void playRoundStepThree() {
        time = trdMaxTime;
        showTimer2BossBar();
        bukkitTask = Bukkit.getScheduler().runTaskTimer(instance, () -> {
            if (time <= 0) {
                round++;
                bukkitTask.cancel();
                playRound();
            } else {
                if (!isPause()) {
                    showTimer2BossBar();
                    setBossBarProgress(time, trdMaxTime);
                    time--;
                }
            }
            if (stop) {
                removeAllFromBossBar();
                bukkitTask.cancel();
            }
        }, 0L, 1L);
    }

    public static void randomPattern() {
        Random random = new Random();
        switch (wave) {
            case 1 -> {
                if (random.nextDouble() < 0.5D) {
                    int patternSize = random.nextInt(6) + 4;
                    setBlockPattern(Pattern.BLOB, patternSize);
                } else {
                    setBlockPattern(Pattern.LINE, 0);
                }
            }
            case 2 -> {
                if (random.nextDouble() < 0.6D) {
                    int patternSize = random.nextInt(6) + 3;
                    setBlockPattern(Pattern.BLOB, patternSize);
                } else {
                    setBlockPattern(Pattern.LINE, 0);
                }
            }
            case 3 -> {
                if (random.nextDouble() < 0.7D) {
                    int patternSize = random.nextInt(5) + 2;
                    setBlockPattern(Pattern.BLOB, patternSize);
                } else {
                    setBlockPattern(Pattern.LINE, 0);
                }
            }
            case 4 -> {
                if (random.nextDouble() < 0.8D) {
                    int patternSize = random.nextInt(5) + 2;
                    setBlockPattern(Pattern.BLOB, patternSize);
                } else {
                    setBlockPattern(Pattern.LINE, 0);
                }
            }
            case 5 -> {
                if (random.nextDouble() < 0.75D) {
                    int patternSize = random.nextInt(4) + 1;
                    setBlockPattern(Pattern.BLOB, patternSize);
                } else {
                    setBlockPattern(Pattern.RANDOM, 0);
                }
            }
            case 6 -> {
                int patternSize = random.nextInt(2) + 1;
                setBlockPattern(Pattern.BLOB, patternSize);
            }
            case 7 -> {
                setBlockPattern(Pattern.BLOB, 1);
            }
        }
    }

    /////////////////////COLOR FUNCTIONS/////////////////////

    public static void chooseColor() {
        Random random = new Random();
        chosenColor = usedColors.get(random.nextInt(usedColors.size()));
    }

    public static void showColor() {
        for (Player player : instance.getPlayingPlayers()) {
            player.getInventory().clear();
            for (int i = 0; i < 9; i++) {
                player.getInventory().setItem(i, new ColorItem(chosenColor));
            }
        }
    }

    /////////////////////BLOCK FUNCTIONS/////////////////////

    public static void setBlockPattern(Pattern pattern, int blockSize) {
        PlayArea playArea = instance.getPlayArea();
        World world = playArea.getWorld();
        usedColors = new ArrayList<>();
        Random random = new Random();
        switch (pattern) {
            case RANDOM -> {
                for (int x = playArea.getMinX(); x <= playArea.getMaxX(); x++) {
                    for (int z = playArea.getMinZ(); z <= playArea.getMaxZ(); z++) {
                        Material material = coloredBlocks[random.nextInt(coloredBlocks.length)];
                        usedColors.add(material);
                        world.getBlockAt(x, playArea.getY(), z).setType(material);
                    }
                }
            }
            case LINE -> {
                if (random.nextBoolean()) {
                    for (int x = playArea.getMinX(); x <= playArea.getMaxX(); x++) {
                        Material material = coloredBlocks[random.nextInt(coloredBlocks.length)];
                        usedColors.add(material);
                        for (int z = playArea.getMinZ(); z <= playArea.getMaxZ(); z++) {
                            world.getBlockAt(x, playArea.getY(), z).setType(material);
                        }
                    }
                } else {
                    for (int z = playArea.getMinZ(); z <= playArea.getMaxZ(); z++) {
                        Material material = coloredBlocks[random.nextInt(coloredBlocks.length)];
                        usedColors.add(material);
                        for (int x = playArea.getMinX(); x <= playArea.getMaxX(); x++) {
                            world.getBlockAt(x, playArea.getY(), z).setType(material);
                        }
                    }
                }
            }
            case BLOB -> {
                int rndDivider = blockSize;
                if (rndDivider == 0) {
                    int[] divider = new int[]{2, 4, 5, 8, 10, 20};
                    rndDivider = divider[random.nextInt(divider.length)];
                }
                ArrayList<Location> playAreaAllLocations = playArea.getAllLocations();
                ArrayList<ArrayList<Location>> seedList = new ArrayList<>();
                for (int x = 1; x <= playArea.getSizeX(); x++) {
                    for (int z = 1; z <= playArea.getSizeZ(); z++) {
                        if (x % rndDivider == 0 && z % rndDivider == 0) {
                            Material material = coloredBlocks[random.nextInt(coloredBlocks.length)];
                            usedColors.add(material);
                            int posX = x + playArea.getMinX() - 1 - rndDivider / 2;
                            int posZ = z + playArea.getMinZ() - 1 - rndDivider / 2;
                            Location location = new Location(world, posX, playArea.getY(), posZ);
                            world.getBlockAt(location).setType(material);
                            ArrayList<Location> seedBlocks = new ArrayList<>();
                            seedBlocks.add(location);
                            seedList.add(seedBlocks);
                            playAreaAllLocations.remove(location);
                        }
                    }
                }
                while (playAreaAllLocations.size() > 0) {
                    for (ArrayList<Location> seedBlocks : seedList) {
                        boolean placed = false;
                        if (seedBlocks.isEmpty()) {
                            continue;
                        }
                        ArrayList<Location> blob = (ArrayList<Location>) seedBlocks.clone();
                        do {
                            int rndIndex = random.nextInt(blob.size());
                            Location oldBockLoc = blob.get(rndIndex);
                            blob.remove(oldBockLoc);
                            Block oldBlock = world.getBlockAt(oldBockLoc);
                            ArrayList<BlockFace> faces = new ArrayList<>();
                            faces.add(BlockFace.NORTH);
                            faces.add(BlockFace.EAST);
                            faces.add(BlockFace.SOUTH);
                            faces.add(BlockFace.WEST);
                            for (int i = 0; i < 4; i++) {
                                BlockFace face = faces.get(random.nextInt(faces.size()));
                                faces.remove(face);
                                Block newBlock = oldBlock.getRelative(face);
                                Location newBlockLoc = newBlock.getLocation();
                                if (playAreaAllLocations.contains(newBlockLoc)) {
                                    newBlock.setType(oldBlock.getType(), true);
                                    seedBlocks.add(newBlockLoc);
                                    playAreaAllLocations.remove(newBlockLoc);
                                    placed = true;
                                    break;
                                }
                            }
                            if (faces.isEmpty() && !placed) {
                                seedBlocks.remove(oldBockLoc);
                            }
                            blob.remove(oldBockLoc);
                        } while (!placed && !blob.isEmpty());
                    }
                }

            }
            default -> throw new IllegalStateException("Unexpected value: " + pattern);
        }
    }

    public static void removeColors() {
        PlayArea playArea = instance.getPlayArea();
        World world = playArea.getWorld();
        for (int x = playArea.getMinX(); x <= playArea.getMaxX(); x++) {
            for (int z = playArea.getMinZ(); z <= playArea.getMaxZ(); z++) {
                if (!world.getBlockAt(x, playArea.getY(), z).getType().equals(chosenColor)) {
                    world.getBlockAt(x, playArea.getY(), z).setType(Material.AIR);
                }
            }
        }
    }

    public static void setBackground() {
        Config config = instance.getBPConfig();
        PlayArea playArea = instance.getPlayArea();
        World world = playArea.getWorld();
        int counter = config.getFileConfiguration().getInt("Blockparty.Background.Blocks.counter");
        for(int i = 0; i < counter; i++){
            Location blockLoc = config.getFileConfiguration().getLocation("Blockparty.Background.Blocks." + i + ".Location");
            BlockData blockData = Bukkit.createBlockData(config.getFileConfiguration().getString("Blockparty.Background.Blocks." + i + ".BlockData"));
            world.getBlockAt(blockLoc).setBlockData(blockData);
        }
    }

    /////////////////////SPECIFIC BOSSBAR/////////////////////

    public static void showTimerBossBar() {
        if (bossBar == null) {
            createBossBar();
        }
        bossBar.setColor(BarColor.BLUE);
        bossBar.setTitle(ChatColor.WHITE + ChatColor.BOLD.toString() + "Wave " + ChatColor.BLUE + ChatColor.BOLD + wave
                + ChatColor.GRAY + ChatColor.BOLD + " | " + ChatColor.WHITE + ChatColor.BOLD + "Runde " + ChatColor.BLUE +
                ChatColor.BOLD + round
                + ChatColor.GRAY + ChatColor.BOLD + "/" + ChatColor.BLUE + ChatColor.BOLD + maxRound
                + ChatColor.GRAY + ChatColor.BOLD + " | " + ChatColor.RED + ChatColor.BOLD + "NÄCHSTE FARBE IN: ");
        bossBar.setVisible(true);
    }

    public static void showTimer2BossBar() {
        if (bossBar == null) {
            createBossBar();
        }
        bossBar.setColor(BarColor.GREEN);
        String str;
        switch (chosenColor) {
            case BLACK_CONCRETE -> {
                str = ChatColor.BLACK.toString() + ChatColor.BOLD + "SCHWARZ";
            }
            case ORANGE_CONCRETE -> {
                str = ChatColor.GOLD.toString() + ChatColor.BOLD + "ORANGE";
            }
            case BLUE_CONCRETE -> {
                str = ChatColor.DARK_BLUE.toString() + ChatColor.BOLD + "BLAU";
            }
            case LIGHT_BLUE_CONCRETE -> {
                str = ChatColor.AQUA.toString() + ChatColor.BOLD + "HELLBLAU";
            }
            case GREEN_CONCRETE -> {
                str = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "GRÜN";
            }
            case LIME_CONCRETE -> {
                str = ChatColor.GREEN.toString() + ChatColor.BOLD + "HELLGRÜN";
            }
            case PURPLE_CONCRETE -> {
                str = ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "LILA";
            }
            case RED_CONCRETE -> {
                str = ChatColor.RED.toString() + ChatColor.BOLD + "ROT";
            }
            case YELLOW_CONCRETE -> {
                str = ChatColor.YELLOW.toString() + ChatColor.BOLD + "GELB";
            }
            default -> {
                str = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "ERROR";
            }
        }
        bossBar.setTitle(str);
        bossBar.setVisible(true);
    }

    public static void showPauseBossBar() {
        if (bossBar == null) {
            createBossBar();
        }
        bossBar.setColor(BarColor.YELLOW);
        bossBar.setTitle(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Pausiert");
        bossBar.setVisible(true);
    }

    public static void showWaveEndBossBar() {
        if (bossBar == null) {
            createBossBar();
        }
        bossBar.setColor(BarColor.GREEN);
        bossBar.setTitle(ChatColor.GREEN + ChatColor.BOLD.toString() + "Wave " + wave + " geschafft!");
        bossBar.setVisible(true);
    }

    public static void showWaveStartBossBar() {
        if (bossBar == null) {
            createBossBar();
        }
        bossBar.setColor(BarColor.RED);
        bossBar.setTitle(ChatColor.RED + ChatColor.BOLD.toString() + "Wave " + ChatColor.BLUE + ChatColor.BOLD + wave
                + ChatColor.RED + ChatColor.BOLD + " startet in: " + ChatColor.BLUE + ChatColor.BOLD + time);
        bossBar.setVisible(true);
    }

    private static void setBossBarProgress(long i, long j) {
        double progress = (double) i / j;
        bossBar.setProgress(progress);
    }

    /////////////////////BOSSBAR GENERAL/////////////////////

    public static void createBossBar() {
        bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        for (Player player : instance.getAllPlayersPlayers()) {
            bossBar.addPlayer(player);
        }
        bossBar.setVisible(true);
    }

    public static void removeAllFromBossBar() {
        if (bossBar == null) {
            return;
        }
        bossBar.removeAll();
        bossBar = null;
    }

    public static void removePlayerFromBossBar(Player player) {
        if (bossBar == null) {
            return;
        }
        bossBar.removePlayer(player);
    }

    /////////////////////GETTER/////////////////////

    public static boolean isAutoplay() {
        return autoplay;
    }

    public static boolean isPause() {
        return pause;
    }


    /////////////////////SETTER/////////////////////

    public static void setAutoplay(boolean autoplay) {
        WaveManager.autoplay = autoplay;
    }
}

