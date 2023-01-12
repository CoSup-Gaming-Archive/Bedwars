package eu.cosup.bedwars.commands;

import eu.cosup.bedwars.objects.ItemGenerator;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GeneratorSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // TODO: check for permissions even if you will remove it in prod

        if (sender instanceof Player player) {
            ItemGenerator itemGenerator = new ItemGenerator(player.getLocation(), Material.EMERALD, 1, 1, 3, 0.2);
            itemGenerator.startSpawning();
        }

        return true;
    }
}
