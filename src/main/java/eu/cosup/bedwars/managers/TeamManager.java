package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.tournament.common.objects.GameTeam;
import eu.cosup.tournament.server.TournamentServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class TeamManager {

    private ArrayList<Team> teams = new ArrayList<>();
    public ArrayList<Team> getTeams() {
        return teams;
    }

    // this will probably be thrown away later
    public void makeTeams() {

        List<GameTeam> gameTeams = TournamentServer.getInstance().getTeams();

        for (int i = 0; i < gameTeams.size(); i++) {
            TeamColor teamColor = TeamColor.values()[i];
            List<Player> teamPlayers = new ArrayList<>();
            for (UUID playerUUID : gameTeams.get(i).getPlayerUUIDs()) {
                Player player = Bedwars.getInstance().getServer().getPlayer(playerUUID);
                if (player != null) {
                    teamPlayers.add(player);
                }
            }
            teams.add(new Team(teamColor, teamPlayers, true));
        }
    }

    public ArrayList<Team> getTeamsWithBeds() {
        ArrayList<Team> aliveTeams = new ArrayList<>();

        for (Team team : teams) {
            if (team.isAlive()) {
                aliveTeams.add(team);
            }
        }

        return aliveTeams;
    }

    public @Nullable Team getTeamWithName(@NotNull String name) {

        for (Team team : teams) {
            if (name.toLowerCase().contains(team.getColor().toString().toLowerCase())) {
                return team;
            }
        }

        return null;
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

    public @Nullable Team getTeamByColor(TeamColor teamColor) {

        for (Team team : teams) {
            if (Objects.equals(team.getColor().toString(), teamColor.toString())) {
                return team;
            }
        }
        return null;
    }
}
