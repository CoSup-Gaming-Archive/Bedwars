package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractWithEntityListener implements Listener {
    @EventHandler
    public void onEvent(PlayerInteractEntityEvent event){

        Player player=event.getPlayer();

        Entity entity = event.getRightClicked();
        if (!(entity instanceof Villager)){

            return;
        }

        Game.getGameInstance().getShopManager().openShopForPlayer(player, null);
    }
}
