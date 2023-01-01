package eu.cosup.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerReceiveListener implements Listener {

    @EventHandler
    private void onPlayerGetHungry(FoodLevelChangeEvent event) {
        event.getEntity().setFoodLevel(20);
    }

}
