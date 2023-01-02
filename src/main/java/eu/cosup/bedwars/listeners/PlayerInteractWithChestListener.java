package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.PrivateChest;
import eu.cosup.bedwars.objects.TeamChest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractWithChestListener implements Listener {

    @EventHandler
    private void onPlayerInteractWithChest(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) {
            return;
        }

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE ||
            event.getPlayer().getGameMode() == GameMode.SPECTATOR){
            return;
        }

        if (event.getClickedBlock().getType() != PrivateChest.CHEST_BLOCK &&
            event.getClickedBlock().getType() != TeamChest.CHEST_BLOCK
        ) {
            event.setCancelled(true);
            return;
        }

        if (!Game.getGameInstance().getChestManager().isGameChest(event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        if (!Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer()).getColor().equals(
            Game.getGameInstance().getChestManager().getTeamChest(event.getClickedBlock().getLocation()).getTeamColor())
        ) {
            event.getPlayer().sendMessage(Component.text().content("You cannot open this chest").color(NamedTextColor.RED));
            return;
        }

        event.setCancelled(false);
    }
}
