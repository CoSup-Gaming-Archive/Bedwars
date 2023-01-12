package eu.cosup.bedwars.listeners.custom;

import eu.cosup.bedwars.Bedwars;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class PearlTeleportListener implements Listener {

    @EventHandler
    private void onPlayerPearl(@NotNull PlayerTeleportEvent event) {

        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            for (Player player : Bedwars.getInstance().getServer().getOnlinePlayers()) {
                player.playSound(event.getTo(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            }
        }
    }
}
