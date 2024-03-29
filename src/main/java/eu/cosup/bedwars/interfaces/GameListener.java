package eu.cosup.bedwars.interfaces;

import eu.cosup.bedwars.events.ChangeGamePhaseEvent;
import eu.cosup.bedwars.events.ChangeGameStateEvent;

public interface GameListener {
    void firedChangeGamePhaseEvent(ChangeGamePhaseEvent event);
    void firedChangeGameStateEvent(ChangeGameStateEvent event);
}
