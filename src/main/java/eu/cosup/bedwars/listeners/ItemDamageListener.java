package eu.cosup.bedwars.listeners.custom;

import io.papermc.paper.event.entity.EntityDamageItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemDamageListener implements Listener {

    @EventHandler
    public void onItemDamage(EntityDamageItemEvent event) {
        event.setCancelled(true);
    }

}
