package eu.cosup.bedwars;

import eu.cosup.bedwars.managers.*;
import eu.cosup.bedwars.objects.LoadedMap;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import eu.cosup.bedwars.tasks.GameEndTask;
import eu.cosup.bedwars.tasks.GameTimerTask;
import eu.cosup.bedwars.tasks.StartCountdownTask;
import eu.cosup.tournament.server.TournamentServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Game {

    private static Game gameInstance;
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private ShopManager shopManager;
    private UpgradesManager upgradesManager;
    private LoadedMap selectedMap;
    private BlockManager blockManager;
    private ItemGeneratorManager itemGeneratorManager;
    private ChestManager chestManager;

    public Game(LoadedMap selectedMap) {
        gameInstance = this;
        this.selectedMap = selectedMap;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();
        blockManager = new BlockManager();
        itemGeneratorManager = new ItemGeneratorManager();
        shopManager=new ShopManager();
        shopManager.loadConfig();
        chestManager = new ChestManager();
        upgradesManager=new UpgradesManager();

        initGame();
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public UpgradesManager getUpgradesManager(){
        return upgradesManager;
    }

    public ChestManager getChestManager() {
        return chestManager;
    }

    public ItemGeneratorManager getItemGeneratorManager() {
        return itemGeneratorManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public static Game getGameInstance() {
        return gameInstance;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }


    public LoadedMap getSelectedMap() {
        return selectedMap;
    }

    // loading and joining phase
    private void initGame() {

        // this really just a useless state
        gameStateManager.setGameState(GameStateManager.GameState.LOADING);

        gameStateManager.setGameState(GameStateManager.GameState.JOINING);

    }

    // active phase
    public void activateGame() {

        new ActivateGameTask().runTask(Bedwars.getInstance());
        GameTimerTask.resetTimer();
        new GameTimerTask().runTask(Bedwars.getInstance());

    }

    public void finishGame(@Nullable TeamColor winner) {
        new GameEndTask(winner).runTask(Bedwars.getInstance());
    }
}
