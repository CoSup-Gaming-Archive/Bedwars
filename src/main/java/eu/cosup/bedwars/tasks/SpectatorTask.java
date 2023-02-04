package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.ShopManager;
import eu.cosup.bedwars.objects.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SpectatorTask extends BukkitRunnable {

    private final Player player;
    private final Boolean respawn;
    private static final int respawnDelay = Bedwars.getInstance().getConfig().getInt("respawn-delay");

    public SpectatorTask(Player player, Boolean respawn) {
        this.player = player;
        this.respawn = respawn;

    }

    @Override
    public void run() {

        player.setGameMode(GameMode.SPECTATOR);
        player.setVelocity(new Vector().zero());

        // yay
        player.teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());

        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) != null) {
                Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).setPlayerDead(player, true);
            }
        }

        if (!respawn) {
            return;
        }


        TeamColor team = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor();

        for (int i = 0; i < respawnDelay; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.clearTitle();
                    Component msg = Component.text().content("Respawning in " ).color(NamedTextColor.RED)
                            .append(Component.text().content(String.valueOf(respawnDelay-finalI))).build();

                    Title title = Title.title(msg, Component.text().build());

                    player.showTitle(title);

                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, finalI);
                }
            }.runTaskLater(Bedwars.getInstance(), i*20L);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.clearTitle();

                if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ENDING) {
                    cancel();
                    return;
                }

                player.teleport(Game.getGameInstance().getSelectedMap().getSpawnByPlayer(player));

                ActivateGameTask.preparePlayerFull(player, Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().get(player.getName()), Game.getGameInstance().getShopManager().getPlayerTools().get(player.getName()));

                Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).setPlayerDead(player, false);

                player.sendMessage(Component.text().content("You are alive!").color(TeamColor.getNamedTextColor(team)));
            }
        }.runTaskLater(Bedwars.getInstance(), respawnDelay * 20L);
    }
}
