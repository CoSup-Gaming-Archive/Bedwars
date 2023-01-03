package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.SideBarInformation;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimerTask extends BukkitRunnable {

    private static final int timeToFirstUpgrade = Bedwars.getInstance().getConfig().getInt("time-to-first-phase");
    private static final int timeToSecondUpgrade = Bedwars.getInstance().getConfig().getInt("time-to-second-phase");
    private static final int timeToBedDestruction = Bedwars.getInstance().getConfig().getInt("time-to-bed-destruction-phase");
    private static final int timeToSuddenDeath = Bedwars.getInstance().getConfig().getInt("time-to-sudden-death-phase");

    private static int secondsElapsed;
    private static GameTimerTask instance;

    public GameTimerTask() {
        instance = this;
    }

    @Override
    public void run() {
        SideBarInformation.update();
        setSecondsElapsed(getSecondsElapsed()+1);

        if (secondsElapsed == timeToFirstUpgrade) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.FIRST_UPGRADE);
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(Material.DIAMOND);
        }

        if (secondsElapsed == timeToSecondUpgrade) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.SECOND_UPGRADE);
            Game.getGameInstance().getItemGeneratorManager().upgradeGenerators(Material.EMERALD);
        }

        if (secondsElapsed == timeToBedDestruction) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.BED_DESTRUCTION);
        }

        if (secondsElapsed == timeToSuddenDeath) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.SUDDEN_DEATH);
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
