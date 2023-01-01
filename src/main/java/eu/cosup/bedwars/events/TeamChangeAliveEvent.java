package eu.cosup.bedwars.events;

import eu.cosup.bedwars.interfaces.TeamListener;
import eu.cosup.bedwars.objects.TeamColor;

import java.util.ArrayList;
import java.util.List;


public record TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {

    public TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {
        this.teamAlive = teamAlive;
        this.teamColor = teamColor;
        changeGamePhase();
    }

    private static List<TeamListener> listeners = new ArrayList<>();

    public static void addListener(TeamListener listener) {
        listeners.add(listener);
    }

    private void changeGamePhase() {
        for (TeamListener listener : listeners)
            listener.firedTeamChangeState(this);
    }
}
