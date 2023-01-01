package eu.cosup.bedwars.events;

import eu.cosup.bedwars.interfaces.Listener;
import eu.cosup.bedwars.managers.GameStateManager;

import java.util.ArrayList;
import java.util.List;

public record ChangeGameStateEvent(GameStateManager.GameState newGameState, GameStateManager.GameState oldGameState) {

    public ChangeGameStateEvent(GameStateManager.GameState newGameState, GameStateManager.GameState oldGameState) {
        this.newGameState = newGameState;
        this.oldGameState = oldGameState;
        changeGameState();
    }

    private static List<Listener> listeners = new ArrayList<>();

    public static void addListener(Listener listener) {
        listeners.add(listener);
    }

    private void changeGameState() {
        for (Listener listener : listeners)
            listener.firedChangeGameStateEvent(this);
    }
}
