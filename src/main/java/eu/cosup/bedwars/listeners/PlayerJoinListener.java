package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.SpectatorTask;
import eu.cosup.tournament.common.utility.PlayerUtility;
import eu.cosup.tournament.server.TournamentServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Game game = Game.getGameInstance();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        // TODO nameTagEditor.setNameColor(ChatColor.GRAY).setPrefix("Spectator ").setTabName(ChatColor.translateAlternateColorCodes('&', "&7"+event.getPlayer().getName()));

        // if game has already started
        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            event.getPlayer().setHealth(0);
            return;
        }

        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.JOINING) {
            event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            if (!PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
                new SpectatorTask(event.getPlayer(), false).runTask(Bedwars.getInstance());
                return;
            }

            event.getPlayer().setGameMode(GameMode.CREATIVE);
            return;
        }

        new SpectatorTask(event.getPlayer(), false).runTask(Bedwars.getInstance());
    }
}
