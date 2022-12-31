package eu.cosup.bedwars.utility;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class BlockUtility {

    private static final List<String> blockWhitelist = Bedwars.getInstance().getConfig().getStringList("block-whitelist");
    private static final double spawnProtectionDistance = Bedwars.getInstance().getConfig().getDouble("spawn-protection-distance");

    // Invert this method
    public static boolean blockWhitelisted(Material material) {

        for (String materialString : blockWhitelist) {

            Material testMaterial = Material.getMaterial(materialString.toUpperCase());

            if (testMaterial == material) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLocationProtected(Location location) {

        for (Location teamSpawn : Game.getGameInstance().getSelectedMap().getTeamSpawns().values()) {

            if (teamSpawn.distance(location) < spawnProtectionDistance) {
                return true;
            }
        }
        return false;
    }
}
