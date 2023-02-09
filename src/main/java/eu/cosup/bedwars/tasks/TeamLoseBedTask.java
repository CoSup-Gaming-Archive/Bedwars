package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.SideBarInformation;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamLoseBedTask extends BukkitRunnable {

    private TeamColor bedColor;
    private Player killer;

    public TeamLoseBedTask(TeamColor bedColor, Player killer) {
        this.bedColor = bedColor;
        this.killer = killer;

        this.runTask(Bedwars.getInstance());
    }

    @Override
    public void run() {

        Team playerTeam = null;

        if (killer != null) {
            playerTeam = Game.getGameInstance().getTeamManager().whichTeam(killer.getUniqueId());
        }


        TeamColor playerTeamColor = null;

        if (playerTeam != null) {
            playerTeamColor = playerTeam.getColor();
        }

        // so no own kill
        if (playerTeamColor == bedColor) {
            if (killer.getGameMode() != GameMode.CREATIVE) {
                return;
            }
        }

        // it was no accident
        Team loserTeam = Game.getGameInstance().getTeamManager().getTeamByColor(bedColor);

        if (loserTeam != null) {
            loserTeam.setAlive(false);

            for (Location bedLocation :Game.getGameInstance().getSelectedMap().getTeamBedsFull().get(loserTeam.getColor())) {
                Bedwars.getInstance().getGameWorld().setType(bedLocation, Material.AIR);
            }

            for (Player alivePlayer : loserTeam.getAlivePlayers()) {
                Title title = Title.title(Component.text("Your bed has been detroyed").color(NamedTextColor.RED), Component.text().build());
                alivePlayer.showTitle(title);
                alivePlayer.playSound(alivePlayer.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
            }
        }

        SideBarInformation.update();

        // broadcast that they lost bed
        Component msg = Component.text().content("A ").color(TextColor.color(NamedTextColor.YELLOW))
                .append(Component.text().content(TeamColor.getFormattedTeamColor(loserTeam.getColor())+" bed").color(TeamColor.getNamedTextColor(loserTeam.getColor())))
                .append(Component.text().content(" was destroyed!").color(NamedTextColor.YELLOW)).build();

        Bedwars.getInstance().getServer().broadcast(msg);
    }
}
