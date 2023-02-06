package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.tasks.GameTimerTask;
import eu.cosup.tournament.server.commands.controls.FreezeGameCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class GameFreezeListener implements Consumer<CommandSender> {

    public GameFreezeListener() {
        FreezeGameCommand.registerConsumer(this);
    }

    @Override
    public void accept(CommandSender commandSender) {
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return;
        }

        if (GameTimerTask.getInstance() != null) {
            GameTimerTask.getInstance().cancelTimer();
        }
    }
}
