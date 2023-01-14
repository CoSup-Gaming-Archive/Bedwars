package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Game game = Game.getGameInstance();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        // TODO nameTagEditor.setNameColor(ChatColor.RESET).setPrefix("").setTabName(event.getPlayer().getName());

        game.getJoinedPlayers().remove(event.getPlayer());
        game.refreshPlayerCount();

        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {

            Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer()).setPlayerDead(event.getPlayer(), true);
            event.getPlayer().setHealth(0);

            if (!Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer()).isAlive()) {
                if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer()).getOnlinePlayers().size() < 2) {
                    Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer()).setAlive(false);
                }
            }
        }
    }
}
