package eu.cosup.bedwars.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.cosup.bedwars.Bedwars;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ItemGenerator implements ConfigurationSerializable {

    private final Location location;
    private final Material item;
    private double dropTime;
    private final int maxLevel;
    private final double percentTimeDecreaseLevel;
    private int dropAmount;
    private int currentLevel = 1;

    private SpawnerTask spawnerTask;

    public ItemGenerator(Location location, Material item, double dropTime, int dropAmount, int maxLevel, double percentTimeDecreaseLevel) {
        this.location = location;
        this.item = item;
        this.dropTime = dropTime;
        this.maxLevel = maxLevel;
        this.percentTimeDecreaseLevel = percentTimeDecreaseLevel;
        this.dropAmount = dropAmount;
    }

    public void startSpawning() {
        spawnerTask = new SpawnerTask();
    }

    public void stopDropping() {
        assert spawnerTask != null;
        spawnerTask.cancel();
    }

    public void upgrade() {
        if (currentLevel < maxLevel) {
            currentLevel++;
            this.dropTime *= percentTimeDecreaseLevel;
        }
    }

    public void removeGenerator() {
        // we will want to remove the holograms that show that there is a generator there or something more nice
    }

    private void dropItem() {
        ItemStack drop = new ItemStack(item);
        drop.setAmount(dropAmount);
        location.getWorld().dropItem(location, drop);
    }

    public double getDropTime() {
        return dropTime;
    }

    public double getPercentTimeDecreaseLevel() {
        return percentTimeDecreaseLevel;
    }

    public int getDropAmount() {
        return dropAmount;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Location getLocation() {
        return location;
    }

    public Material getItem() {
        return item;
    }

    public SpawnerTask getSpawnerTask() {
        return spawnerTask;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("location", location);
        map.put("item", item.toString());
        map.put("dropTime", dropTime);
        map.put("maxLevel", maxLevel);
        map.put("percentTimeDecreaseLevel", percentTimeDecreaseLevel);
        map.put("dropAmount", dropAmount);

        return map;
    }

    public static ItemGenerator deserialize(ConfigurationSection configurationSection) {

        if (configurationSection.getString("item") == null) {
            Bukkit.getLogger().severe("While loading a generator material was a null string or material does not exist");
            return null;
        }

        return new ItemGenerator(
                configurationSection.getLocation("location"),
                Material.getMaterial(configurationSection.getString("item").toUpperCase()),
                configurationSection.getDouble("dropTime"),
                configurationSection.getInt("dropAmount"),
                configurationSection.getInt("maxLevel"),
                configurationSection.getDouble("percentTimeDecreaseLevel")
        );
    }

    class SpawnerTask extends BukkitRunnable {

        private SpawnerTask() {
            this.runTaskLater(Bedwars.getInstance(), (long) dropTime*20L);
        }

        @Override
        public void run() {
            dropItem();
            new SpawnerTask();
        }
    }
}
