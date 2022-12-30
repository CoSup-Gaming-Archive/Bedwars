package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import org.bukkit.Bukkit;
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

        ArrayList<Player> teamPlayers = new ArrayList<>();

        for (TeamColor teamColor : TeamColor.values()) {

            for (Player player : players) {

                if (teamPlayers.size() >= players.size() / Game.getGameInstance().getSelectedMap().getTeamBeds().size()) {

                    teams.add(new Team(teamColor, teamPlayers));

                    teamPlayers = new ArrayList<>();
                }
                teamPlayers.add(player);
            }
        }
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
