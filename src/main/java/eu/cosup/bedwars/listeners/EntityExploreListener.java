package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.utility.BlockUtility;
import eu.cosup.bedwars.utility.ExplodedBlocksUtility;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityExploreListener implements Listener {
    @EventHandler
    public void onEvent(EntityExplodeEvent event){
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
                        event.getLocation().getX() - block.getLocation().getX() + 0.2,
                        event.getLocation().getY() - block.getLocation().getY() + 0.2,
                        event.getLocation().getZ() - block.getLocation().getZ() + 0.2
                );

                direction = direction.normalize();

                RayTraceResult rayTraceResult = BlockUtility.rayTrace(block.getLocation(), direction, distance, searchedTargets);

                if (rayTraceResult == null) {
                    block.breakNaturally();
                    //block.getWorld().setType(block.getLocation(), Material.AIR);
                }
            }
        }
        /*event.setCancelled(true);
        Block atBlock = event.getLocation().getBlock();
        List<Block> blocks = event.blockList();
        ArrayList<ExplodedBlocksUtility> changedBlocks=new ArrayList<>();
        List<Material> searchedTargets=List.of(Material.GLASS);
        for (Block block : blocks){
            if (searchedTargets.contains(block.getType())){
                changedBlocks.add(new ExplodedBlocksUtility(block.getLocation(), block.getType()));
                continue;
            }
            Vector direct=new Vector(block.getX()-atBlock.getX(), block.getY()-atBlock.getY(), block.getZ()-atBlock.getZ());
            int xyz=direct.getBlockX()^direct.getBlockX()+direct.getBlockY()^direct.getBlockY()+ direct.getBlockZ()^direct.getBlockZ();
            if (xyz<0){
                xyz*=-1;
            }
            Double xyzd=Math.sqrt(xyz*1.0);

            Bukkit.getLogger().info(String.valueOf(xyz));
            block.rayTrace(block.getLocation(), direct, xyzd, FluidCollisionMode.ALWAYS);
            RayTraceResult rayTraceResult = BlockUtility.rayTrace(block.getLocation(), direct, xyzd+1.0, searchedTargets);
            if (rayTraceResult == null){
                changedBlocks.add(new ExplodedBlocksUtility(block.getLocation(), Material.AIR));
            } else {
                Bukkit.getLogger().info("ok glass");
            }
        }
        for (ExplodedBlocksUtility changedBlock:changedBlocks){
            changedBlock.setBlockAt(atBlock.getWorld());
        }

        /*for (int z=-2; z<2; z++){
            for (int y=-2; y<2; y++){
                for(int x=-2; x<2; x++){
                    /*Block thisBlock= atBlock.getWorld().getBlockAt(new Location(atBlock.getWorld(), atBlock.getX()-2+x, atBlock.getY()-2+y, atBlock.getZ()-2+z));
                    if (thisBlock.getType()==Material.AIR){
                        continue;
                    }
                    thisBlock.setType(Material.STONE);
                    Bukkit.getLogger().info(String.valueOf(x));
                    Bukkit.getLogger().info(String.valueOf(y));
                    Bukkit.getLogger().info(String.valueOf(z));
                    Bukkit.getLogger().info(" ");
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    RayTraceResult result = atBlock.rayTrace(atBlock.getLocation(), new Vector(-2+x, -2+y, -2+z), 3.0, FluidCollisionMode.NEVER );//new Location(atBlock.getWorld(), atBlock.getX()-2+x, atBlock.getY()-2+y, atBlock.getZ()-2+z), )
                    if (result==null){
                        Bukkit.getLogger().info("Didnt hit a block");
                        continue;
                    }
                    if (result.getHitBlock().getType()==Material.GLASS){
                        continue;
                    } else {
                        result.getHitBlock().setType(Material.STONE);
                    }
                }
            }
        }
    }*/
}
