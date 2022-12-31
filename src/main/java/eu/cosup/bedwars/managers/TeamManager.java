package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeamManager {

    private ArrayList<Team> teams = new ArrayList<>();

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // this will probably be thrown away later
    public void makeTeams(ArrayList<Player> players) {

        int index = 0;
        for (TeamColor teamColor : Game.getGameInstance().getSelectedMap().getTeamSpawns().keySet()) {

            if (players.size() == 1) {
                teams.add(new Team(teamColor, players, true));
                break;
            }

            if (players.size() > 1) {

                List<Player> teamPlayers = players.subList(index, players.size() / Game.getGameInstance().getSelectedMap().getTeamSpawns().keySet().size() + index);

                ArrayList<Player> teamPlayersArrayList = new ArrayList<>(teamPlayers.stream().filter(player -> whichTeam(player) == null).toList());

                if (teamPlayersArrayList.size() > 0) {
                    teams.add(new Team(teamColor, teamPlayersArrayList, true));
                } else {
                    teams.add(new Team(teamColor, new ArrayList<>(), false));
                }

                index+=players.size() / Game.getGameInstance().getSelectedMap().getTeamSpawns().keySet().size();
            }
        }


        for (TeamColor teamColor : TeamColor.values()) {
            // add all the dead teams
            if (Game.getGameInstance().getTeamManager().getTeamByColor(teamColor) == null) {
                teams.add(new Team(teamColor, new ArrayList<>(), false));
            }
        }

        for (Team team : teams) {
            Bukkit.getLogger().info(team.getPlayers()+"");
        }

    }

    // which team player is in
    public Team whichTeam(Player player) {

        if (player == null) {
            return null;
        }

        for (Team team : teams) {
            if (team.isPlayerInTeam(player)) {
                return team;
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
