package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.ItemGenerator;
import eu.cosup.bedwars.objects.SideBarInformation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimerTask extends BukkitRunnable {

    private static int secondsElapsed;
    private static GameTimerTask instance;

    public GameTimerTask() {
        instance = this;
    }

    @Override
    public void run() {
        SideBarInformation.update();
        setSecondsElapsed(getSecondsElapsed()+1);

        if (secondsElapsed == 360) {
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.DIAMOND);
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("DIAMOND ").color(NamedTextColor.AQUA)).append(Component.text("generators were upgraded to level II")));
        }

        if (secondsElapsed == 720) {
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.EMERALD);
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("EMERALD ").color(NamedTextColor.AQUA)).append(Component.text(" generators were upgraded to level II")));
        }

        if (secondsElapsed == 1080) {
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.DIAMOND);
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("DIAMOND ").color(NamedTextColor.AQUA)).append(Component.text("generators were upgraded to level III")));
        }

        if (secondsElapsed == 1440) {
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.EMERALD);
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("EMERALD ").color(NamedTextColor.AQUA)).append(Component.text("generators were upgraded to level III")));
        }

        if (secondsElapsed == 2040) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.BED_DESTRUCTION);
        }

        if (secondsElapsed == 2640) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.DRAGONS);
        }

        new GameTimerTask().runTaskLater(Bedwars.getInstance(), 20L);
    }

    public void cancelTimer() {
        this.cancel();
        instance = null;
    }

    public static void resetTimer() {
        secondsElapsed = 0;
    }

    private static void setSecondsElapsed(int secondsElapsed) {
        GameTimerTask.secondsElapsed = secondsElapsed;
    }

    public static GameTimerTask getInstance() {
        return instance;
    }
    public static int getSecondsElapsed() {
        return secondsElapsed;
    }
}
