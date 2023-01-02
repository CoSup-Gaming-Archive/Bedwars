package eu.cosup.bedwars.objects;


import org.bukkit.Location;
import org.bukkit.Material;


public class PrivateChest {

    private final Location location;

    public static final Material CHEST_BLOCK = Material.ENDER_CHEST;

    public PrivateChest(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
