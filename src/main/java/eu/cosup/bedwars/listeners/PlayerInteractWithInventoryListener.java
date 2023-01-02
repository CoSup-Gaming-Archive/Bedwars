package eu.cosup.bedwars.listeners;


import eu.cosup.bedwars.managers.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInteractWithInventoryListener implements Listener {
    @EventHandler
    public void onEvent(InventoryClickEvent event){
        if (event.getView().title()==ShopManager.getInstance().title) {
            if (event.getClickedInventory() != event.getView().getPlayer().getInventory()) {
                ShopManager.getInstance().interactWithShop(event.getSlot(), (Player) event.getWhoClicked(), event.getClickedInventory());

            }
        }
    }
}
