package eu.cosup.bedwars.listeners;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDamageListener implements Listener {

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

}
