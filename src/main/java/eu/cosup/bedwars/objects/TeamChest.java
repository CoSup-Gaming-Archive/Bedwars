package eu.cosup.bedwars.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TeamChest {

    private final Location location;
    private final TeamColor teamColor;

    public static final Material CHEST_BLOCK = Material.CHEST;

    private final HashMap<TeamColor, Inventory> teamInventories = new HashMap<>();

    public TeamChest(TeamColor teamColor, Location location) {
        this.location = location;
        this.teamColor = teamColor;
    }

    public Location getLocation() {
        return location;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }
}
