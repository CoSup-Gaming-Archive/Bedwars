package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Game game = Game.getGameInstance();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        // TODO nameTagEditor.setNameColor(ChatColor.RESET).setPrefix("").setTabName(event.getPlayer().getName());

        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {

            event.getPlayer().setHealth(0);

            // the player is not in a team
            if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()) == null) {
                return;
            }

            if (!Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).isAlive()) {
                if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).getOnlinePlayers().size() < 2) {
                    Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).setAlive(false);
                }
            }
        }
    }
}
