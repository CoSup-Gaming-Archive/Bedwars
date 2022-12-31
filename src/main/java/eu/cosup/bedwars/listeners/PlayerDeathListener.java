package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.managers.PlayerDamageManager;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.SpectatorTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.block.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class PlayerDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getInventory().setItem(EquipmentSlot.HAND, itemStack);
                player.dropItem(true);
                player.getInventory().removeItem(itemStack);
            }
        }

        event.setCancelled(true);

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {

            // after game ends and stuff
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            return;
        }

        Player killer = PlayerDamageManager.getPlayerLastDamage(event.getPlayer());

        // same as before no null pointer exeptions
        if (Game.getGameInstance().getTeamManager().whichTeam(killer) == null) {
            killer = null;
        }

        TextComponent.Builder killerText = Component.text().content(player.getName()).color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(player).getColor()));

        // that means player was not damaged by other players
        if (killer == null) {
            killerText.append(Component.text().content(" committed suicide").color(NamedTextColor.YELLOW));
        } else {
            killerText
            .append(Component.text().content(" was killed by ").color(NamedTextColor.YELLOW))
            .append(Component.text().content(killer.getName()).color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(killer).getColor())));
            PlayerDamageManager.setPlayerLastDamage(event.getPlayer(), null);
        }


        if (!Game.getGameInstance().getTeamManager().whichTeam(player).isAlive()) {
            killerText
            .append(Component.text().content(" FINAL KILL").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA));

            player.teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            player.setGameMode(GameMode.SPECTATOR);
            Bedwars.getInstance().getServer().sendMessage(killerText);
            return;
        }

        Bedwars.getInstance().getServer().sendMessage(killerText);

        new SpectatorTask(event.getPlayer()).runTask(Bedwars.getInstance());
    }
}
