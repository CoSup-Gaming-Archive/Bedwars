package eu.cosup.bedwars.commands;

import eu.cosup.bedwars.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

// TODO fix file name (OpenShopCommand)
public class openshop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cYou cannot use this command, you have to be a player");
            return true;
        }

        if (!(player.hasPermission("bedwars.openshop"))) {
            player.sendMessage(Component.text("You don't have permission to use this").color(TextColor.color(255, 0, 0)));
            return true;
        }

        //ItemStack is = new ItemStack(Material.CHEST); // This was unused
        Game.getGameInstance().getShopManager().openShopForPlayer(player, null);
        return true;
    }
}
