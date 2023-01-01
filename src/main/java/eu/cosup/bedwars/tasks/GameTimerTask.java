package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.objects.SideBarInformation;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimerTask extends BukkitRunnable {

    private static final int timeToFirstUpgrade = Bedwars.getInstance().getConfig().getInt("time-to-first-phase");
    private static final int timeToSecondUpgrade = Bedwars.getInstance().getConfig().getInt("time-to-second-phase");
    private static final int timeToThirdUpgrade = Bedwars.getInstance().getConfig().getInt("time-to-third-phase");
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
