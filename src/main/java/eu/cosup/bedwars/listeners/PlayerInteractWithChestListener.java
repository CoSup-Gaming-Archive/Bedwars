package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.PrivateChest;
import eu.cosup.bedwars.objects.TeamChest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayerInteractWithChestListener implements Listener {

    private static ArrayList<String> nonInteractBlocks = new ArrayList<>();

    static {
        nonInteractBlocks.add("bed");
        nonInteractBlocks.add("crafting");
        nonInteractBlocks.add("furnace");
        nonInteractBlocks.add("brewing");
        nonInteractBlocks.add("door");
    }

    public static boolean shouldInteract(@NotNull Material material) {
        for (String materialName : nonInteractBlocks) {
            if (material.toString().toLowerCase().contains(materialName)) {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    private void onPlayerInteractWithChest(PlayerInteractEvent event) {

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return;
        }

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
            if (!shouldInteract(event.getClickedBlock().getType())) {
                event.setCancelled(true);
            }
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
