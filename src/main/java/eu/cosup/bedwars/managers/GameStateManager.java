package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.events.ChangeGamePhaseEvent;
import eu.cosup.bedwars.events.ChangeGameStateEvent;

public class GameStateManager {

    private GameState gameState;
    private GamePhase gamePhase;

    public void setGameState(GameState newGameState) {
        new ChangeGameStateEvent(newGameState, this.gameState);
        this.gameState = newGameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGamePhase(GamePhase newGamePhase) {
        new ChangeGamePhaseEvent(newGamePhase, this.gamePhase);
        this.gamePhase = newGamePhase;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public enum GameState {
        LOADING,
        JOINING,
        STARTING,
        ACTIVE,
        ENDING
    }

    public enum GamePhase {

        BEFORE_UPGRADE,
        FIRST_UPGRADE,
        SECOND_UPGRADE,
        BED_DESTRUCTION,
        SUDDEN_DEATH
    }
}