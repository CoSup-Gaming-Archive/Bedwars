package eu.cosup.bedwars.listeners;


import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.ShopManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInteractWithInventoryListener implements Listener {
    @EventHandler
    public void onEvent(InventoryClickEvent event){
        if (event.getView().title()==Game.getGameInstance().getUpgradesManager().title){
            if (!(event.getClickedInventory()==null)){
                if (!(event.getClickedInventory().equals(event.getWhoClicked().getInventory()))){
                    Game.getGameInstance().getUpgradesManager().onClick(event.getSlot(), (Player) event.getWhoClicked(), event.getClickedInventory());
                } else {
                    Bukkit.broadcast(Component.text("clicked own inv"));
                }
            } else {
                Bukkit.broadcast(Component.text("clicked outside"));
            }
        }
        if (event.getView().title()== Game.getGameInstance().getShopManager().title) {
            if (event.getClickedInventory() != event.getView().getPlayer().getInventory()) {
                Game.getGameInstance().getShopManager().interactWithShop(event.getSlot(), (Player) event.getWhoClicked(), event.getClickedInventory());

            }
        }
    }
}
