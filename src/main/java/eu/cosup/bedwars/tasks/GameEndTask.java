package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.SideBarInformation;
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
import org.jetbrains.annotations.Nullable;

public class GameEndTask extends BukkitRunnable {

    private final TeamColor winner;

    public GameEndTask(@Nullable TeamColor winner) {
        this.winner = winner;
    }

    @Override
    public void run() {

        Team winnerTeam = Game.getGameInstance().getTeamManager().getTeamByColor(winner);

        GameTimerTask.getInstance().cancelTimer();
        Game.getGameInstance().getItemGeneratorManager().deactivateGenerators();

        if (winnerTeam != null) {
            for (Player player : winnerTeam.getPlayers()) {

                Location playerLocation = player.getLocation();

                for (int i = 0; i < 1; i++) {
                    Bedwars.getInstance().getGameWorld().spawnEntity(playerLocation, EntityType.FIREWORK);
                }
            }
        }

        Component msg = Component.text().content(String.valueOf(winner)).color(TeamColor.getNamedTextColor(winner))
                .append(Component.text().content(" is the winner!").color(NamedTextColor.YELLOW)).build();
        Bedwars.getInstance().getServer().broadcast(msg);

        SideBarInformation.update();

        Bukkit.getLogger().warning("Shutting down in: " + Bedwars.getInstance().getConfig().getInt("return-to-lobby-delay"));
        new BukkitRunnable() {
            @Override
            public void run() {

                Component msg = Component.text().content("server is shutting down").color(NamedTextColor.RED).build();
                Bedwars.getInstance().getServer().broadcast(msg);

                Bedwars.getInstance().getServer().shutdown();

            }
        }.runTaskLater(Bedwars.getInstance(), Bedwars.getInstance().getConfig().getInt("return-to-lobby-delay") * 20L);
    }
}
