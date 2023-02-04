package eu.cosup.bedwars.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemGenerator implements ConfigurationSerializable {

    public enum GeneratorType {
        SPAWN,
        DIAMOND,
        EMERALD;
    }

    private final Location location;
    private int currentLevel = 1;
    private GeneratorType type;
    private String name;

    private SpawnerTask spawnerTask;

    public ItemGenerator(String name, Location location, GeneratorType type) {
        this.location = location;
        this.type = type;
        this.name = name;
    }

    public void startSpawning() {
        spawnerTask = new SpawnerTask();
    }

    public void stopDropping() {
        assert spawnerTask != null;
        spawnerTask.cancel();
    }

    public void upgrade() {
        currentLevel++;
    }

    public GeneratorType getType() {
        return type;
    }

    private void dropItem() {
        switch (type) {
            case SPAWN -> {
                Team team = Game.getGameInstance().getTeamManager().getTeamWithName(name);
                assert team != null;

                switch (team.getUpgrades().getRessources()) {
                    default -> {
                        location.getWorld().dropItem(location, new ItemStack(Material.IRON_INGOT, 10));
                        location.getWorld().dropItem(location, new ItemStack(Material.GOLD_INGOT, 1));
                    }
                    case 1 -> {
                        location.getWorld().dropItem(location, new ItemStack(Material.IRON_INGOT, 20));
                        location.getWorld().dropItem(location, new ItemStack(Material.GOLD_INGOT, 5));
                    }
                    case 2 -> {
                        location.getWorld().dropItem(location, new ItemStack(Material.IRON_INGOT, 20));
                        location.getWorld().dropItem(location, new ItemStack(Material.GOLD_INGOT, 5));
                        location.getWorld().dropItem(location, new ItemStack(Material.EMERALD_BLOCK, 1));
                    }
                    case 3 -> {
                        location.getWorld().dropItem(location, new ItemStack(Material.IRON_INGOT, 40));
                        location.getWorld().dropItem(location, new ItemStack(Material.GOLD_INGOT, 10));
                        location.getWorld().dropItem(location, new ItemStack(Material.EMERALD_BLOCK, 2));
                    }
                }

            }
            case DIAMOND -> {
                location.getWorld().dropItem(location, new ItemStack(Material.DIAMOND, 1));
            }
            case EMERALD -> {
                location.getWorld().dropItem(location, new ItemStack(Material.EMERALD, 1));
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public SpawnerTask getSpawnerTask() {
        return spawnerTask;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("location", location);
        map.put("type", type);

        return map;
    }

    public static ItemGenerator deserialize(@NotNull String name, @NotNull ConfigurationSection configurationSection) {

        if (configurationSection.getString("type") == null) {
            throw new RuntimeException("While loading a generator material was a null string or material does not exist");
        }

        return new ItemGenerator(
                name,
                configurationSection.getLocation("location"),
                GeneratorType.valueOf(configurationSection.getString("type").toUpperCase())
        );
    }

    class SpawnerTask extends BukkitRunnable {

        private SpawnerTask() {
            switch (type) {
                case EMERALD, DIAMOND -> {
                    switch (currentLevel) {
                        case 1 -> {
                            this.runTaskLater(Bedwars.getInstance(), (long) 39*20L);
                        }
                        case 2 -> {
                            this.runTaskLater(Bedwars.getInstance(), (long) 30*20L);
                        }
                        case 3 -> {
                            this.runTaskLater(Bedwars.getInstance(), (long) 20*20L);
                        }
                    }
                }
                case SPAWN -> {
                    this.runTaskLater(Bedwars.getInstance(), (long) 8*20L);
                }
            }
        }

        @Override
        public void run() {
            dropItem();
            new SpawnerTask();
        }
    }
}
