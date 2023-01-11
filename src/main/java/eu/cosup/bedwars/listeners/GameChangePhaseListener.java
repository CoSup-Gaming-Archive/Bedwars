package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.ChangeGamePhaseEvent;
import eu.cosup.bedwars.events.ChangeGameStateEvent;
import eu.cosup.bedwars.interfaces.GameListenerInterface;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.SideBarInformation;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.TeamLoseBedTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;


// this could be usefull for later
public class GameChangePhaseListener implements GameListenerInterface {

    public GameChangePhaseListener() {
        ChangeGamePhaseEvent.addListener(this);
        ChangeGameStateEvent.addListener(this);
    }

    @Override
    public void firedChangeGamePhaseEvent(ChangeGamePhaseEvent event) {

        if (event.newGamePhase() == GameStateManager.GamePhase.SUDDEN_DEATH) {
            // spawn dragons
        }

        if (event.newGamePhase() == GameStateManager.GamePhase.BED_DESTRUCTION) {

            Component msg = Component.text().content("All beds have destroyed!" ).color(NamedTextColor.RED).build();

            Title title = Title.title(msg, Component.text().build());

            for (Player player : Bedwars.getInstance().getServer().getOnlinePlayers()) {
                player.showTitle(title);
            }

            for (TeamColor teamColor : Game.getGameInstance().getSelectedMap().getTeamBeds().keySet()) {
                new TeamLoseBedTask(teamColor, null);
            }

            Bedwars.getInstance().getGameWorld().playSound(
                    Game.getGameInstance().getSelectedMap().getSpectatorSpawn(),
                    Sound.ENTITY_ENDER_DRAGON_AMBIENT,
                    20,
                    1
            );
        }
    }

    @Override
    public void firedChangeGameStateEvent(ChangeGameStateEvent event) {

        if (event.newGameState().equals(GameStateManager.GameState.ACTIVE)) {
            Game.getGameInstance().activateGame();
        }

        if (event.newGameState().equals(GameStateManager.GameState.ENDING)) {
            Game.getGameInstance().finishGame(Game.getGameInstance().getTeamManager().getAliveTeam().getColor());
        }
    }
}
