package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.events.TeamChangeAliveEvent;
import eu.cosup.bedwars.interfaces.TeamListenerInterface;


public class TeamChangeAliveListener implements TeamListenerInterface {

    public TeamChangeAliveListener() {
        TeamChangeAliveEvent.addListener(this);
    }


    @Override
    public void firedTeamChangeState(TeamChangeAliveEvent event) {

    }
}
