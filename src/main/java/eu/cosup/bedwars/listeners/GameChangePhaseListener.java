package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.ChangeGamePhaseEvent;
import eu.cosup.bedwars.events.ChangeGameStateEvent;
import eu.cosup.bedwars.interfaces.GameListenerInterface;
import eu.cosup.bedwars.managers.GameStateManager;
import org.bukkit.Bukkit;

// this could be usefull for later
public class GameChangePhaseListener implements GameListenerInterface {

    public GameChangePhaseListener() {
        ChangeGamePhaseEvent.addListener(this);
        ChangeGameStateEvent.addListener(this);
    }

    @Override
    public void firedChangeGamePhaseEvent(ChangeGamePhaseEvent event) {

        if (event.newGamePhase().equals(GameStateManager.GamePhase.FIRST_UPGRADE)) {
            Bukkit.getLogger().info("FIRST UPGRADE NOW!");
        }
    }

    @Override
    public void firedChangeGameStateEvent(ChangeGameStateEvent event) {

        if (event.newGameState() == event.oldGameState()) {
            return;
        }

        Bukkit.getLogger().info(""+event.newGameState());

        if (event.newGameState().equals(GameStateManager.GameState.ACTIVE)) {
            Game.getGameInstance().activateGame();
        }

        if (event.newGameState().equals(GameStateManager.GameState.JOINING)) {
            Game.getGameInstance().refreshPlayerCount();
        }

        if (event.newGameState().equals(GameStateManager.GameState.ENDING)) {
            Game.getGameInstance().finishGame(Game.getGameInstance().getTeamManager().getAliveTeam().getColor());
        }

    }
}
