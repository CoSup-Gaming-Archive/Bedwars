package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Bed;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndTask extends BukkitRunnable {

    private final TeamColor winner;

    public GameEndTask(TeamColor winner) {

        this.winner = winner;

    }

    @Override
    public void run() {

        if (winner == null) {
            return;
        }

        Team winnerTeam = Game.getGameInstance().getTeamManager().getTeamByColor(winner);

        GameTimerTask.getInstance().cancelTimer();

        for (Player player : winnerTeam.getPlayers()) {

            Location playerLocation = player.getLocation();

            for (int i = 0; i < 10; i++) {
                Bedwars.getInstance().getGameWorld().spawnEntity(playerLocation, EntityType.FIREWORK);
            }
        }
        for (Player player : Game.getGameInstance().getJoinedPlayers()) {

            if (!winnerTeam.isPlayerInTeam(player)) {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
        Component msg = Component.text().content(winner.toString()).color(TeamColor.getNamedTextColor(winner))
                .append(Component.text().content(" is the winner!").color(NamedTextColor.YELLOW)).build();
        Bedwars.getInstance().getServer().broadcast(msg);

        Bukkit.getLogger().warning("New game in: " + Bedwars.getInstance().getConfig().getInt("return-to-lobby-delay"));
        new BukkitRunnable() {
            @Override
            public void run() {

                // you should return players to lobby before restarting
                Bukkit.getLogger().severe("Restarting game");

                // create new game instance
                Bedwars.getInstance().createGame();

                Bedwars.getInstance().getGame().refreshPlayerCount();

            }
        }.runTaskLater(Bedwars.getInstance(), Bedwars.getInstance().getConfig().getInt("return-to-lobby-delay") * 20L);
    }
}
