package eu.cosup.bedwars.interfaces;

import eu.cosup.bedwars.events.TeamChangeAliveEvent;

public interface TeamListenerInterface {

    void firedTeamChangeState(TeamChangeAliveEvent event);

}
