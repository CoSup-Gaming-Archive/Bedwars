package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.TeamManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.SpectatorTask;
import eu.cosup.tournament.common.utility.PlayerUtility;
import eu.cosup.tournament.server.TournamentServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Game game = Game.getGameInstance();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        // TODO nameTagEditor.setNameColor(ChatColor.GRAY).setPrefix("Spectator ").setTabName(ChatColor.translateAlternateColorCodes('&', "&7"+event.getPlayer().getName()));

        Component msg = Component.text("DIAMOND_UPGRADE_1 at 3.3 minutes \n");
        msg = msg.append(Component.text("EMERALD_UPGRADE_1 at 6.6 minutes \n"));
        msg = msg.append(Component.text("DIAMOND_UPGRADE_2 at 10 minutes \n"));
        msg = msg.append(Component.text("EMERALD_UPGRADE_2 at 13.3 minutes \n"));
        msg = msg.append(Component.text("BEDS_DESTROYED at 15 minutes \n"));
        msg = msg.append(Component.text("DRAGONS at 16.6 minutes \n"));

        event.getPlayer().sendMessage(msg);

        if (PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            return;
        }

        // The player needs to be readded to the teams player list on rejoin because the old player
        // object in the teams player list becomes stale
        Team team = Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId());
        if (team != null) {
            team.getPlayers().removeIf(player -> player.getUniqueId().equals(event.getPlayer().getUniqueId()));
            if (team.getPlayers().stream().noneMatch(player -> player.getUniqueId().equals(event.getPlayer().getUniqueId()))) {
                team.getPlayers().add(event.getPlayer());
            }
        }


        // if game has already started
        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            event.getPlayer().setHealth(0);
            return;
        }
        event.getPlayer().getInventory().clear();

        event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
        if (!PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            new SpectatorTask(event.getPlayer(), false).runTask(Bedwars.getInstance());
            return;
        }
    }
}
