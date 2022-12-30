package eu.cosup.bedwars.Objects;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LoadedMap {

    private final String name;
    private final Location spectatorSpawn;

    private final int maxHeight;
    private final int minHeight;

    private final int deathHeight;

    private final int xMax;
    private final int xMin;
    private final int zMax;
    private final int zMin;
    private HashMap<TeamColor, Location> teamSpawns;
    private HashMap<TeamColor, Location> teamBeds;

    public LoadedMap(String name, HashMap<TeamColor, Location> teamSpawns, HashMap<TeamColor, Location> teamBeds, Location spectatorSpawn, int maxHeight, int minHeight, int deathHeight
                    , int xMax, int xMin, int zMax, int zMin) {

        this.teamSpawns = teamSpawns;
        this.teamBeds = teamBeds;
        this.name = name;
        this.spectatorSpawn = spectatorSpawn;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.deathHeight = deathHeight;
        this.xMax = xMax;
        this.xMin = xMin;
        this.zMax = zMax;
        this.zMin = zMin;
    }

    public HashMap<TeamColor, Location> getTeamSpawns() {
        return teamSpawns;
    }

    public Location getTeamSpawn(TeamColor color) {
        return teamSpawns.get(color);
    }

    public String getName() {
        return name;
    }

    public int getDeathHeight() {
        return deathHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public int getxMax() {
        return xMax;
    }

    public int getxMin() {
        return xMin;
    }

    public int getzMax() {
        return zMax;
    }

    public int getzMin() {
        return zMin;
    }

    public void saveToConfig() {

        File configFile = new File(Bedwars.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException exception) {
                Bukkit.getLogger().severe("There was no maps file and we were not able to create new one.");
            }
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        customConfig.set(name + ".maxHeight", maxHeight);
        customConfig.set(name + ".minHeight", minHeight);
        customConfig.set(name + ".deathHeight", deathHeight);

        customConfig.set(name + ".spectatorSpawn", spectatorSpawn);

        customConfig.set(name + ".xMax", xMax);
        customConfig.set(name + ".zMax", zMax);
        customConfig.set(name + ".zMin", zMin);
        customConfig.set(name + ".xMin", xMin);

        try {
            customConfig.save(configFile);
        } catch (IOException exception) {
            Bukkit.getLogger().severe("Were not able to save map to config");
        }
    }

    public static LoadedMap loadMapFromConfig(String name) {

        File configFile = new File(Bedwars.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            Bukkit.getLogger().severe("There is no maps.yml in datafolder while trying to load a map");
            return null;
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        if (!customConfig.contains(name)) {
            Bukkit.getLogger().severe("The map that we are trying to load does not exists in maps.yml");
            return null;
        }

        int maxHeight = customConfig.getInt(name + ".maxHeight");
        int minHeight = customConfig.getInt(name + ".minHeight");
        int deathHeight = customConfig.getInt(name + ".deathHeight");

        // idk this is not mandatory
        if (minHeight == maxHeight) {
            Bukkit.getLogger().severe(name + "   minHeight cannot be the same as the maxHeight plase change that");
            return null;
        }

        Location spectatorSpawn = customConfig.getLocation(name + ".spectatorSpawn");

        if (spectatorSpawn == null) {
            Bukkit.getLogger().severe("spectator spawn for " + name + " was not loaded corectly");
            return null;
        }

        ConfigurationSection teamSpawnsSection = customConfig.getConfigurationSection(name+".teams");

        HashMap<TeamColor, Location> spawns = new HashMap<>();
        HashMap<TeamColor, Location> beds= new HashMap<>();

        for (String teamKey : teamSpawnsSection.getKeys(false)) {

            // we have every team

            Location spawnLocation = teamSpawnsSection.getLocation(teamKey+".spawn");
            Location bedLocation = teamSpawnsSection.getLocation(teamKey+".bed");

            spawns.put(TeamColor.valueOf(teamKey.toUpperCase()), spawnLocation);
            beds.put(TeamColor.valueOf(teamKey.toUpperCase()), bedLocation);

        }

        if (spawns.size() == 0) {
            Bukkit.getLogger().severe("There are no spawns for no teams therefore map was not considered.");
            return null;
        }


        int xMax = customConfig.getInt(name + ".xMax");
        int zMax = customConfig.getInt(name + ".zMax");
        int zMin = customConfig.getInt(name + ".zMin");
        int xMin = customConfig.getInt(name + ".xMin");

        if (xMax == xMin || zMax == zMin) {
            Bukkit.getLogger().severe("Acording to your configuration for "+name+" players not by able to place blocks anywhere");
            Bukkit.getLogger().severe("Make sure you have the correct version of maps.yml (check in repository)");
        }

        return new LoadedMap(
                name,
                spawns,
                beds,
                spectatorSpawn,
                maxHeight,
                minHeight,
                deathHeight,
                xMax,
                xMin,
                zMax,
                zMin
        );
    }

    public static ArrayList<LoadedMap> getLoadedMapsFromConfig() {

        ArrayList<LoadedMap> loadedMaps = new ArrayList<>();

        File configFile = new File(Bedwars.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            Bukkit.getLogger().severe("There is no maps.yml in datafolder while trying to load a map");
            return null;
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        Set<String> mapNames = customConfig.getKeys(false);

        if (mapNames.size() == 0) {
            Bukkit.getLogger().severe("There are no maps in maps.yml");
            return null;
        }

        for (String name : mapNames) {

            LoadedMap loadedMap = LoadedMap.loadMapFromConfig(name);
            if (loadedMap != null) {
                loadedMaps.add(loadedMap);
            }
        }

        return loadedMaps;
    }
}
