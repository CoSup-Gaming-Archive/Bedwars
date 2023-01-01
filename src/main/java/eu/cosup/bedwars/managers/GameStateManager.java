package eu.cosup.bedwars.managers;


public class GameStateManager {

    private GameState gameState;
    private GamePhase gamePhase;

    public void setGameState(GameState newGameState) {
        this.gameState = newGameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGamePhase(GamePhase newGamePhase) {
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
        FIRST_UPGRADE,
        SECOND_UPGRADE,
        THIRD_UPGRADE,
        SUDDEN_DEATH
    }
}