package eu.cosup.bedwars.commands;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpectatorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            Component msg = Component.text().content("only player please").build();
            sender.sendMessage(msg);
            return false;
        }

        if (Game.getGameInstance() == null) {
            Component msg = Component.text().content("There is no game happening").color(NamedTextColor.RED).build();
            player.sendMessage(msg);
            return true;
        }

        // creative players can change team
        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            Component msg = Component.text().content("You cannot become spectator mid game").color(NamedTextColor.RED).build();
            player.sendMessage(msg);
            return true;
        }

        if (!Game.getGameInstance().getJoinedPlayers().contains(player)) {

            Component msg = Component.text().content("You joined the game").color(NamedTextColor.YELLOW).build();
            player.sendMessage(msg);
            Game.getGameInstance().getJoinedPlayers().add(player);
            Game.getGameInstance().refreshPlayerCount();
            return true;
        }

        Game.getGameInstance().getJoinedPlayers().remove(player);
        Game.getGameInstance().refreshPlayerCount();
        Component msg = Component.text().content("You are now a spectator").color(NamedTextColor.GRAY).build();
        player.sendMessage(msg);

        return true;
    }

}
