package eu.cosup.bedwars.objects;

import eu.cosup.bedwars.events.TeamChangeAliveEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    private final TeamColor color;
    private boolean isAlive;

    public Team(TeamColor teamColor, ArrayList<Player> players, boolean isAlive) {
        this.players = players;
        this.color = teamColor;
        this.isAlive = isAlive;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public TeamColor getColor() {
        return color;
    }

    public boolean isPlayerInTeam(Player player) {
        for (Player player1 : players) {
            if (player1.getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Player> getOnlinePlayers() {
        return new ArrayList<>(players.stream().filter(OfflinePlayer::isOnline).toList());
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        new TeamChangeAliveEvent(alive, color);
    }

    public boolean isAlive() {
        return isAlive;
    }
}
