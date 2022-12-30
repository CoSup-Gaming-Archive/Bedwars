package eu.cosup.bedwars;

import eu.cosup.bedwars.Objects.LoadedMap;
import eu.cosup.bedwars.Data.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Bedwars extends JavaPlugin {

    private static Bedwars instance;
    private World gameWorld;
    private Game game;
    private ArrayList<LoadedMap> loadedMaps = new ArrayList<>();

    public static Bedwars getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        gameWorld = Bukkit.getWorld("world");

        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        loadMaps();

        if (loadedMaps.size() == 0) {
            Bukkit.getLogger().severe("Were not able to load any maps");
            return;
        }

        // initial creation of game.
        if (!createGame()) {
            return;
        }

        // register all the listeners


        // register all the commands
    }

    @Override
    public void onDisable() {
        instance = null;
    }


    public boolean createGame() {
        LoadedMap selectedMap = selectMap();
        if (selectedMap == null) {
            Bukkit.getLogger().severe("We were not able to create a game.");
            return false;
        }
        game = new Game(selectedMap);
        Bukkit.getLogger().warning("Succesfully started a game.");
        return true;
    }

    private LoadedMap selectMap() {

        Random random = new Random();

        // by default we get number 0
        LoadedMap selectedMap = loadedMaps.get(0);

        // if there are more maps to choose from
        if (loadedMaps.size() > 1) {
            int selection = random.nextInt(loadedMaps.size());
            Bukkit.getLogger().warning("Choosing from: " + loadedMaps + " chose:" + selection);
            selectedMap = loadedMaps.get(selection);
        }

        Bukkit.getLogger().info("Selected map: " + selectedMap.getName());

        if (!WorldLoader.loadNewWorld(selectedMap.getName())) {
            Bukkit.getLogger().severe("Not able to load map");
            return null;
        }

        return selectedMap;
    }

    private void loadMaps() {

        // we just have to have the name of the world and then we can load it from config by name
        ArrayList<LoadedMap> loadedMapsConfig = LoadedMap.getLoadedMapsFromConfig();
        if (loadedMapsConfig != null) {
            loadedMaps = loadedMapsConfig;
        }

        WorldLoader.getWorldNames();

        // this is to sort out the maps that dont exist in the folder
        List<LoadedMap> tempLoadedMaps = loadedMaps.stream().filter(loadedMap -> WorldLoader.getWorldNames().contains(loadedMap.getName())).toList();
        loadedMaps = new ArrayList<>();
        loadedMaps.addAll(tempLoadedMaps);
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public Game getGame() {
        return game;
    }
}
