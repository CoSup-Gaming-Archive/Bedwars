package eu.cosup.bedwars.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockExplodeEvent;

import java.net.http.WebSocket;

public class BlockExplodeListener implements Listener {
    @EventHandler
    public void onEvent(BlockExplodeEvent event){
        System.out.println(event.getBlock().getType());
        if (event.getBlock().getType()== Material.GLASS){
            event.setCancelled(true);
        }
    }
}
