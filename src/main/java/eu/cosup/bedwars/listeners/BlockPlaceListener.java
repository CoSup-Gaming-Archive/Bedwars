package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.ItemGenerator;
import eu.cosup.bedwars.utility.BlockUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;


public class BlockPlaceListener implements Listener {

    @EventHandler
    private void onPlayerPlaceBlock(@NotNull BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.getGameMode() == GameMode.CREATIVE) {
            Game.getGameInstance().getBlockManager().addBlock(block);
            return;
        }

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            event.setCancelled(true);
            return;
        }

        Component msg = Component.text().content("You cannot place blocks here").color(NamedTextColor.RED).build();

        if (Game.getGameInstance().getSelectedMap().getMaxHeight() < block.getY()) {
            player.sendMessage(msg);
            event.setCancelled(true);
            return;
        }

        if (Game.getGameInstance().getSelectedMap().getMinHeight() > block.getY()) {
            player.sendMessage(msg);
            event.setCancelled(true);
            return;
        }

        if (BlockUtility.isLocationProtected(block.getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(msg);
            return;
        }

        if (event.getBlock().getLocation().getBlockX() > Game.getGameInstance().getSelectedMap().getxMax() ||
            event.getBlock().getLocation().getBlockX() < Game.getGameInstance().getSelectedMap().getxMin()) {

            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getLocation().getBlockZ() > Game.getGameInstance().getSelectedMap().getzMax() ||
            event.getBlock().getLocation().getBlockZ() < Game.getGameInstance().getSelectedMap().getzMin()) {

            event.setCancelled(true);
            return;
        }

        // for the lols if this actualy happens
        if (event.getBlock().getType().toString().toLowerCase().contains("bed")) {
            event.getPlayer().sendMessage("You cheater how you get bed?!?!");
            Bukkit.getLogger().severe("Someone just tried placing a bed lol");
            event.getPlayer().getInventory().remove(event.getBlock().getType());
            event.setCancelled(true);
            return;
        }

        Game.getGameInstance().getBlockManager().addBlock(block);

    }

    @EventHandler
    private void onPlaceInSpawner(BlockPlaceEvent event) {
        // prevent placement of blocks in spawner

        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            if (itemGenerator.getType().equals(ItemGenerator.GeneratorType.SPAWN)) {
                if (event.getBlock().getLocation().distance(itemGenerator.getLocation()) < 3) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }
}
