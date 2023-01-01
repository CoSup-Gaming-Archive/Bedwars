package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.TeamChangeAliveEvent;
import eu.cosup.bedwars.interfaces.TeamListenerInterface;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.weather.LightningStrikeEvent;


public class TeamChangeAliveListener implements TeamListenerInterface {

    public TeamChangeAliveListener() {
        TeamChangeAliveEvent.addListener(this);
    }


    @Override
    public void firedTeamChangeState(TeamChangeAliveEvent event) {

        if (event.teamAlive()) {
            return;
        }



        Bedwars.getInstance().getGameWorld().spawnEntity(
                Game.getGameInstance().getSelectedMap().getSpawnByColor(event.teamColor()),
                EntityType.LIGHTNING
        );

    }
}
