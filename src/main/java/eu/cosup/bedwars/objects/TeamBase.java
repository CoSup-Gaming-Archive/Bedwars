package eu.cosup.bedwars.objects;

import eu.cosup.bedwars.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.sql.Timestamp;
import java.util.ArrayList;

public class TeamBase {
    public ArrayList<Player> playersInRange = new ArrayList<>();
    public Location center;
    public Team team;
    public int radius;
    public long lastBaseEntering=0;
    public TeamBase(Team team, Location location, int radius){
        this.team=team;
        this.center=location;
        this.radius=radius;
    }
    public void checkIfEnteredBase(Player player){
        if (!(this.team.getBase().playersInRange.contains(player))){
            playersInRange.add(player);
            long now = System.currentTimeMillis();
            if (now-20*1000>=lastBaseEntering){
                lastBaseEntering=now;
                this.enemyEnterEvent(player);
            }

        } else {

        }
    }
    public void enemyEnterEvent(Player player){
        switch (team.getUpgrades().getActivatedTraps().get(0)){
            //TODO traps ignore staff members
            case BLINDNESS -> {
                PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 20*8, 0);
                PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 20*8, 3);
                player.addPotionEffect(blindness);
                player.addPotionEffect(slowness);

            }
            case ALARM -> {
                for (Player teamPlayer : team.getAlivePlayers()){
                    teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).decoration(TextDecoration.ITALIC, false).append(
                            Component.text(" just entered your base").color(TextColor.color(85, 255, 85)).decoration(TextDecoration.ITALIC, false)
                    ));
                    teamPlayer.showTitle(Title.title(Component.text("Base invasion!").color(TextColor.color(85, 255, 85)).decoration(TextDecoration.ITALIC, false), Component.text(player.getName()).color(TextColor.color(255, 255, 85)).decoration(TextDecoration.ITALIC, false).append(
                            Component.text(" from "+ Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor() +" team just entered your base").color(TextColor.color(85, 255, 85)).decoration(TextDecoration.ITALIC, false)
                    )));

                    for (int index=0; index<5; index++){
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100.0f, 1.0f);
                    }
                }
            }
            case MINING_FATIGUE -> {
                PotionEffect miningFatigue = new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*10, 2);
                player.addPotionEffect(miningFatigue);
            }
            case OFFENSIVE -> {
                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 20*15, 1);
                PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, 20*15, 1);
                int tx = team.getBase().center.getBlockX();
                int ty = team.getBase().center.getBlockY();
                int tz = team.getBase().center.getBlockZ();
                for (Player teamPlayer: team.getAlivePlayers()){
                    int x = teamPlayer.getLocation().getBlockX();
                    int y = teamPlayer.getLocation().getBlockY();
                    int z = teamPlayer.getLocation().getBlockZ();
                    int dx = tx-x;
                    int dy = ty-y;
                    int dz = tz-z;
                    double distance = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2)+Math.pow(dz, 2));
                    if (distance<=radius){
                        teamPlayer.addPotionEffect(speed);
                        teamPlayer.addPotionEffect(jump);
                    }
                }
            }
        }
        team.getUpgrades().getActivatedTraps().remove(0);
    }
}