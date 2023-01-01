package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.TeamChangeAliveEvent;
import eu.cosup.bedwars.interfaces.TeamListener;
import org.bukkit.Location;
import org.bukkit.Material;


public class TeamChangeAliveListener implements TeamListener {

    public TeamChangeAliveListener() {
        TeamChangeAliveEvent.addListener(this);
    }


    @Override
    public void firedTeamChangeState(TeamChangeAliveEvent event) {

        if (event.teamAlive()) {
            return;
        }

        Location bedLocation = Game.getGameInstance().getSelectedMap().getTeamBeds().get(event.teamColor());

        Bedwars.getInstance().getGameWorld().setType(bedLocation, Material.AIR);

        if (Game.getGameInstance().getTeamManager().onlyOneTeamAlive()) {
            Game.getGameInstance().finishGame(Game.getGameInstance().getTeamManager().getAliveTeam().getColor());
        }

    }

}
