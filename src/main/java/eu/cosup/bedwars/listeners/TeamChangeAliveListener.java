package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.events.TeamChangeAliveEvent;
import eu.cosup.bedwars.interfaces.TeamListener;


public class TeamChangeAliveListener implements TeamListener {

    public TeamChangeAliveListener() {
        TeamChangeAliveEvent.addListener(this);
    }


    @Override
    public void firedTeamChangeState(TeamChangeAliveEvent event) {

    }
}
