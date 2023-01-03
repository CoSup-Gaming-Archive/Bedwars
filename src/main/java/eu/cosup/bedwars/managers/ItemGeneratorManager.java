package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;

public class ItemGeneratorManager {

    public ItemGeneratorManager() {
    }

    public void activateGenerators() {
        Bukkit.getLogger().warning("started "+Game.getGameInstance().getSelectedMap().getItemGenerators().size()+" generators");

        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            itemGenerator.startSpawning();
        }
    }

    public void deactivateGenerators() {
        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            itemGenerator.stopDropping();
        }
    }

    public void upgradeGenerators(Material material) {
        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            if (itemGenerator.getItem() == material) {
                itemGenerator.upgrade();
            }
        }
    }
}
