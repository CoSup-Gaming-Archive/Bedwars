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

import javax.naming.Name;

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

        if (secondsElapsed == 200) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.DIAMOND);
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.DIAMOND);
            Bedwars.getInstance().getServer().broadcast(Component.text("At 15 minutes all beds will be destroyed").color(NamedTextColor.RED));
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("DIAMOND ").color(NamedTextColor.AQUA)).append(Component.text("generators were upgraded to level II")));
        }

        if (secondsElapsed == 400) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.EMERALD);
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.EMERALD);
            Bedwars.getInstance().getServer().broadcast(Component.text("At 15 minutes all beds will be destroyed").color(NamedTextColor.RED));
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("EMERALD ").color(NamedTextColor.GREEN)).append(Component.text(" generators were upgraded to level II")));
        }

        if (secondsElapsed == 600) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.DIAMOND_TWO);
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.DIAMOND);
            Bedwars.getInstance().getServer().broadcast(Component.text("At 15 minutes all beds will be destroyed").color(NamedTextColor.RED));
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("DIAMOND ").color(NamedTextColor.AQUA)).append(Component.text("generators were upgraded to level III")));
        }

        if (secondsElapsed == 800) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.EMERALD_TWO);
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(ItemGenerator.GeneratorType.EMERALD);
            Bedwars.getInstance().getServer().broadcast(Component.text("At 15 minutes all beds will be destroyed").color(NamedTextColor.RED));
            Bedwars.getInstance().getServer().broadcast(Component.text("All ").color(NamedTextColor.YELLOW).append(Component.text("EMERALD ").color(NamedTextColor.GREEN)).append(Component.text("generators were upgraded to level III")));
        }

        if (secondsElapsed == 900) {
            Bedwars.getInstance().getServer().broadcast(Component.text("At 16 minutes the dragons will spawn").color(NamedTextColor.RED));
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.BED_DESTRUCTION);
        }

        if (secondsElapsed == 1000) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.DRAGONS);
            Bedwars.getInstance().getServer().broadcast(Component.text("At 30 minutes the team with more players alive wins").color(NamedTextColor.RED));
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
