package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.PlayerDamageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        // creative players can hit anyone
        if (((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (Game.getGameInstance().getTeamManager().whichTeam((Player) event.getEntity()).getColor() ==
            Game.getGameInstance().getTeamManager().whichTeam((Player) event.getDamager()).getColor()) {

            event.getDamager().sendMessage(Component.text().content("You cannot damage teammates").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        PlayerDamageManager.setPlayerLastDamage((Player) event.getEntity(), (Player) event.getDamager());
    }
}
