package eu.cosup.bedwars.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;


public class ExplodedBlocksUtility {
    private Location location=null;
    private Material newMatertial=null;


    public ExplodedBlocksUtility(Location location, Material newMaterial){
        this.location=location;
        this.newMatertial=newMaterial;
    }
    public void setBlockAt(World world){
        world.getBlockAt(location).setType(newMatertial);
    }
}
