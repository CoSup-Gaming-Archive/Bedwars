package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Game game = Game.getGameInstance();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        // TODO nameTagEditor.setNameColor(ChatColor.RESET).setPrefix("").setTabName(event.getPlayer().getName());


        game.getPlayerList().remove(event.getPlayer());
        game.getJoinedPlayers().remove(event.getPlayer());
        game.refreshPlayerCount();
    }
}
