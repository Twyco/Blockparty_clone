package de.twyco.blockparty;

import de.twyco.stegisagt.Util.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class PlayArea {

    private final int minX;
    private final int maxX;
    private final int minZ;
    private final int maxZ;
    private final int y;
    private final World world;
    private final Location spawn;
    private final ArrayList<Location> allLocations;

    public PlayArea() {
        Config config = Blockparty.getInstance().getBPConfig();
        Location loc1 = config.getFileConfiguration().getLocation("Blockparty.PlayArea.Location.1");
        Location loc2 = config.getFileConfiguration().getLocation("Blockparty.PlayArea.Location.2");
        this.spawn = config.getFileConfiguration().getLocation("Blockparty.Spawn.Location.Spawn");
        if (loc1 == null || loc2 == null || spawn == null) {
            throw new RuntimeException("PlayArea Locations are not set!");
        }
        this.world = loc1.getWorld();
        if (world == null) {
            throw new RuntimeException("PlayArea Locations are not set!");
        }
        this.minX = (int) Math.min(loc1.getX(), loc2.getX());
        this.maxX = (int) Math.max(loc1.getX(), loc2.getX());
        this.minZ = (int) Math.min(loc1.getZ(), loc2.getZ());
        this.maxZ = (int) Math.max(loc1.getZ(), loc2.getZ());
        this.y = (int) loc1.getY();

        allLocations = new ArrayList<>();
        for(int x = minX; x <= maxX; x++){
            for (int z = minZ; z <= maxZ; z++){
                Location location = new Location(world, x, y, z);
                allLocations.add(location);
            }
        }
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getSizeX(){
        return maxX - minX + 1;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getSizeZ(){
        return maxZ - minZ + 1;
    }

    public int getY() {
        return y;
    }

    public World getWorld() {
        return world;
    }

    public Location getSpawn() {
        return spawn;
    }

    public ArrayList<Location> getAllLocations() {
        return (ArrayList<Location>) allLocations.clone();
    }

}
