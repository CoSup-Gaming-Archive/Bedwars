package eu.cosup.bedwars.listeners.custom;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.utility.BlockUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(@NotNull EntityExplodeEvent event){
            event.setCancelled(true);

            ArrayList<Material> searchedTargets=new ArrayList<>();
            searchedTargets.add(Material.RED_STAINED_GLASS);
            searchedTargets.add(Material.YELLOW_STAINED_GLASS);
            searchedTargets.add(Material.BLUE_STAINED_GLASS);
            searchedTargets.add(Material.GREEN_STAINED_GLASS);
            searchedTargets.add(Material.GLASS);

            for (Block block : event.blockList()) {

                if (searchedTargets.contains(block.getType()) || !Game.getGameInstance().getBlockManager().isBlockPlaced(block)){
                    continue;
                }

                int distance = (int) event.getLocation().distance(block.getLocation());

                Vector direction = new Vector(
                        event.getLocation().getX() - block.getLocation().getX() + 0.0001,
                        event.getLocation().getY() - block.getLocation().getY() + 0.0001,
                        event.getLocation().getZ() - block.getLocation().getZ() + 0.0001
                );

                direction = direction.normalize();

                RayTraceResult rayTraceResult = BlockUtility.rayTrace(block.getLocation(), direction, distance, searchedTargets);

                if (rayTraceResult == null) {
                    block.breakNaturally();
                }
            }
        }
}
