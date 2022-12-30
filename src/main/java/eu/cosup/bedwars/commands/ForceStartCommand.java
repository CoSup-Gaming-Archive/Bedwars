package eu.cosup.bedwars.commands;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ForceStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.JOINING) {
            sender.sendMessage("You can only force when players are joining.");
            return true;
        }

        // TODO new StartCountdownTask().runTask(Cores.getInstance());
        Bedwars.getInstance().getServer().broadcast(Component.text(sender.getName()+" issued a force start.").color(NamedTextColor.RED));

        return true;
    }

}
