package eu.cosup.bedwars.listeners.custom;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.tasks.GameTimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerShootFireballListener implements Listener {

    private static HashMap<String, Integer> playerCooldown = new HashMap<>();

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction().isLeftClick()) {
            return;
        }

        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {

            playerCooldown.putIfAbsent(event.getPlayer().getName(), 0);

            if (GameTimerTask.getSecondsElapsed() - playerCooldown.get(event.getPlayer().getName()) < 2) {
                event.getPlayer().sendMessage(Component.text().content("You cannot shoot that fast").color(NamedTextColor.RED));
                return;
            }

            Location location = event.getPlayer().getEyeLocation();

            // remove the firecharge itself from inventory
            ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
            handItem.setAmount(handItem.getAmount()-1);

            location.setX(location.getX() + location.getDirection().getX()*2);
            location.setY(location.getY() + location.getDirection().getY()*2);
            location.setZ(location.getZ() + location.getDirection().getZ()*2);

            event.getPlayer().getInventory().setItemInMainHand(handItem);

            location.getWorld().spawnEntity(location, EntityType.FIREBALL);

            playerCooldown.put(event.getPlayer().getName(), GameTimerTask.getSecondsElapsed());

            event.setCancelled(true);
        }
    }
}
