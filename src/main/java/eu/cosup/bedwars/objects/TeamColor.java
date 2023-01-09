package eu.cosup.bedwars.objects;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;



public enum TeamColor {

    RED,
    BLUE;

    public static Color getColor(TeamColor teamColor) {

        if (teamColor == RED) {
            return Color.RED;
        }

        if (teamColor == BLUE) {
            return Color.BLUE;
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
        
        return NamedTextColor.BLACK;
    }

    public static String getFormattedTeamColor(TeamColor teamColor) {
        return teamColor.toString().substring(0, 1).toUpperCase()+teamColor.toString().substring(1).toLowerCase();
    }
}
