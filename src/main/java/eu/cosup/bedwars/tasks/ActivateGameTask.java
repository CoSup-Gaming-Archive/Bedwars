package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.PrivateChest;
import eu.cosup.bedwars.objects.TeamChest;
import eu.cosup.bedwars.objects.TeamColor;
import org.bukkit.*;
import org.bukkit.block.Bed;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivateGameTask extends BukkitRunnable {

    private ArrayList<Player> joinedPlayers;

    private static final List<String> armorPeaces = Bedwars.getInstance().getConfig().getStringList("armor");
    private static final ConfigurationSection hotbar = Bedwars.getInstance().getConfig().getConfigurationSection("hotbar");

    public ActivateGameTask(ArrayList<Player> joinedPlayers) {

        this.joinedPlayers = joinedPlayers;

    }

    @Override
    public void run() {

        prepareEnviroment();
        preparePlayers();
        spawnGenerators();
        spawnChests();
    }

    private void prepareEnviroment() {
        

        Bedwars.getInstance().getGameWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Bedwars.getInstance().getGameWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Bedwars.getInstance().getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bedwars.getInstance().getGameWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);

        // im pretty sure this is right
        Bedwars.getInstance().getGameWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);


        // qol for builders
        Bedwars.getInstance().getGameWorld().setGameRule(GameRule.DO_FIRE_TICK, false);


    }

    private void preparePlayers() {
        for (Player player : joinedPlayers) {
            preparePlayerFull(player);
        }
    }

    // ooo so juicy
    public static void preparePlayerFull(Player player) {
        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(player);
        // TODO nameTagEditor.setNameColor(TeamColor.getChatColor(teamColor)).setPrefix(teamColor.toString()+" ").setTabName(TeamColor.getChatColor(teamColor)+player.getName()).setChatName((TeamColor.getChatColor(teamColor)+player.getName()));
        preparePlayerStats(player);
        givePlayerArmor(player);
        givePlayerTools(player);
        teleportPlayerToSpawn(player);

    }

    // prepare player stats
    public static void preparePlayerStats(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(Integer.MAX_VALUE);
        player.setHealth(20);
    }

    public static void teleportPlayerToSpawn(Player player) {
        player.teleport(Game.getGameInstance().getSelectedMap().getSpawnByPlayer(player));
    }

    public static void givePlayerArmor(Player player) {

        for (String armorPeaceName : armorPeaces) {

            ItemStack armorPeace = new ItemStack(Objects.requireNonNull(Material.getMaterial(armorPeaceName)));
            ItemMeta meta = armorPeace.hasItemMeta() ? armorPeace.getItemMeta() : Bukkit.getItemFactory().getItemMeta(armorPeace.getType());
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            // from Color:
            leatherArmorMeta.setColor(TeamColor.getColor(Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor()));
            armorPeace.setItemMeta(leatherArmorMeta);

            // cheeky way but maybe there is a better method
            if (armorPeaceName.toLowerCase().contains("helmet")) {
                player.getInventory().setHelmet(armorPeace);
            }


            if (armorPeaceName.toLowerCase().contains("chestplate")) {
                player.getInventory().setChestplate(armorPeace);
            }


            if (armorPeaceName.toLowerCase().contains("leggings")) {
                player.getInventory().setLeggings(armorPeace);
            }


            if (armorPeaceName.toLowerCase().contains("boots")) {
                player.getInventory().setBoots(armorPeace);
            }
        }
    }

    public static void givePlayerTools(Player player) {

        if (hotbar == null) {
            return;
        }

        int i = 0;
        for (String itemName : hotbar.getKeys(false)) {

            Material material = Material.getMaterial(itemName);
            if (material != null) {
                player.getInventory().setItem(i, new ItemStack(material, hotbar.getInt(itemName)));
                i++;
            }
        }
    }

    public static boolean isItemDefault(Material item) {

        for (String itemName : hotbar.getKeys(false)) {

            if (item == Material.getMaterial(itemName)) {
                return true;
            }
        }

        for (String inventoryItem : armorPeaces) {

            if (Material.getMaterial(inventoryItem) == item) {
                return true;
            }
        }
        return false;
    }

    private void spawnGenerators() {
        Game.getGameInstance().getItemGeneratorManager().activateGenerators();
    }

    private void spawnChests() {
        for (Location location : Game.getGameInstance().getChestManager().getGameChestLocation()) {

            if (Game.getGameInstance().getChestManager().isTeamChest(location)) {
                Bedwars.getInstance().getGameWorld().setType(location, TeamChest.CHEST_BLOCK);
                continue;
            }

            if (Game.getGameInstance().getChestManager().isPrivateChest(location)) {
                Bedwars.getInstance().getGameWorld().setType(location, PrivateChest.CHEST_BLOCK);
            }
        }
    }
}
