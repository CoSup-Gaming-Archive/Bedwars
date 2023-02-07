package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEvent (EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player player)){
            return;
        }
        if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId())==null){
            return;
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
            Game.getGameInstance().updatePlayersNameTag(player);
        }, 1L);
    }
}
