package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class StartCountdownTask extends BukkitRunnable {

    private int startCountdown;
    private boolean stopped = false;

    public StartCountdownTask() {

        startCountdown = Bedwars.getInstance().getConfig().getInt("start-countdown");

        if (startCountdown <= 0) {
            Bukkit.getLogger().severe("Start countdown cannot be that low, defaulting to 10");
            startCountdown = 10;
        }

    }

    @Override
    public void run() {
        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.STARTING);
        Game.getGameInstance().getTeamManager().makeTeams();
        for (int i = 0; i <= startCountdown; i++) {

            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.JOINING || stopped) {
                        stopped = true;
                        return;
                    }

                    // at the alst second
                    if (finalI <= 0) {

                        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ACTIVE);

                        Bedwars.getInstance().getServer().broadcast(
                                Component.text("STARTING").color(NamedTextColor.YELLOW)
                        );
                        return;
                    }

                    Bedwars.getInstance().getServer().broadcast(
                            Component.text("Starting in " + finalI)
                                    .color(NamedTextColor.YELLOW)
                    );

                }
            }.runTaskLater(Bedwars.getInstance(), (startCountdown - i) * 20L);
        }
    }
}
