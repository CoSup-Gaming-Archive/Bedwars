package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class TeamManager {

    public static boolean randomTeams = Bedwars.getInstance().getConfig().getBoolean("random-teams");
    private ArrayList<Team> teams = new ArrayList<>();

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // this will probably be thrown away later
    public void makeTeams(ArrayList<Player> players) {

        if (randomTeams) {
            int index = 0;
            for (TeamColor teamColor : Game.getGameInstance().getSelectedMap().getTeamSpawns().keySet()) {

                if (players.size() == 1) {
                    teams.add(new Team(teamColor, new ArrayList<>(players), true));
                    break;
                }

                if (players.size() > 1) {

                    List<Player> teamPlayers = players.subList(index, players.size() / Game.getGameInstance().getSelectedMap().getTeamSpawns().keySet().size() + index);

                    ArrayList<Player> teamPlayersArrayList = new ArrayList<>(teamPlayers.stream().filter(player -> whichTeam(player.getUniqueId()) == null).toList());

                    if (teamPlayersArrayList.size() > 0) {
                        teams.add(new Team(teamColor, teamPlayersArrayList, true));
                    } else {
                        teams.add(new Team(teamColor, new ArrayList<>(), false));
                    }

                    index+=players.size() / Game.getGameInstance().getSelectedMap().getTeamSpawns().keySet().size();
                }
            }
        }

        // TODO add non random teams

        for (TeamColor teamColor : TeamColor.values()) {
            // add all the dead teams
            if (Game.getGameInstance().getTeamManager().getTeamByColor(teamColor) == null) {
                teams.add(new Team(teamColor, new ArrayList<>(), false));
            }
        }
    }

    public ArrayList<Team> getAliveTeams() {
        ArrayList<Team> aliveTeams = new ArrayList<>();

        for (Team team : teams) {
            if (team.isAlive()) {
                aliveTeams.add(team);
            }
        }

        return aliveTeams;
    }

    public boolean onlyOneTeamAlive() {
        return getAliveTeams().size() < 2;
    }

    public Team getAliveTeam() {

        if (!onlyOneTeamAlive()) {
            return null;
        }

        if (getAliveTeams().size() == 0) {
            Bukkit.getLogger().severe("There was probably only one team registered at the start");
            return teams.get(0);
        }

        return getAliveTeams().get(0);
    }

    // which team player is in
    public Team whichTeam(@NotNull UUID playerUUID) {

        for (Team team : teams) {

            if (team.isPlayerInTeam(playerUUID)) {
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
