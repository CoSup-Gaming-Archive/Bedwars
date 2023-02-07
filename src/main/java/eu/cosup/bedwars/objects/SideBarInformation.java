package eu.cosup.bedwars.objects;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.ScoreBoardManager;
import eu.cosup.bedwars.tasks.GameTimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SideBarInformation {
    public static void update() {

        // scoreboard
        ScoreBoardManager scoreBoardManager = Game.getGameInstance().getScoreBoardManager();
        scoreBoardManager.clearObjective();
        scoreBoardManager.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&BBedwars"));

        scoreBoardManager.addItem(Component.text().content(" ").build());

        addFormattedTeamStrings(scoreBoardManager);
        scoreBoardManager.addItem(Component.text().content(" ").build());


        scoreBoardManager.addItem(Component.text().content(getFormattedTime()).color(NamedTextColor.YELLOW).build());
        scoreBoardManager.addItem(Component.text().content(" ").build());


        scoreBoardManager.addItem(Component.text().content("CoSup Gaming").color(NamedTextColor.GRAY).build());
        scoreBoardManager.setSlot(DisplaySlot.SIDEBAR);
        scoreBoardManager.getObjective();
    }

    private static String getFormattedTime() {

        int seconds = GameTimerTask.getSecondsElapsed();

        int minutes = seconds / 60;
        seconds = seconds - minutes * 60;

        if (minutes > 0) {
            return minutes+":"+seconds+" elapsed";
        }

        return seconds+" seconds";
    }

    private static void addFormattedTeamStrings(ScoreBoardManager scoreBoardManager) {

        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {


            TextComponent.Builder teamText = Component.text().content(TeamColor.getFormattedTeamColor(team.getColor())).color(TeamColor.getNamedTextColor(team.getColor()));

            if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
                teamText
                .append(getTeamSymbol(team));
            }

            scoreBoardManager.addItem(teamText.build());

        }
    }

    private static Component getTeamSymbol(@NotNull Team team) {
        if (team.isAlive()) {
            return Component.text().content("\u2714").color(NamedTextColor.GREEN).build();
        }
        if (team.getAlivePlayers().size() > 0) {
            return Component.text().content(" "+team.getAlivePlayers().size()).color(NamedTextColor.GREEN).build();
        }
        return Component.text().content("\u2716").color(NamedTextColor.RED).build();
    }
}
