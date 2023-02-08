package eu.cosup.bedwars.objects;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LoadedMap {

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
    private ArrayList<PrivateChest> privateChests;
    private ArrayList<TeamChest> teamChests;
    private HashMap<TeamColor, ArrayList<Location>> teamBedsFull;
    private int baseDetectionRadius;

    public LoadedMap(HashMap<TeamColor, Location> teamSpawns, HashMap<TeamColor, Location> teamBeds, Location spectatorSpawn, int maxHeight, int minHeight, int deathHeight
                    , int xMax, int xMin, int zMax, int zMin, ArrayList<ItemGenerator> itemGenerators,
                     ArrayList<PrivateChest> privateChests, ArrayList<TeamChest> teamChests, int baseDetectionRadius
    ) {
        this.teamSpawns = teamSpawns;
        this.teamBeds = teamBeds;
        this.spectatorSpawn = spectatorSpawn;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.deathHeight = deathHeight;
        this.xMax = xMax;
        this.xMin = xMin;
        this.zMax = zMax;
        this.zMin = zMin;
        this.itemGenerators = itemGenerators;
        this.privateChests = privateChests;
        this.teamChests = teamChests;
        this.baseDetectionRadius=baseDetectionRadius;
    }
    public int getBaseDetectionRadius() {
        return baseDetectionRadius;
    }

    public HashMap<TeamColor, Location> getTeamBeds() {
        return teamBeds;
    }

    public HashMap<TeamColor, ArrayList<Location>> getTeamBedsFull() {
        return teamBedsFull;
    }

    // This is very recource intense
    public HashMap<TeamColor, ArrayList<Location>> refreshTeamBedsFull() {

        HashMap<TeamColor, ArrayList<Location>> fullTeamBeds = new HashMap<>();

        for (TeamColor teamColor : getTeamBeds().keySet()) {

            ArrayList<Location> teamBeds = new ArrayList<>();
            Location originalLocation = getTeamBeds().get(teamColor);

            for (double i = 0; i < 2*Math.PI; i+=Math.PI/4) {

                Location checkLocation = (new Location(
                        Bedwars.getInstance().getGameWorld(),
                        originalLocation.getX() + Math.round(Math.cos(i)),
                        originalLocation.getY(),
                        originalLocation.getZ() + Math.round(Math.sin(i))));

                if (Bedwars.getInstance().getGameWorld().getBlockAt(checkLocation).getType().toString().toLowerCase().contains("bed")) {
                    teamBeds.add(checkLocation);
                }
            }

            teamBeds.add(originalLocation);
            fullTeamBeds.put(teamColor, teamBeds);
        }

        teamBedsFull = fullTeamBeds;

        return fullTeamBeds;
    }

    public ArrayList<PrivateChest> getPrivateChests() {
        return privateChests;
    }

    public ArrayList<TeamChest> getTeamChests() {
        return teamChests;
    }

    public HashMap<TeamColor, Location> getTeamSpawns() {
        return teamSpawns;
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

        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor();

        return teamSpawns.get(teamColor);
    }

    public Team whichTeamBed(Location location) {
        for (TeamColor teamColor : getTeamBedsFull().keySet()) {
            for (Location bedLocation : getTeamBedsFull().get(teamColor)) {
                if (Objects.equals(bedLocation.toBlockLocation().toVector(), location.toBlockLocation().toVector())) {
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

    public static LoadedMap loadMapFromConfig() {

        File configFile = new File(Bedwars.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            Bukkit.getLogger().severe("There is no maps.yml in datafolder while trying to load a map");
            return null;
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);


        int maxHeight = customConfig.getInt("maxHeight");
        int minHeight = customConfig.getInt("minHeight");
        int deathHeight = customConfig.getInt("deathHeight");

        // idk this is not mandatory
        if (minHeight == maxHeight) {
            Bukkit.getLogger().severe("   minHeight cannot be the same as the maxHeight plase change that");
            return null;
        }

        Location spectatorSpawn = customConfig.getLocation("spectatorSpawn");

        if (spectatorSpawn == null) {
            Bukkit.getLogger().severe("spectator spawn for " + " was not loaded corectly");
            return null;
        }

        ConfigurationSection teamSpawnsSection = customConfig.getConfigurationSection("teams");

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
                Bukkit.getLogger().severe("No such team color exists as: " + teamKey + " from map: "+" therefore we didnt register map");
                return null;
            }


            spawns.put(teamColor, spawnLocation);
            beds.put(teamColor, bedLocation);

        }

        if (spawns.size() == 0) {
            Bukkit.getLogger().severe("There are no spawns for no teams therefore map was not considered.");
            return null;
        }


        int xMax = customConfig.getInt(".xMax");
        int zMax = customConfig.getInt(".zMax");
        int zMin = customConfig.getInt(".zMin");
        int xMin = customConfig.getInt(".xMin");
        int baseDetectionRadius = customConfig.getInt(".baseRadius", 15);

        if (xMax == xMin || zMax == zMin) {
            Bukkit.getLogger().severe("Make sure you have the correct version of maps.yml (check in repository)");
        }


        ConfigurationSection generatorsList = customConfig.getConfigurationSection("generators");

        ArrayList<ItemGenerator> generators = new ArrayList<>();

        if (generatorsList != null) {
            for (String key : generatorsList.getKeys(false)) {
                ConfigurationSection itemGeneratorSection = customConfig.getConfigurationSection("generators."+key);
                if (itemGeneratorSection == null) {
                    Bukkit.getLogger().severe("Didnt load generator by name: "+key);
                    continue;
                }
                generators.add(ItemGenerator.deserialize(key, itemGeneratorSection));
            }
        } else {
            Bukkit.getLogger().severe("There was no generators path in your maps.yml");
        }


        ArrayList<PrivateChest> privateChests = new ArrayList<>();

        ConfigurationSection privateChestConfiguration = customConfig.getConfigurationSection("chests.private");

        if (privateChestConfiguration == null) {
            Bukkit.getLogger().severe("Cannot load private chests");
        } else {
            for (String key : privateChestConfiguration.getKeys(false)) {
                Location privateChestLocation = privateChestConfiguration.getLocation(key);
                privateChests.add(new PrivateChest(privateChestLocation));
            }
        }


        ArrayList<TeamChest> teamChests = new ArrayList<>();

        ConfigurationSection teamChestConfiguration = customConfig.getConfigurationSection("chests.team");

        if (teamChestConfiguration == null) {
            Bukkit.getLogger().severe("Cannot load team chests");
        } else {
            for (String key : teamChestConfiguration.getKeys(false)) {
                Location teamChestLocation = teamChestConfiguration.getLocation(key);
                teamChests.add(new TeamChest(TeamColor.valueOf(key.toUpperCase()),teamChestLocation));
            }
        }

        return new LoadedMap(
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
                generators,
                privateChests,
                teamChests,
                baseDetectionRadius
        );
    }
}
