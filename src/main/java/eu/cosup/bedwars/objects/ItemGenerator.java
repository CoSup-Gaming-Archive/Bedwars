package eu.cosup.bedwars.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.managers.GameStateManager;
import eu.cosup.tournament.server.item.ItemBuilder;
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
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
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
    private int count = 0;

    private SpawnerTask spawnerTask;

    public ItemGenerator(String name, Location location, GeneratorType type) {
        this.location = location;
        this.location.setX(location.getX() + 0.5);
        this.location.setZ(location.getZ() + 0.5);
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
        count++;

        switch (type) {
            case SPAWN -> {
                Team team = Game.getGameInstance().getTeamManager().getTeamWithName(name);

                if (team == null) {
                    return;
                }
                switch (team.getUpgrades().getRessources()) {
                    default -> {
                        if (count % 10 == 0) {
                            location.getWorld().dropItem(location, ItemBuilder.of(Material.GOLD_INGOT).amount(1).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                        }
                        location.getWorld().dropItem(location, ItemBuilder.of(Material.IRON_INGOT).amount(1).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                    }
                    case 2 -> {
                        if (count % 10 == 0) {
                            location.getWorld().dropItem(location, ItemBuilder.of(Material.GOLD_INGOT).amount(1).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                        }
                        location.getWorld().dropItem(location, ItemBuilder.of(Material.IRON_INGOT).amount(2).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                    }
                    case 3 -> {
                        if (count % 10 == 0) {
                            location.getWorld().dropItem(location, ItemBuilder.of(Material.EMERALD).amount(1).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                            location.getWorld().dropItem(location, ItemBuilder.of(Material.GOLD_INGOT).amount(2).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                        }
                        location.getWorld().dropItem(location, ItemBuilder.of(Material.IRON_INGOT).amount(2).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                    }
                    case 4 -> {
                        if (count % 10 == 0) {
                            location.getWorld().dropItem(location, ItemBuilder.of(Material.EMERALD).amount(2).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                            location.getWorld().dropItem(location, ItemBuilder.of(Material.GOLD_INGOT).amount(4).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
                        }
                        location.getWorld().dropItem(location, ItemBuilder.of(Material.IRON_INGOT).amount(4).lore(new ArrayList<>()).build()).setVelocity(new Vector().zero());
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
                    this.runTaskLater(Bedwars.getInstance(), (long) 20L);
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
