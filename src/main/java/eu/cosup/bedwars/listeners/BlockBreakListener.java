package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.SideBarInformation;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.utility.BlockUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {


    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                return;
            }
        }
        if (block.getType().toString().contains("BED")) {

            TeamColor bedColor = Game.getGameInstance().getSelectedMap().whichTeamBed(block.getLocation()).getColor();

            TeamColor playerTeamColor = Game.getGameInstance().getTeamManager().whichTeam(player).getColor();

            if (bedColor == null || playerTeamColor == null) {
                event.setCancelled(true);
                return;
            }

            // so no own kill
            if (playerTeamColor == bedColor) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                    return;
                }
            }


            // it was no accident
            Team loserTeam = Game.getGameInstance().getTeamManager().getTeamByColor(bedColor);

            loserTeam.setAlive(false);
            SideBarInformation.update();

            // broadcast that they lost beacon
            Component msg = Component.text().content("A ").color(TextColor.color(NamedTextColor.YELLOW))
                    .append(Component.text().content(TeamColor.getFormattedTeamColor(loserTeam.getColor())+" bed").color(TeamColor.getNamedTextColor(loserTeam.getColor())))
                    .append(Component.text().content(" was destroyed!").color(NamedTextColor.YELLOW)).build();
            Bedwars.getInstance().getServer().broadcast(msg);

            // cheeky way of getting the beacon to not drop anything

            for (Location bedLocation :Game.getGameInstance().getSelectedMap().getTeamBedsFull().get(loserTeam.getColor())) {
                Bedwars.getInstance().getGameWorld().setType(bedLocation, Material.AIR);
            }

            event.setCancelled(true);
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (!Game.getGameInstance().getBlockManager().isBlockPlaced(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
