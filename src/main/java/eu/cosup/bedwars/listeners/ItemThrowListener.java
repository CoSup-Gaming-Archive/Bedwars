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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class ItemThrowListener implements Listener {

    // when player tries to get rid of default items we should stop him

    @EventHandler
    private void onPlayerThrow(PlayerDropItemEvent event) {


        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    private void onItemStack(ItemMergeEvent event) {

        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
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
            if (event.getEntity().getItemStack().getType() == Material.DIAMOND) {
                if (event.getTarget().getItemStack().getAmount() + event.getEntity().getItemStack().getAmount() > 4) {
                    event.getEntity().remove();
                    event.getTarget().setItemStack(new ItemStack(Material.DIAMOND, 4));
                    event.setCancelled(true);
                }
            }
            if (event.getEntity().getItemStack().getType() == Material.EMERALD) {
                if (event.getTarget().getItemStack().getAmount() + event.getEntity().getItemStack().getAmount() > 2) {
                    event.getEntity().remove();
                    event.getTarget().setItemStack(new ItemStack(Material.EMERALD, 2));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerPickUp(EntityPickupItemEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) == null) {
            event.setCancelled(true);
            return;
        }

        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            if (itemGenerator.getType().equals(ItemGenerator.GeneratorType.SPAWN)) {

                if (player.getLocation().toVector().distance(itemGenerator.getLocation().toVector()) > 3) {
                    continue;
                }

                if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) == null) {
                    return;
                }

                // this part should prevent item duping
                if (event.getItem().getItemStack().getItemMeta().getLore() == null) {
                    return;
                }

                event.setCancelled(true);
                event.getItem().getItemStack().setLore(null);
                event.getItem().remove();

                for (Player teammate : Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getPlayers()) {
                    if (teammate.getLocation().toVector().distance(itemGenerator.getLocation().toVector()) < 3) {
                        teammate.getInventory().addItem(event.getItem().getItemStack());
                        teammate.playSound(teammate.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    }
                }

            }
        }
    }
}
