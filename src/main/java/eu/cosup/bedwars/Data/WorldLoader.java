package eu.cosup.bedwars.Data;

import eu.cosup.bedwars.Bedwars;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldLoader {


    // returns false when world failed to load and returns true if it succeeded.
    public static boolean loadNewWorld(String name) {

        // this is a very brute force way and there is probably a better way of doing this.

        Bukkit.unloadWorld("world", true);

        try {
            FileUtils.forceDelete(new File("world"));
        } catch (IOException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Couldnt delete directory");
        }

        try {
            FileUtils.copyDirectory(new File("plugins/Bedwars/maps/" + name), new File("world"));
        } catch (IOException exception) {
            Bukkit.getLogger().severe("Couldnt copy the folder from maps directory now you should probably worry because this is really bad.");
            exception.printStackTrace();
            return false;
        }

        World world = Bukkit.createWorld(new WorldCreator("world").environment(World.Environment.CUSTOM));
        Bedwars.getInstance().setGameWorld(world);

        Bukkit.getLogger().info("Loaded: " + name);
        return true;
    }

    public static ArrayList<String> getWorldNames() {

        ArrayList<String> mapNames = new ArrayList<>();

        File mapFolder = new File(Bedwars.getInstance().getDataFolder(), "maps");

        // make sure the was a folder called maps

        if (mapFolder.mkdir()) {
            Bukkit.getLogger().warning("Created a folder for maps");
        }

        File[] mapFiles = mapFolder.listFiles();

        if (mapFiles == null) {
            Bukkit.getLogger().severe("There are no maps in map folder");
            return null;
        }

        for (File mapFile : mapFiles) {
            if (mapFile.isDirectory()) {
                if (!mapNames.contains(mapFile.getName())) {
                    mapNames.add(mapFile.getName());
                }
            }
        }

        return mapNames;
    }

}
