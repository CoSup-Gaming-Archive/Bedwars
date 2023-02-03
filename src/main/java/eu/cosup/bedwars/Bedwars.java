package eu.cosup.bedwars;

import eu.cosup.bedwars.commands.OpenShopCommand;
import eu.cosup.bedwars.commands.OpenUpgradesCommand;
import eu.cosup.bedwars.listeners.StartGameCommandListener;
import eu.cosup.bedwars.listeners.*;
import eu.cosup.bedwars.listeners.custom.*;
import eu.cosup.bedwars.listeners.PlayerDeathListener;
import eu.cosup.bedwars.listeners.PlayerJoinListener;
import eu.cosup.bedwars.listeners.PlayerLeaveListener;
import eu.cosup.bedwars.listeners.PlayerMoveListener;
import eu.cosup.bedwars.managers.ScoreBoardManager;

import eu.cosup.bedwars.objects.LoadedMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private static Bedwars instance;
    private World gameWorld;
    private Game game;

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

        // initial creation of game.
        if (!createGame()) {
            return;
        }

        if (Game.getGameInstance().getShopManager().items.size() == 0){
            Bukkit.getLogger().severe("We'Re not able to load the itemshop canceled game");
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
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractWithChestListener(), this);
        getServer().getPluginManager().registerEvents(new TNTPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PearlTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerShootFireballListener(), this);

        new StartGameCommandListener();
        new EndGameCommandListener();
        new GameFreezeListener();
        new GameUnfreezeListener();

        new GameChangePhaseListener();
        new TeamChangeAliveListener();

        getCommand("os").setExecutor(new OpenShopCommand());
        getCommand("openshop").setExecutor(new OpenShopCommand());
        getCommand("ou").setExecutor(new OpenUpgradesCommand());
        getCommand("openupgrades").setExecutor(new OpenUpgradesCommand());
    }

    @Override
    public void onDisable() {
        // so this doesnt accidentaly show up next time if the server was to crash
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("bedwars");
        scoreBoardManager.clearObjective();
        instance = null;
    }


    public boolean createGame() {
        LoadedMap selectedMap = LoadedMap.loadMapFromConfig();
        if (selectedMap == null) {
            Bukkit.getLogger().severe("We were not able to create a game.");
            return false;
        }
        game = new Game(selectedMap);
        selectedMap.refreshTeamBedsFull();
        Bukkit.getLogger().warning("Succesfully started a game.");
        return true;
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
