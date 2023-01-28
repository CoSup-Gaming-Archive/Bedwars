package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.tasks.GameEndTask;
import eu.cosup.tournament.server.commands.controls.EndGameCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class EndGameCommandListener implements Consumer<CommandSender> {

    public EndGameCommandListener() {
        EndGameCommand.registerConsumer(this);
    }

    @Override
    public void accept(CommandSender commandSender) {
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            commandSender.sendMessage(Component.text().content("You cannot do that right now.").color(NamedTextColor.RED));
            return;
        }

        new GameEndTask(null).runTask(Bedwars.getInstance());
    }

}
