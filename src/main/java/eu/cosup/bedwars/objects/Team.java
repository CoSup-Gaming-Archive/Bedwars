package eu.cosup.bedwars.objects;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.TeamChangeAliveEvent;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Team {

    private List<Player> players;
    private final TeamColor color;
    private boolean isAlive;
    private final HashMap<String, Boolean> deathList = new HashMap<>();
    private TeamBase base;
    private TeamUpgrades upgrades = new TeamUpgrades();

    public Team(TeamColor teamColor, List<Player> players, boolean isAlive) {
        this.players = players;
        this.color = teamColor;
        this.isAlive = isAlive;
        this.base= new TeamBase(this, Game.getGameInstance().getSelectedMap().getTeamBeds().get(this.color), Game.getGameInstance().getSelectedMap().getBaseDetectionRadius());
        for (Player player : players) {
            isPlayerDead(player);
        }
    }
    public TeamUpgrades getUpgrades() {
        return upgrades;
    }

    // keep track of all the dead players in the taem
    public boolean isPlayerDead(Player player) {
        if (deathList.get(player.getName()) == null) {
            deathList.put(player.getName(), false);
            return false;
        }
        return deathList.get(player.getName());
    }

    public void setPlayerDead(Player player, Boolean dead) {
        deathList.put(player.getName(), dead);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public TeamColor getColor() {
        if (color == null) {
            return TeamColor.RED;
        }

        return color;
    }

    public TeamBase getBase() {
        return base;
    }

    public boolean isPlayerInTeam(@NotNull UUID playerUUID) {
        for (Player player1 : players) {

            if (player1.getUniqueId().equals(playerUUID)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Player> getOnlinePlayers() {
        return new ArrayList<>(players.stream().filter(OfflinePlayer::isOnline).toList());
    }

    public ArrayList<Player> getAlivePlayers() {
        return new ArrayList<>(this.getOnlinePlayers().stream().filter(player -> !deathList.get(player.getName())).toList());
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        new TeamChangeAliveEvent(alive, color);
    }

    public boolean isAlive() {
        return isAlive;
    }
}
