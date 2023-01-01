package eu.cosup.bedwars.objects;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private ArrayList<ItemGenerator> itemGenerators;

    public LoadedMap(String name, HashMap<TeamColor, Location> teamSpawns, HashMap<TeamColor, Location> teamBeds, Location spectatorSpawn, int maxHeight, int minHeight, int deathHeight
                    , int xMax, int xMin, int zMax, int zMin, ArrayList<ItemGenerator> itemGenerators) {

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
        this.itemGenerators = itemGenerators;
    }

    public HashMap<TeamColor, Location> getTeamBeds() {
        return teamBeds;
    }

    public HashMap<TeamColor, ArrayList<Location>> getTeamBedsFull() {

        HashMap<TeamColor, ArrayList<Location>> fullTeamBeds = new HashMap<>();

        for (TeamColor teamColor : getTeamBeds().keySet()) {

            ArrayList<Location> teamBeds = new ArrayList<>();
            Location originalLocation = getTeamBeds().get(teamColor);

            teamBeds.add(new Location(
                    Bedwars.getInstance().getGameWorld(),
                    originalLocation.getX() + Math.sin(Math.toRadians(originalLocation.getYaw())),
                    originalLocation.getY(),
                    originalLocation.getZ() + Math.cos(Math.toRadians(originalLocation.getYaw()))
            ).toBlockLocation());

            teamBeds.add(originalLocation);

            fullTeamBeds.put(teamColor, teamBeds);
        }

        return fullTeamBeds;
    }

    public HashMap<TeamColor, Location> getTeamSpawns() {
        return teamSpawns;
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

    public Location getSpawnByPlayer(Player player) {

        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player).getColor();

        return teamSpawns.get(teamColor);
    }

    public Team whichTeamBed(Location location) {
        for (TeamColor teamColor : getTeamBedsFull().keySet()) {
            for (Location bedLocation : getTeamBedsFull().get(teamColor)) {
                if (Objects.equals(bedLocation.toBlockLocation().toString(), location.toBlockLocation().toString())) {
                    return Game.getGameInstance().getTeamManager().getTeamByColor(teamColor);
                }
            }
        }
        return null;
    }

    public ArrayList<ItemGenerator> getItemGenerators() {
        return itemGenerators;
    }

    public Location getSpawnByColor(TeamColor teamColor) {
        return teamSpawns.get(teamColor);
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

            Location spawnLocation = teamSpawnsSection.getLocation(teamKey + ".spawn");
            Location bedLocation = teamSpawnsSection.getLocation(teamKey + ".bed");

            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(teamKey.toUpperCase());
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().severe("No such team color exists as: " + teamKey + " from map: " + name+" therefore we didnt register map");
                return null;
            }


            spawns.put(teamColor, spawnLocation);
            beds.put(teamColor, bedLocation);

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

        ConfigurationSection generatorsList = customConfig.getConfigurationSection(name+".generators");

        ArrayList<ItemGenerator> generators = new ArrayList<>();

        if (generatorsList != null) {
            for (String key : generatorsList.getKeys(false)) {
                ConfigurationSection itemGeneratorSection = customConfig.getConfigurationSection(name+".generators."+key);
                if (itemGeneratorSection == null) {
                    Bukkit.getLogger().severe("Didnt load generator by name: "+key);
                    continue;
                }
                generators.add(ItemGenerator.deserialize(itemGeneratorSection));
            }
        } else {
            Bukkit.getLogger().severe("There was no generators path in your maps.yml for map: "+name);
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
                zMin,
                generators
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
