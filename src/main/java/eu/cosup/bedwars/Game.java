package eu.cosup.bedwars;

import eu.cosup.bedwars.managers.*;
import eu.cosup.bedwars.objects.LoadedMap;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import eu.cosup.bedwars.tasks.GameEndTask;
import eu.cosup.bedwars.tasks.GameTimerTask;
import eu.cosup.bedwars.tasks.StartCountdownTask;
import eu.cosup.bedwars.utility.NameTagEditor;
import eu.cosup.tournament.server.TournamentServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
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
    private ScoreBoardManager scoreBoardManager;

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
        scoreBoardManager=new ScoreBoardManager("bedwars");

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
    public ScoreBoardManager getScoreBoardManager(){return scoreBoardManager;}


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
    //Didn't know where else to put it
    public void updatePlayersNameTag(Player player){
        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor();
        NameTagEditor nameTagEditor = new NameTagEditor(player);
        //nameTagEditor.setPrefix(ChatColor.translateAlternateColorCodes('&', "&7 [&f"+Math.round(player.getHealth())+"&c♥&7]"));
        nameTagEditor.setNameColor(TeamColor.getChatColor(teamColor)).setPrefix(teamColor.toString()+" ").setSuffix(ChatColor.translateAlternateColorCodes('&', "&7 [&f"+Math.round(player.getHealth())+"&c\u2764&7]")) .setTabName(TeamColor.getChatColor(teamColor)+player.getName()).setChatName((TeamColor.getChatColor(teamColor)+player.getName()));
    }

    public void finishGame(@Nullable TeamColor winner) {
        new GameEndTask(winner).runTask(Bedwars.getInstance());
    }
}
