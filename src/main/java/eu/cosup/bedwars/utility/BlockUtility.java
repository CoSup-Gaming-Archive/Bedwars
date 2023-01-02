package eu.cosup.bedwars.utility;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class BlockUtility {
    private static final double spawnProtectionDistance = Bedwars.getInstance().getConfig().getDouble("spawn-protection-distance");

    public static boolean isLocationProtected(Location location) {

        for (Location teamSpawn : Game.getGameInstance().getSelectedMap().getTeamSpawns().values()) {

            if (teamSpawn.distance(location) < spawnProtectionDistance) {
                return true;
            }
        }
        return false;
    }
    public static RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, ArrayList<Material> targetBlocks) {
        if (direction.lengthSquared() < 1e-5 || maxDistance <= 1e-5) return null;

        // Look for block collisions
        RayTraceResult blockRayTrace = null;

        BlockIterator bIterator = new BlockIterator(start.getWorld(), start.toVector(), direction, 0, (int) Math.ceil(maxDistance));

        while(bIterator.hasNext()) {

            Block block = bIterator.next();
            // First perform a rough collision check with BlockIterator
            if (targetBlocks.contains(block.getType()) && !block.isEmpty()) {

                // Now check if the collision still occurs with the precise collision geometry of the block
                RayTraceResult res = block.rayTrace(start, direction, maxDistance, FluidCollisionMode.ALWAYS);
                if (res != null) {
                    blockRayTrace = res;
                    break;
                }

            }

        }
        // Return closest RayTraceResult
        return blockRayTrace;
    }
}
