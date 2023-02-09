package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.PlayerDamageManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.GameEndTask;
import eu.cosup.bedwars.tasks.SpectatorTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class PlayerDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getInventory().removeItem(itemStack);
            }
        }

        event.setCancelled(true);

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {

            new SpectatorTask(event.getPlayer(), false);

            return;
        }

        Player killer = PlayerDamageManager.getPlayerLastDamage(event.getPlayer());

        if (killer != null) {
            // same as before no null pointer exeptions
            if (Game.getGameInstance().getTeamManager().whichTeam(killer.getUniqueId()) == null) {
                killer = null;
            }
        }

        // if
        if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) == null) {
            return;
        }

        Bukkit.getLogger().info(event.getPlayer().getName());

        TextComponent.Builder killerText = Component.text().content(player.getName())
                .color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor()));

        // that means player was not damaged by other players
        if (killer == null) {
            killerText.append(Component.text().content(" committed suicide").color(NamedTextColor.YELLOW));
        } else {
            killer.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 200f, 2f);
            killerText
            .append(Component.text().content(" was killed by ").color(NamedTextColor.YELLOW))
            .append(Component.text().content(killer.getName()).color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(killer.getUniqueId()).getColor())));
            PlayerDamageManager.setPlayerLastDamage(event.getPlayer(), null);
        }

        if (!Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).isAlive()) {
            killerText.append(Component.text().content(" FINAL KILL").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA));

            new SpectatorTask(event.getPlayer(), false).runTask(Bedwars.getInstance());

            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) != null) {
                Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).setPlayerDead(player, true);
            }

            Bedwars.getInstance().getServer().sendMessage(killerText);

            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getAlivePlayers().size() == 0) {
                Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ENDING);
            }
            return;
        }

        Bedwars.getInstance().getServer().sendMessage(killerText);

        new SpectatorTask(event.getPlayer(), true).runTask(Bedwars.getInstance());
    }
}
