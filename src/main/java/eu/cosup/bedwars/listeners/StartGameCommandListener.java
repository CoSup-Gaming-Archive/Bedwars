package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.tasks.StartCountdownTask;
import eu.cosup.tournament.server.commands.controls.StartGameCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class StartGameCommandListener implements Consumer<CommandSender> {

    public StartGameCommandListener() {
        StartGameCommand.registerConsumer(this);
    }

    @Override
    public void accept(CommandSender commandSender) {
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.JOINING) {
            commandSender.sendMessage(Component.text().content("You cannot do that right now.").color(NamedTextColor.RED));
            return;
        }

        new StartCountdownTask().runTask(Bedwars.getInstance());
    }

}
