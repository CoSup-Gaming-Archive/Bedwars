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

        if (sender instanceof Player) {
            ItemGenerator itemGenerator = new ItemGenerator(((Player) sender).getLocation(), Material.EMERALD, 1, 1, 3, 0.2);
            itemGenerator.startSpawning();
        }

        return true;
    }
}
