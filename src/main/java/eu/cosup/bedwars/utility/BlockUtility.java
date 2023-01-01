package eu.cosup.bedwars.utility;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

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
}
