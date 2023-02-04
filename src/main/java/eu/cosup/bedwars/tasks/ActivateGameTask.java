package eu.cosup.bedwars.tasks;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.PrivateChest;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamChest;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.tournament.common.objects.GameTeam;
import eu.cosup.tournament.server.TournamentServer;
import org.bukkit.*;
import org.bukkit.block.Bed;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ActivateGameTask extends BukkitRunnable {

    public ActivateGameTask() {
    }

    @Override
    public void run() {

        prepareEnviroment();
        preparePlayers();
        spawnGenerators();
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
        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {
            team.getPlayers().forEach(player -> preparePlayerFull(player, 0, new HashMap<>()));
        }
    }

    // ooo so juicy
    public static void preparePlayerFull(@NotNull Player player, int armorLevel, @Nullable HashMap<String, Integer> tools) {
        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor();
        // TODO NameTagEditor nameTagEditor = new NameTagEditor(player);
        // TODO nameTagEditor.setNameColor(TeamColor.getChatColor(teamColor)).setPrefix(teamColor.toString()+" ").setTabName(TeamColor.getChatColor(teamColor)+player.getName()).setChatName((TeamColor.getChatColor(teamColor)+player.getName()));

        preparePlayerStats(player, Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getUpgrades().getHaste());
        givePlayerArmor(player, Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getUpgrades().getProtection(), armorLevel);
        teleportPlayerToSpawn(player);

        if (tools == null) {
            givePlayerTools(player, Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getUpgrades().getSharpness(), new HashMap<>());
            return;
        }

        givePlayerTools(player, Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getUpgrades().getSharpness(), tools);
    }

    // prepare player stats
    public static void preparePlayerStats(@NotNull Player player, int upgradeLevel) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(Integer.MAX_VALUE);
        player.setHealth(20);
    }

    public static void teleportPlayerToSpawn(@NotNull Player player) {
        player.teleport(Game.getGameInstance().getSelectedMap().getSpawnByPlayer(player));
    }

    public static void givePlayerArmor(@NotNull Player player, int upgradeLevel, int armorLevel) {

        ItemStack leggings;
        ItemStack boots;

        switch (armorLevel) {
            default -> {
                leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                boots = new ItemStack(Material.LEATHER_BOOTS);
                addColor(leggings, player);
                addColor(boots, player);
            }
            case 1 -> {
                leggings = new ItemStack(Material.IRON_LEGGINGS);
                boots = new ItemStack(Material.IRON_BOOTS);
            }
            case 2 -> {
                leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                boots = new ItemStack(Material.DIAMOND_BOOTS);
            }
        }

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);

        addColor(helmet, player);
        addColor(chestplate, player);

        if (upgradeLevel > 0) {
            leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradeLevel);
            boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradeLevel);
            chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradeLevel);
            helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradeLevel);
        }

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }


    private static void addColor(@NotNull ItemStack armorPeace, @NotNull Player player) {
        ItemMeta meta = armorPeace.hasItemMeta() ? armorPeace.getItemMeta() : Bukkit.getItemFactory().getItemMeta(armorPeace.getType());
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
        // from Color:
        leatherArmorMeta.setColor(TeamColor.getColor(Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor()));
        leatherArmorMeta.setUnbreakable(true);
        armorPeace.setItemMeta(leatherArmorMeta);
    }

    public static void givePlayerTools(@NotNull Player player, int swordLevel, @NotNull HashMap<String, Integer> tools) {

        // TODO: 2/4/2023 rework this so its nicer
        // TODO: 2/4/2023 add tools

        player.getInventory().remove(Material.DIAMOND_SWORD);
        player.getInventory().remove(Material.IRON_SWORD);
        player.getInventory().remove(Material.WOODEN_SWORD);

        player.getInventory().remove(Material.DIAMOND_PICKAXE);
        player.getInventory().remove(Material.IRON_PICKAXE);
        player.getInventory().remove(Material.WOODEN_PICKAXE);

        player.getInventory().remove(Material.DIAMOND_AXE);
        player.getInventory().remove(Material.IRON_AXE);
        player.getInventory().remove(Material.WOODEN_AXE);

        player.getInventory().remove(Material.SHEARS);

        ItemStack sword;

        switch (swordLevel) {
            default -> {
                sword = new ItemStack(Material.WOODEN_SWORD, 1);
            }
            case 1 -> {
                sword = new ItemStack(Material.IRON_SWORD, 1);
            }
            case 2 -> {
                sword = new ItemStack(Material.DIAMOND_SWORD, 1);
            }
        }

        if (swordLevel > 0) {
            sword.addEnchantment(Enchantment.DAMAGE_ALL, swordLevel);
        }
        player.getInventory().setItem(0, sword);

        for (String tool : tools.keySet()) {

            Material material;

            switch (tools.get(tool)) {
                case 1 -> {
                    material = Material.getMaterial("IRON_"+tool);
                }
                case 2 -> {
                    material = Material.getMaterial("GOLDEN_"+tool);
                }
                case 3 -> {
                    material = Material.getMaterial("DIAMOND_"+tool);
                }
                default -> {
                    material = Material.AIR;
                }
            }

            ItemStack item = new ItemStack(material);
            player.getInventory().addItem(item);
        }
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
