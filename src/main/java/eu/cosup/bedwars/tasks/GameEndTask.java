package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.bedwars.objects.SideBarInformation;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class GameEndTask extends BukkitRunnable {

    private final TeamColor winner;

    public GameEndTask(@Nullable TeamColor winner) {
        this.winner = winner;
    }

    @Override
    public void run() {

        Team winnerTeam = Game.getGameInstance().getTeamManager().getTeamByColor(winner);

        Location baseCenter = winnerTeam.getBase().getCenter();
        int x = baseCenter.getBlockX();
        int y = baseCenter.getBlockY();
        int z = baseCenter.getBlockZ();
        for (int xIndex = -8; xIndex<8; xIndex++){
            for (int yIndex = -8; yIndex<8; yIndex++){
                for (int zIndex = -8; zIndex<8; zIndex++) {
                    int bx = x + xIndex;
                    int by = y + yIndex;
                    int bz = z + zIndex;
                    double distance = Math.sqrt(Math.pow(xIndex, 2) + Math.pow(yIndex, 2) + Math.pow(zIndex, 2));
                    if (distance <= 8) {
                        baseCenter.getWorld().getBlockAt(x + xIndex, y + yIndex, z + zIndex).setType(Material.AIR, false);
                    }
                }
            }
        }
        /*for (int xIndex = -15; xIndex<15; xIndex++){
            for (int yIndex = -15; yIndex<15; yIndex++){
                for (int zIndex = -15; zIndex<15; zIndex++){
                    Block blockAt = baseCenter.getWorld().getBlockAt(x+xIndex, y+yIndex, z+zIndex);
                    if (!(blockAt.getType().isAir())){
                        blockAt.setType(Material.GOLD_BLOCK);
                    }
                }
            }
        }*/
        for (int xi = -2; xi<=2; xi++){
            for (int yi = 0; yi<=y-2; yi++){
                for (int zi = -2; zi<=2; zi++){
                    double distance = Math.sqrt(Math.pow(xi, 2) + Math.pow(zi, 2));
                    if (distance<=2.3) {
                        if (yi == y - 2) {
                            baseCenter.getWorld().getBlockAt(baseCenter.getBlockX() + xi, yi, baseCenter.getBlockZ() + zi).setType(Material.POLISHED_ANDESITE);
                        } else if (yi == y - 3) {
                            baseCenter.getWorld().getBlockAt(baseCenter.getBlockX() + xi, yi, baseCenter.getBlockZ() + zi).setType(Material.DIORITE_WALL);
                        } else {
                            baseCenter.getWorld().getBlockAt(baseCenter.getBlockX() + xi, yi, baseCenter.getBlockZ() + zi).setType(Material.ANDESITE);
                        }
                    }
                }
            }
        }
        for (Player player: winnerTeam.getOnlinePlayers()){
            player.teleport(baseCenter);
        }
        for (Player player: Bukkit.getOnlinePlayers()){
            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId())!=winnerTeam){
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(baseCenter);
            }

        }

        GameTimerTask.getInstance().cancelTimer();
        Game.getGameInstance().getItemGeneratorManager().deactivateGenerators();

        if (winnerTeam != null) {
            for (Player player : winnerTeam.getPlayers()) {

                Location playerLocation = player.getLocation();

                for (int i = 0; i < 1; i++) {
                    Bedwars.getInstance().getGameWorld().spawnEntity(playerLocation, EntityType.FIREWORK);
                }
            }
        }

        Component msg = Component.text().content(String.valueOf(winner)).color(TeamColor.getNamedTextColor(winner))
                .append(Component.text().content(" is the winner!").color(NamedTextColor.YELLOW)).build();
        Bedwars.getInstance().getServer().broadcast(msg);

        SideBarInformation.update();

        Bukkit.getLogger().warning("Shutting down in: " + Bedwars.getInstance().getConfig().getInt("return-to-lobby-delay"));
        new BukkitRunnable() {
            @Override
            public void run() {

                Component msg = Component.text().content("server is shutting down").color(NamedTextColor.RED).build();
                Bedwars.getInstance().getServer().broadcast(msg);

                Bedwars.getInstance().getServer().shutdown();

            }
        }.runTaskLater(Bedwars.getInstance(), Bedwars.getInstance().getConfig().getInt("return-to-lobby-delay") * 20L);
    }
}
