package eu.cosup.bedwars.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.TeamLoseBedTask;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    private void onBlockBreak(@NotNull BlockBreakEvent event) {

        Block block = event.getBlock();

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                return;
            }
        }

        if (block.getType().toString().contains("BED")) {

            Team bedTeam = Game.getGameInstance().getSelectedMap().whichTeamBed(block.getLocation());

            if (bedTeam == null) {
                Bedwars.getInstance().getLogger().warning("A bed was broken but it wasn't a team bed");
                event.setCancelled(true);
                return;
            }

            TeamColor bedColor = bedTeam.getColor();
            event.setCancelled(true);
            new TeamLoseBedTask(bedColor, event.getPlayer());

            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (!Game.getGameInstance().getBlockManager().isBlockPlaced(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    private void onBlockDestroy(BlockBreakBlockEvent event) {

        if (!event.getBlock().isSolid() && !event.getBlock().getType().equals(Material.LADDER)) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    private void onBlockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType().equals(Material.LAVA_CAULDRON)
                    || event.getClickedBlock().getType().equals(Material.WATER_CAULDRON)
                    || event.getClickedBlock().getType().equals(Material.CAULDRON)) {
                event.setCancelled(true);
            }
        }
    }


}
