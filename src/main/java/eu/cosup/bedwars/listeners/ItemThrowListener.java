package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.ItemGeneratorManager;
import eu.cosup.bedwars.objects.ItemGenerator;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

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

    @EventHandler
    private void onItemStack(ItemMergeEvent event) {
        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            if (itemGenerator.getType().equals(ItemGenerator.GeneratorType.SPAWN)) {


                if (event.getTarget().getLocation().toVector().distance(itemGenerator.getLocation().toVector()) > 5) {
                    continue;
                }

                if (event.getEntity().getItemStack().getType() == Material.GOLD_INGOT) {
                    if (event.getTarget().getItemStack().getAmount() + event.getEntity().getItemStack().getAmount() > 10) {
                        event.getEntity().remove();
                        event.getTarget().setItemStack(new ItemStack(Material.GOLD_INGOT, 10));
                        event.setCancelled(true);
                    }
                }

                if (event.getEntity().getItemStack().getType() == Material.IRON_INGOT) {
                    if (event.getTarget().getItemStack().getAmount() + event.getEntity().getItemStack().getAmount() > 50) {
                        event.getEntity().remove();
                        event.getTarget().setItemStack(new ItemStack(Material.IRON_INGOT, 50));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
