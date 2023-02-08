package eu.cosup.bedwars.listeners.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TNTPlaceListener implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    private void onPlaceTNT(@NotNull BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.TNT) {
            event.setCancelled(true);
            event.getPlayer().getInventory().removeItem(new ItemStack(Material.TNT));
            Location location = event.getBlock().getLocation();
            location.setX(location.getX()-0.5);
            location.setZ(location.getZ()-0.5);
            event.getBlock().getLocation().getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        }
    }
}
