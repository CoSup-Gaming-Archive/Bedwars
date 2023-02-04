package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractWithEntityListener implements Listener {
    @EventHandler
    public void onEvent(PlayerInteractEntityEvent event){

        Player player=event.getPlayer();

        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            event.setCancelled(true);
            Game.getGameInstance().getShopManager().openShopForPlayer(player, null);
        }

        if (event.getRightClicked().getType() == EntityType.WANDERING_TRADER) {
            event.setCancelled(true);
            Game.getGameInstance().getUpgradesManager().openGUIForPlayer(player);
        }
    }
}
