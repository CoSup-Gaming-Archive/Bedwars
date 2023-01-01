package eu.cosup.bedwars;

import eu.cosup.bedwars.managers.BlockManager;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.TeamManager;
import eu.cosup.bedwars.objects.LoadedMap;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import eu.cosup.bedwars.tasks.GameEndTask;
import eu.cosup.bedwars.tasks.GameTimerTask;
import eu.cosup.bedwars.tasks.StartCountdownTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    private static Game gameInstance;
    private ArrayList<Player> joinedPlayers = new ArrayList<>();
    private ArrayList<Player> playerList = new ArrayList<>();
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private LoadedMap selectedMap;
    private BlockManager blockManager;

    public Game(LoadedMap selectedMap) {
        gameInstance = this;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();
        blockManager = new BlockManager();

        this.selectedMap = selectedMap;

        joinedPlayers = new ArrayList<>(Bedwars.getInstance().getServer().getOnlinePlayers());

        initGame();
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

        new ActivateGameTask(joinedPlayers).runTask(Bedwars.getInstance());
        GameTimerTask.resetTimer();
        new GameTimerTask().runTask(Bedwars.getInstance());

    }

    public void finishGame(TeamColor winner) {

        new GameEndTask(winner).runTask(Bedwars.getInstance());

    }

    public ArrayList<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    // returns boolean -> is the joined players > required players
    // to check how many players are on the Bedwars game
    public void refreshPlayerCount() {

        // if the game already started
        if (
                gameStateManager.getGameState() != GameStateManager.GameState.JOINING &&
                gameStateManager.getGameState() != GameStateManager.GameState.STARTING
        ) {
            return;
        }

        if (joinedPlayers.size() < Bedwars.getInstance().getConfig().getInt("required-player-count")) {
            // this means there is already a countdown going
            if (Game.gameInstance.gameStateManager.getGameState() == GameStateManager.GameState.STARTING) {
                Component msg = Component.text().content("Stopping!").color(NamedTextColor.YELLOW).build();

                Bedwars.getInstance().getServer().broadcast(msg);
            }
            Component msg = Component.text().content("Not enough players: (").color(NamedTextColor.RED)
                    .append(Component.text().content(String.valueOf(joinedPlayers.size())).color(NamedTextColor.RED))
                    .append(Component.text().content("/").color(NamedTextColor.RED))
                    .append(Component.text().content(String.valueOf(Bedwars.getInstance().getConfig().getInt("required-player-count"))).color(NamedTextColor.RED)
                            .append(Component.text().content(")").color(NamedTextColor.RED))).build();
            Bedwars.getInstance().getServer().broadcast(msg);
            Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.JOINING);
            return;
        }

        // just saving so we can cancel it later
        new StartCountdownTask().runTask(Bedwars.getInstance());
    }
}
