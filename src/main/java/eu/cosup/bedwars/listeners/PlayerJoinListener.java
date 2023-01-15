package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.SpectatorTask;
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
            // if player is not in a team

            Team playerTeam = game.getTeamManager().whichTeam(event.getPlayer().getUniqueId());

            if (playerTeam == null) {
                Component msg = Component.text().content("You joined as spectator since the game already started").color(NamedTextColor.RED).build();
                event.getPlayer().sendMessage(msg);
                new SpectatorTask(event.getPlayer(), false).runTask(Bedwars.getInstance());
                return;
            }
            
            Component msg = Component.text().content("You joined as ").color(NamedTextColor.YELLOW)
                            .append(Component.text().content(TeamColor.getFormattedTeamColor(playerTeam.getColor())).color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).getColor()))).build();

            event.getPlayer().sendMessage(msg);
            event.getPlayer().setHealth(0);
            return;
        }

        if (game.getJoinedPlayers().size() < Bedwars.getInstance().getConfig().getInt("required-player-count") && Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.JOINING) {
            game.getJoinedPlayers().add(event.getPlayer());
            game.refreshPlayerCount();
        } else {
            // the player will join as spectator
            Component msg = Component.text().content("You joined as spectator since there are already enough players in the game").color(NamedTextColor.GRAY).build();
            event.getPlayer().sendMessage(msg);
        }

        new SpectatorTask(event.getPlayer(), false).runTask(Bedwars.getInstance());

    }
}
