package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.PrivateChest;
import eu.cosup.bedwars.objects.TeamChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChestManager {

    private ArrayList<TeamChest> teamChests;
    private ArrayList<PrivateChest> privateChests;

    public ChestManager() {

        privateChests = Game.getGameInstance().getSelectedMap().getPrivateChests();
        teamChests = Game.getGameInstance().getSelectedMap().getTeamChests();
    }

    public ArrayList<Location> getGameChestLocation() {
        ArrayList<Location> gameChestLocations = new ArrayList<>();

        for (PrivateChest privateChest : privateChests) {
            gameChestLocations.add(privateChest.getLocation());
        }

        for (TeamChest teamChest : teamChests) {
            gameChestLocations.add(teamChest.getLocation());
        }

        return gameChestLocations;
    }

    public TeamChest getTeamChest(Location location) {
        if (!isTeamChest(location)) {
            return null;
        }

        for (TeamChest teamChest : teamChests) {
            if (teamChest.getLocation().toVector().equals(location.toVector())) {
                return teamChest;
            }
        }

        return null;
    }

    public boolean isPrivateChest(Location location) {
        for (PrivateChest privateChest : privateChests) {
            if (privateChest.getLocation().toVector().equals(location.toVector())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTeamChest(Location location) {
        for (TeamChest teamChest : teamChests) {
            if (teamChest.getLocation().toVector().equals(location.toVector())) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameChest(Location location) {

        if (isPrivateChest(location)) {
            return true;
        }

        return isTeamChest(location);
    }
}
