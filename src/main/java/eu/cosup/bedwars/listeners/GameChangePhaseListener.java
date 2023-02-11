package eu.cosup.bedwars.listeners;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.events.ChangeGamePhaseEvent;
import eu.cosup.bedwars.events.ChangeGameStateEvent;
import eu.cosup.bedwars.interfaces.GameListener;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.tasks.TeamLoseBedTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


// this could be usefull for later
public class GameChangePhaseListener implements GameListener {

    public GameChangePhaseListener() {
        ChangeGamePhaseEvent.addListener(this);
        ChangeGameStateEvent.addListener(this);
    }

    @Override
    public void firedChangeGamePhaseEvent(@NotNull ChangeGamePhaseEvent event) {

        if (event.newGamePhase() == GameStateManager.GamePhase.DRAGONS) {

            for (TeamColor teamColor : Game.getGameInstance().getSelectedMap().getTeamBeds().keySet()) {
                Location location = Game.getGameInstance().getSelectedMap().getSpawnByColor(teamColor);
                location.setY(location.getY() + 30);

                Entity entity =  location.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
                EnderDragon enderDragon = (EnderDragon) entity;

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        enderDragon.setPhase(EnderDragon.Phase.CHARGE_PLAYER);

                        Team team = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor);

                        if (team != null) {
                            enderDragon.setTarget(team.getAlivePlayers().get(0));
                        }

                    }
                }.runTaskTimer(Bedwars.getInstance(), 20L*10, 10L*20L);
                enderDragon.setPodium(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            }
        }

        if (event.newGamePhase() == GameStateManager.GamePhase.BED_DESTRUCTION) {

            Component msg = Component.text().content("All beds have been destroyed!").color(NamedTextColor.RED).build();

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
            List<Team> teams = Game.getGameInstance().getTeamManager().getTeams().stream().filter(team1 -> team1.getAlivePlayers().size() > 0).toList();
            if (teams.size() == 0) {
                Game.getGameInstance().finishGame(null);
            }
            if (teams.size() == 1) {
                Game.getGameInstance().finishGame(teams.get(0).getColor());
            }
            if (teams.size() == 2) {
                throw new RuntimeException("There cannot be two alive teams in the ending phase");
            }
        }
    }
}
