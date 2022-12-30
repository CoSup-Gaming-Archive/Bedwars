package eu.cosup.bedwars;

import eu.cosup.bedwars.Managers.GameStateManager;
import eu.cosup.bedwars.Managers.TeamManager;
import eu.cosup.bedwars.Objects.LoadedMap;
import eu.cosup.bedwars.Objects.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    private static Game gameInstance;
    private ArrayList<Player> joinedPlayers = new ArrayList<>();
    private ArrayList<Player> playerList = new ArrayList<>();
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private LoadedMap selectedMap;

    public Game(LoadedMap selectedMap) {
        gameInstance = this;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();

        this.selectedMap = selectedMap;

        joinedPlayers = new ArrayList<>(Bedwars.getInstance().getServer().getOnlinePlayers());
        refreshPlayerCount();

        initGame();
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

        // TODO new ActivateGameTask(joinedPlayers).runTask(Cores.getInstance());
        // TODO GameTimerTask.resetTimer();
        // TODO new GameTimerTask().runTask(Cores.getInstance());

    }

    public void finishGame(TeamColor winner) {

        // TODO new GameEndTask(winner).runTask(Bedwars.getInstance());

    }

    public ArrayList<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    // returns boolean -> is the joined players > required players
    // to check how many players are on the cores game
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
                Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.JOINING);

                Component msg = Component.text().content("Stopping!").color(NamedTextColor.YELLOW).build();

                Bedwars.getInstance().getServer().broadcast(msg);
            }
            Component msg = Component.text().content("Not enough players: (").color(NamedTextColor.RED)
                    .append(Component.text().content(String.valueOf(joinedPlayers.size())).color(NamedTextColor.RED))
                    .append(Component.text().content("/").color(NamedTextColor.RED))
                    .append(Component.text().content(String.valueOf(Bedwars.getInstance().getConfig().getInt("required-player-count"))).color(NamedTextColor.RED)
                            .append(Component.text().content(")").color(NamedTextColor.RED))).build();
            Bedwars.getInstance().getServer().broadcast(msg);
            return;
        }

        // just saving so we can cancel it later
        // TODO new StartCountdownTask().runTask(Bedwars.getInstance());
    }
}
