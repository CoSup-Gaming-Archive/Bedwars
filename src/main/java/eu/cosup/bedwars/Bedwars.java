package eu.cosup.bedwars;

import eu.cosup.bedwars.commands.ForceStartCommand;
import eu.cosup.bedwars.commands.GeneratorSpawnCommand;
import eu.cosup.bedwars.commands.SpectatorCommand;
import eu.cosup.bedwars.listeners.*;
import eu.cosup.bedwars.commands.openshop;
import eu.cosup.bedwars.listeners.PlayerDeathListener;
import eu.cosup.bedwars.listeners.PlayerJoinListener;
import eu.cosup.bedwars.listeners.PlayerLeaveListener;
import eu.cosup.bedwars.listeners.PlayerMoveListener;
import eu.cosup.bedwars.managers.ScoreBoardManager;
import eu.cosup.bedwars.listeners.*;
import eu.cosup.bedwars.managers.ShopManager;

import eu.cosup.bedwars.objects.LoadedMap;
import eu.cosup.bedwars.data.WorldLoader;
import eu.cosup.bedwars.utility.ShopItemsUtility;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        if (Game.getGameInstance().getShopManager().items.size() == 0){
            Bukkit.getLogger().severe("We'Re not able to load the itemshop");
            return;
        }

        // register all the listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new ItemThrowListener(), this);
        getServer().getPluginManager().registerEvents(new HungerReceiveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractWithEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractWithInventoryListener(), this);

        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);


        new GameChangePhaseListener();
        new TeamChangeAliveListener();

        // register all the commands
        Objects.requireNonNull(getCommand("spectate")).setExecutor(new SpectatorCommand());
        Objects.requireNonNull(getCommand("forcestart")).setExecutor(new ForceStartCommand());
        Objects.requireNonNull(getCommand("generator")).setExecutor(new GeneratorSpawnCommand());


        getCommand("spectate").setExecutor(new SpectatorCommand());
        getCommand("forcestart").setExecutor(new ForceStartCommand());
        getCommand("openshop").setExecutor(new openshop());
        getCommand("os").setExecutor(new openshop());
    }

    @Override
    public void onDisable() {
        // so this doesnt accidentaly show up next time if the server was to crash
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("bedwars");
        scoreBoardManager.clearObjective();
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
        LoadedMap selectedMap;
        selectedMap = loadedMaps.get(0);


        // if there are more maps to choose from
        if (loadedMaps.size() > 1) {
            /*int selection = random.nextInt(loadedMaps.size());
            Bukkit.getLogger().warning("Choosing from: " + loadedMaps + " chose:" + selection);
            selectedMap = loadedMaps.get(selection);*/
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
