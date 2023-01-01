package eu.cosup.bedwars.events;

import eu.cosup.bedwars.interfaces.TeamListenerInterface;
import eu.cosup.bedwars.objects.TeamColor;

import java.util.ArrayList;
import java.util.List;


public record TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {

    public TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {
        this.teamAlive = teamAlive;
        this.teamColor = teamColor;
        changeGamePhase();
    }

    private static List<TeamListenerInterface> listeners = new ArrayList<>();

    public static void addListener(TeamListenerInterface listener) {
        listeners.add(listener);
    }

    private void changeGamePhase() {
        for (TeamListenerInterface listener : listeners)
            listener.firedTeamChangeState(this);
    }
}
