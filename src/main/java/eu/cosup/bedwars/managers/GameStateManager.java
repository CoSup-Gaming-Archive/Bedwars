package eu.cosup.bedwars.managers;

public class GameStateManager {

    private GameState gameState;

    public void setGameState(GameState gamestate) {
        this.gameState = gamestate;
    }

    public GameState getGameState() {
        return gameState;
    }

    public enum GameState {
        LOADING,
        JOINING,
        STARTING,
        ACTIVE,
        ENDING
    }
}