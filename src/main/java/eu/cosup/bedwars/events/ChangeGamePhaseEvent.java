package eu.cosup.bedwars.events;

import eu.cosup.bedwars.interfaces.GameListener;
import eu.cosup.bedwars.managers.GameStateManager;

import java.util.ArrayList;
import java.util.List;

public record ChangeGamePhaseEvent(GameStateManager.GamePhase newGamePhase, GameStateManager.GamePhase oldGamePhase) {

    public ChangeGamePhaseEvent(GameStateManager.GamePhase newGamePhase, GameStateManager.GamePhase oldGamePhase) {
        this.newGamePhase = newGamePhase;
        this.oldGamePhase = oldGamePhase;
        changeGamePhase();
    }

    private static List<GameListener> listeners = new ArrayList<>();

    public static void addListener(GameListener listener) {
        listeners.add(listener);
    }

    private void changeGamePhase() {
        for (GameListener listener : listeners)
            listener.firedChangeGamePhaseEvent(this);
    }
}