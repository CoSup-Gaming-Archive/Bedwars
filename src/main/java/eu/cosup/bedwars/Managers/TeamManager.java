package eu.cosup.bedwars.Managers;

import eu.cosup.bedwars.Objects.Team;
import eu.cosup.bedwars.Objects.TeamColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class TeamManager {

    private ArrayList<Team> teams = new ArrayList<>();

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // this will probably be thrown away later
    public void makeTeams(ArrayList<Player> players) {
        // TODO make teams
    }

    // which team player is in
    public TeamColor whichTeam(Player player) {

        if (player == null) {
            return null;
        }

        for (Team team : teams) {
            if (team.isPlayerInTeam(player)) {
                return team.getColor();
            }
        }

        return null;

    }

    public Team getTeamByColor(TeamColor teamColor) {

        for (Team team : teams) {
            if (Objects.equals(team.getColor().toString(), teamColor.toString())) {
                return team;
            }
        }
        return null;
    }
}
