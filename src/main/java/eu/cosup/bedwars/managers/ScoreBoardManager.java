package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Bedwars;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

public class ScoreBoardManager {

    private Objective objective;

    private String name;
    ArrayList<Component> displayStrings = new ArrayList<>();

    public ScoreBoardManager(String name) {
        if (name.length() == 0) {
            return;
        }

        this.name = name;
        registerScoreBoard(name);
    }

    private void registerScoreBoard(String name) {
        try {
            // try registering the objective
            this.objective = Bedwars.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewObjective(
                    name,
                    Criteria.create("dummy"),
                    Component.text("name")
            );
        } catch (IllegalArgumentException ignored) {
            // if the objective already exists
            this.objective = Bedwars.getInstance().getServer().getScoreboardManager().getMainScoreboard().getObjective(name);
        }
    }

    public void setDisplayName(String name) {
        objective.setDisplayName(name);
    }

    public void setSlot(DisplaySlot displaySlot) {
        objective.setDisplaySlot(displaySlot);
    }

    public void addItem(Component component) {
        displayStrings.add(component);
    }

    public void getObjective() {
        int displayStringCount = displayStrings.size() - 1;
        for (int i = 0; i < displayStrings.size(); i++) {

            Component displayComponent = displayStrings.get(i);

            // we are using hex even though it is not really acurate.
            String displayText = LegacyComponentSerializer.legacy(LegacyComponentSerializer.HEX_CHAR).serialize(displayComponent);

            this.objective.getScore(ChatColor.translateAlternateColorCodes('#', displayText)).setScore(displayStringCount - i);
        }
    }

    public void clearObjective() {
        String id = objective.getName();
        Scoreboard scoreboard = objective.getScoreboard();
        Component name = objective.displayName();
        objective.unregister();

        if (scoreboard == null) {
            registerScoreBoard(this.name);
        }

        assert scoreboard != null;
        objective = scoreboard.registerNewObjective(id, Criteria.create("dummy"), name);
    }
}

