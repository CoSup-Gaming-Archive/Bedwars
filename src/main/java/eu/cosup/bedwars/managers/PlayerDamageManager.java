package eu.cosup.bedwars.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerDamageManager {

    private static final HashMap<Player, Player> playerLastDamage = new HashMap<>();

    public static void setPlayerLastDamage(Player player, Player damager) {
        playerLastDamage.put(player, damager);
    }

    public static Player getPlayerLastDamage(Player player) {
        return playerLastDamage.get(player);
    }
}
