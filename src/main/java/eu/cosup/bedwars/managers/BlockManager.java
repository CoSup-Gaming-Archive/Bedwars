package eu.cosup.bedwars.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class BlockManager {

    private final ArrayList<Block> playerPlacedBlocks = new ArrayList<>();

    public BlockManager(){
    }

    public void addBlock(Block block) {
        playerPlacedBlocks.add(block);
    }

    public void removeBlock(Block block) {
        playerPlacedBlocks.remove(block);
    }

    public boolean isBlockPlaced(Block block) {
        return playerPlacedBlocks.contains(block);
    }

    public void clearMap() {

        Bukkit.getLogger().severe("----------------CLEARING MAP---------------------");

        for (Block block : playerPlacedBlocks) {
            block.getWorld().setType(block.getLocation(), Material.AIR);
        }

        Bukkit.getLogger().severe("-------------------FINISHED---------------------");
    }

}
