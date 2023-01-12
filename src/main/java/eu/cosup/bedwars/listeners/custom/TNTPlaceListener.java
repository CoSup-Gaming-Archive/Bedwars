package eu.cosup.bedwars.listeners.custom;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class TNTPlaceListener implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    private void onPlaceTNT(@NotNull BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.TNT) {
            event.setCancelled(true);
            event.getBlock().getLocation().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);
        }
    }
}
