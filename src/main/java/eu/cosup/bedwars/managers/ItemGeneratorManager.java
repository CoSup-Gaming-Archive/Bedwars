package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.ItemGenerator;
import org.bukkit.Material;

public class ItemGeneratorManager {

    public ItemGeneratorManager() {
    }

    public void activateGenerators() {

        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            itemGenerator.startSpawning();
        }
    }

    public void deactivateGenerators() {
        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            itemGenerator.stopDropping();
        }
    }

    public void upgradeGenerators(ItemGenerator.GeneratorType type) {
        for (ItemGenerator itemGenerator : Game.getGameInstance().getSelectedMap().getItemGenerators()) {
            if (itemGenerator.getType().equals(type)) {
                itemGenerator.upgrade();
            }
        }
    }
}
