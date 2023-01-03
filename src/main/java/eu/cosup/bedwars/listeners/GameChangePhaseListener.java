package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.ChangeGamePhaseEvent;
import eu.cosup.bedwars.events.ChangeGameStateEvent;
import eu.cosup.bedwars.interfaces.GameListenerInterface;
import eu.cosup.bedwars.managers.GameStateManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

// this could be usefull for later
public class GameChangePhaseListener implements GameListenerInterface {

    public GameChangePhaseListener() {
        ChangeGamePhaseEvent.addListener(this);
        ChangeGameStateEvent.addListener(this);
    }

    @Override
    public void firedChangeGamePhaseEvent(ChangeGamePhaseEvent event) {

        if (event.newGamePhase() == GameStateManager.GamePhase.SUDDEN_DEATH) {
            // spawn dragons
        }

        if (event.newGamePhase() == GameStateManager.GamePhase.BED_DESTRUCTION) {
            Bedwars.getInstance().getGameWorld().playSound(
                    Game.getGameInstance().getSelectedMap().getSpectatorSpawn(),
                    Sound.ENTITY_ENDER_DRAGON_AMBIENT,
                    20,
                    1
            );
        }
    }

    @Override
    public void firedChangeGameStateEvent(ChangeGameStateEvent event) {

        if (event.newGameState().equals(GameStateManager.GameState.ACTIVE)) {
            Game.getGameInstance().activateGame();
        }

        if (event.newGameState().equals(GameStateManager.GameState.ENDING)) {
            Game.getGameInstance().finishGame(Game.getGameInstance().getTeamManager().getAliveTeam().getColor());
        }
    }
}
