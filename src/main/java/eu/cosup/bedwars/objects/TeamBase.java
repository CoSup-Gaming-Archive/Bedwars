package eu.cosup.bedwars.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class TeamBase {
    public ArrayList<Player> playersInRange = new ArrayList<>();
    public Location center;
    public Team team;
    public int radius;
    public TeamBase(Team team, Location location, int radius){
        this.team=team;
        this.center=location;
        this.radius=radius;
    }
    public void checkIfEnteredBase(Player player){
        if (!(this.team.base.playersInRange.contains(player))){
            playersInRange.add(player);
            this.enemyEnterEvent(player);
        } else {

        }
    }
    public void enemyEnterEvent(Player player){
        switch (team.upgrades.activatedTraps.get(0)){
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
                            Component.text(" from "+team.getColor() +" team just entered your base").color(TextColor.color(85, 255, 85)).decoration(TextDecoration.ITALIC, false)
                    )));

                    for (int index=0; index<5; index++){
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100.0f, 1.0f);
                    }
                }
            }
        }
        team.upgrades.activatedTraps.remove(0);
    }
}