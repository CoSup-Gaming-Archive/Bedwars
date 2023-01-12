package eu.cosup.bedwars.interfaces;

import eu.cosup.bedwars.events.TeamChangeAliveEvent;

public interface TeamListener {

    void firedTeamChangeState(TeamChangeAliveEvent event);

}
