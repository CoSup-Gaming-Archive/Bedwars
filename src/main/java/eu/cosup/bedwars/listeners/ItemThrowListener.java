package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemThrowListener implements Listener {

    // when player tries to get rid of default items we should stop him

    @EventHandler
    private void onPlayerThrow(PlayerDropItemEvent event) {


        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
                event.setCancelled(true);
            }
        }
    }
}
