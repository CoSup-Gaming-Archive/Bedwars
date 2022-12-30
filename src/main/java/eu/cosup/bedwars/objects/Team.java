package eu.cosup.bedwars.objects;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    private final TeamColor color;

    public Team(TeamColor teamColor, ArrayList<Player> players) {
        this.players = players;
        this.color = teamColor;
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
}
