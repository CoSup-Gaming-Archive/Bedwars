package eu.cosup.bedwars.objects;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;



public enum TeamColor {

    RED,
    BLUE,
    YELLOW,
    GREEN;

    public static Color getColor(TeamColor teamColor) {

        if (teamColor == RED) {
            return Color.RED;
        }

        if (teamColor == BLUE) {
            return Color.BLUE;
        }

        if (teamColor == GREEN) {
            return Color.GREEN;
        }

        if (teamColor == YELLOW) {
            return Color.YELLOW;
        }

        return Color.BLACK;
    }

    public static NamedTextColor getNamedTextColor(TeamColor teamColor){

        if (teamColor == RED) {
            return NamedTextColor.RED;
        }

        if (teamColor == BLUE) {
            return NamedTextColor.BLUE;
        }

        if (teamColor == GREEN) {
            return NamedTextColor.GREEN;
        }

        if (teamColor == YELLOW) {
            return NamedTextColor.YELLOW;
        }
        
        return NamedTextColor.BLACK;
    }
}
