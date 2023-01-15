package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.PlayerDamageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    private void onEntityDamageEntity(@NotNull EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() == EntityType.PRIMED_TNT) {
            // we want less damage from tnt
            event.setDamage(event.getDamage()/8);
        }

        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }

        // creative players can hit anyone
        if (damager.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!(event.getEntity() instanceof Player damaged)) {
            return;
        }

        if (Game.getGameInstance().getTeamManager().whichTeam(damaged.getUniqueId()).getColor() ==
            Game.getGameInstance().getTeamManager().whichTeam(damager.getUniqueId()).getColor()) {

            damager.sendMessage(Component.text().content("You cannot damage teammates").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        PlayerDamageManager.setPlayerLastDamage((Player) event.getEntity(), (Player) event.getDamager());
    }
}
