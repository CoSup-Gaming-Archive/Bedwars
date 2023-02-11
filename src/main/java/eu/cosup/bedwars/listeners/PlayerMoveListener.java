package eu.cosup.bedwars.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.tournament.common.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class PlayerMoveListener implements Listener {

    public PlayerMoveListener() {

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Bedwars.getInstance(), PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        ItemStack stack = packet.getItemModifier().read(0);

                        if (stack != null) {
                            event.setPacket(packet = packet.deepClone());

                            // Color that depends on the player's name
                            boolean isInvisible = event.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY);

                            if (isInvisible) {
                                packet.getModifier().write(0, null);
                            }
                        };
                    }
                });
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (!(Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId())==null)){
            int x=player.getLocation().getBlockX();
            int y=player.getLocation().getBlockY();
            int z=player.getLocation().getBlockZ();
            for (Team team: Game.getGameInstance().getTeamManager().getTeams()){
                int tx = team.getBase().getCenter().getBlockX();
                int ty = team.getBase().getCenter().getBlockY();
                int tz = team.getBase().getCenter().getBlockZ();
                int dx = tx-x;
                int dy = ty-y;
                int dz = tz-z;  //           x²   + y²   + z²
                double distance = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2)+Math.pow(dz, 2));
                if (distance<=team.getBase().getRadius()){
                    team.getBase().checkIfEnteredBase(player);
                } else {
                    team.getBase().removePlayerFromRange(player);
                }
            }


        }
        double playerY = event.getTo().getY();
        if (
                event.getPlayer().getGameMode() == GameMode.CREATIVE
                || event.getPlayer().getGameMode() == GameMode.SPECTATOR
        ) {
            return;
        }

        // if player is bellow the threshold
        if (Game.getGameInstance().getSelectedMap().getDeathHeight() > playerY) {
            // he die
            event.getPlayer().setHealth(0);
        }

        if (event.getPlayer().getLocation().getBlockX() > Game.getGameInstance().getSelectedMap().getxMax() ||
            event.getPlayer().getLocation().getBlockX() < Game.getGameInstance().getSelectedMap().getxMin()) {

            event.getPlayer().setHealth(0);
            return;
        }

        if (event.getPlayer().getLocation().getBlockZ() > Game.getGameInstance().getSelectedMap().getzMax() ||
            event.getPlayer().getLocation().getBlockZ() < Game.getGameInstance().getSelectedMap().getzMin()) {

            event.getPlayer().setHealth(0);
            return;
        }
    }
}
