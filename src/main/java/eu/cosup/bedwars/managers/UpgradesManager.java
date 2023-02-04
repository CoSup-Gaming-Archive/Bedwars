package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamUpgrades;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import eu.cosup.bedwars.utility.PlayerInventoryUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class UpgradesManager {
    public Component title = Component.text("Team Upgrades");
    public void openGUIForPlayer(Player player){
        Inventory gui = Bukkit.createInventory(null, 9*5, title);
        Team playerTeam = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId());
        if (playerTeam==null){
            player.sendMessage(Component.text("Seems like you can't access this right now"));
            return;
        }

        //Sharpness Upgrade

        ItemStack sharpness = new ItemStack(Material.IRON_SWORD);
        ItemMeta sharpnessMeta = sharpness.getItemMeta();
        Component sharpTitle = Component.text("Sharpened Swords").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //im doing it a little weird because the list doesnt support .add() so ye :)
        List<Component> sharpLoreList = List.of(
                Component.text("Your team permanently gains").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Sharpness I on all swords and").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("axes!").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text(" ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Cost: ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).append(
                        Component.text("8 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false)
                )
        );
        List<Component> sharpLore = new ArrayList<>(sharpLoreList);
        int diamondAmount = PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND);
        if (diamondAmount<8){
            sharpLore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            sharpLore.add(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        } else if (playerTeam.getUpgrades().getSharpness()==1){
            sharpLore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            sharpLore.add(Component.text("Your team already owns this upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        sharpnessMeta.lore(sharpLore);
        sharpnessMeta.displayName(sharpTitle);
        sharpness.setItemMeta(sharpnessMeta);
        gui.setItem(10, sharpness);



        //Protection Upgrade

        ItemStack protection = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta protectionMeta = sharpness.getItemMeta();
        String add=String.valueOf(playerTeam.getUpgrades().getProtection()+1);
        if (playerTeam.getUpgrades().getProtection()==4){
            add="";
        }
        Component protectionTitle = Component.text("Reinforced Armor "+add).color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //so the tiers u already have are like stikedthru
        boolean strike1=false;
        boolean strike2=false;
        boolean strike3=false;
        boolean strike4=false;
        int requiredDiamonds=5;
        if (playerTeam.getUpgrades().getProtection()>=4){
            strike4=true;
            requiredDiamonds=0;
        }
        if (playerTeam.getUpgrades().getProtection()>=3){
            strike3=true;
            requiredDiamonds=30;
        }
        if (playerTeam.getUpgrades().getProtection()>=2){
            strike2=true;
            requiredDiamonds=20;
        }
        if (playerTeam.getUpgrades().getProtection()>=1){
            strike1=true;
            requiredDiamonds=10;
        }
        List<Component> protectionLoreList = List.of(
                Component.text("Your team permanently gains").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Protection on all armor pieces!").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text(" ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Tier 1 Protection I, ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike1).append(
                        Component.text("5 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike1)
                ),
                Component.text("Tier 2 Protection II, ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike2).append(
                        Component.text("10 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike2)
                ),
                Component.text("Tier 3 Protection III, ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike3).append(
                        Component.text("20 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike3)
                ),
                Component.text("Tier 4 Protection IV, ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike4).append(
                        Component.text("30 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.STRIKETHROUGH, strike4)
                )
        );
        List<Component> protectionLore = new ArrayList<>(protectionLoreList);


        diamondAmount = PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND);
        if (diamondAmount<requiredDiamonds ){
            sharpLore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            sharpLore.add(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        } else if (playerTeam.getUpgrades().getSharpness()==1){
            sharpLore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            sharpLore.add(Component.text("Your team already owns this upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        protectionMeta.lore(protectionLore);
        protectionMeta.displayName(protectionTitle);
        protection.setItemMeta(protectionMeta);
        gui.setItem(11, protection);





        //=====================================
        //            Active Traps
        //=====================================

        //Alert Trap

        ItemStack alertTrap = getAlarmTrap();
        ItemMeta alertTrapMeta = alertTrap.getItemMeta();
        List<Component> lore =alertTrapMeta.lore();
        diamondAmount = PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND);
        if (diamondAmount<2){
            lore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        alertTrapMeta.lore(lore);
        alertTrap.setItemMeta(alertTrapMeta);
        gui.setItem(16, alertTrap);

        //Blindness and Slowness Trap

        ItemStack bsTrap = getBlindnessTrapItemStack();
        ItemMeta bsTrapMeta = bsTrap.getItemMeta();
        lore =bsTrapMeta.lore();
        diamondAmount = PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND);
        if (diamondAmount<2){
            lore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        bsTrapMeta.lore(lore);
        bsTrap.setItemMeta(bsTrapMeta);
        gui.setItem(14, bsTrap);

        //Mining Fatigue Trap

        ItemStack mfTrap = getMiningFatigueTrapItemStack();
        ItemMeta mfTrapMeta = mfTrap.getItemMeta();
        lore =mfTrapMeta.lore();
        diamondAmount = PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND);
        if (diamondAmount<2){
            lore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        mfTrapMeta.lore(lore);
        mfTrap.setItemMeta(mfTrapMeta);
        gui.setItem(23, mfTrap);

        //Counter-Offensive Trap

        ItemStack COTrap = getCounterOffensiveTrapItemStack();
        ItemMeta COTrapMeta = COTrap.getItemMeta();
        lore =COTrapMeta.lore();
        diamondAmount = PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND);
        if (diamondAmount<2){
            lore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        COTrapMeta.lore(lore);
        COTrap.setItemMeta(COTrapMeta);
        gui.setItem(15, COTrap);



        //======================================
        //            Show equiped traps
        //======================================


        ArrayList<ItemStack> showItems = new ArrayList<>();
        for (int index=0; index<3; index++){
            if (playerTeam.getUpgrades().getActivatedTraps().size()>=index+1){

                switch (playerTeam.getUpgrades().getActivatedTraps().get(index)){
                    case ALARM -> {
                        showItems.add(getAlarmTrap());
                    }
                    case BLINDNESS -> {
                        showItems.add(getBlindnessTrapItemStack());
                    }
                    case MINING_FATIGUE -> {
                        showItems.add(getMiningFatigueTrapItemStack());
                    }
                    case OFFENSIVE -> {
                        showItems.add(getCounterOffensiveTrapItemStack());
                    }

                }
            } else {
                showItems.add(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
            }
        }
        gui.setItem(39, showItems.get(0));
        gui.setItem(40, showItems.get(1));
        gui.setItem(41, showItems.get(2));

        //fill up empty slots
        for (int index=0; index<9*5; index++){
            if (gui.getItem(index)==null){
                gui.setItem(index, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
            }
        }

        player.openInventory(gui);
    }
    public ItemStack getAlarmTrap(){
        ItemStack alertTrap = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta alertTrapMeta = alertTrap.getItemMeta();
        Component alertTrapTitle = Component.text("Alarm Trap").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //im doing it a little weird because the list doesnt support .add() so ye :)
        List<Component> alertTrapLoreList = List.of(
                Component.text("Reveals invisible players as").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("well as their name and team").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text(" ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Cost: ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).append(
                        Component.text("2 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false)
                )
        );
        List<Component> alertTrapLore = new ArrayList<>(alertTrapLoreList);
        alertTrapMeta.lore(alertTrapLore);
        alertTrapMeta.displayName(alertTrapTitle);
        alertTrap.setItemMeta(alertTrapMeta);
        return alertTrap;

    }
    public ItemStack getBlindnessTrapItemStack(){
        ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component itemName = Component.text("It's a trap!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //im doing it a little weird because the list doesnt support .add() so ye :)
        List<Component> itemLoreList = List.of(
                Component.text("Inflicts Blindness and Slowness").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("for 8 seconds").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text(" ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Cost: ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).append(
                        Component.text("2 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false)
                )
        );
        List<Component> itemLore = new ArrayList<>(itemLoreList);
        itemMeta.lore(itemLore);
        itemMeta.displayName(itemName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }
    public ItemStack getMiningFatigueTrapItemStack(){
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component itemName = Component.text("Miner Fatigue Trap").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //im doing it a little weird because the list doesnt support .add() so ye :)
        List<Component> itemLoreList = List.of(
                Component.text("Inflict Mining Fatigue for 10").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("10 seconds.").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text(" ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Cost: ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).append(
                        Component.text("2 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false)
                )
        );
        List<Component> itemLore = new ArrayList<>(itemLoreList);
        itemMeta.lore(itemLore);
        itemMeta.displayName(itemName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public ItemStack getCounterOffensiveTrapItemStack(){
        ItemStack itemStack = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component itemName = Component.text("Counter-Offensive Trap").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //im doing it a little weird because the list doesnt support .add() so ye :)
        List<Component> itemLoreList = List.of(
                Component.text("Grants Speed II and Jump Boost").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("II for 15 seconds to allied").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("players near your base.").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text(" ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false),
                Component.text("Cost: ").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false).append(
                        Component.text("2 diamonds").color(TextColor.color(85, 255, 255)).decoration(TextDecoration.ITALIC, false)
                )
        );
        List<Component> itemLore = new ArrayList<>(itemLoreList);
        itemMeta.lore(itemLore);
        itemMeta.displayName(itemName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public boolean checkForFreeTrapSlots(Team team){
        return team.getUpgrades().getActivatedTraps().size()>=3;
    }
    public void onClick(int slot, Player player, Inventory shop){

        if (shop.getItem(slot).getType()!=Material.BLACK_STAINED_GLASS_PANE && shop.getItem(slot).getType()!=Material.LIGHT_GRAY_STAINED_GLASS_PANE && slot<36){
            Team playerTeam = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId());
            if (shop.getItem(slot).getType()==Material.IRON_SWORD){

                if (playerTeam.getUpgrades().getSharpness()==1){
                    player.sendMessage(Component.text("You already own that team upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                }
                if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=8){
                    PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, 8);
                    playerTeam.getUpgrades().setSharpness(playerTeam.getUpgrades().getSharpness() + 1);
                    for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                        Game.getGameInstance().getShopManager().getPlayerTools().putIfAbsent(player.getName(), new HashMap<>());
                        ActivateGameTask.givePlayerTools(player, Game.getGameInstance().getTeamManager().whichTeam(teamPlayer.getUniqueId()).getUpgrades().getSharpness(), Game.getGameInstance().getShopManager().getPlayerTools().get(teamPlayer.getName()));
                        teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Sharpened Swords for the whole team").color(TextColor.color(85, 255, 85))));
                    }
                    openGUIForPlayer(player);
                } else {
                    player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                }
            } else if (shop.getItem(slot).getType()==Material.IRON_CHESTPLATE){


                if (playerTeam.getUpgrades().getSharpness()>=4){
                    player.sendMessage(Component.text("You already own that team upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                } else {
                    int reqDiamonds=0;
                    if (playerTeam.getUpgrades().getProtection()==0){
                        reqDiamonds=5;
                    } else if (playerTeam.getUpgrades().getProtection()==1){
                        reqDiamonds=10;
                    } else if (playerTeam.getUpgrades().getProtection()==2){
                        reqDiamonds=20;
                    } else if (playerTeam.getUpgrades().getProtection()==3){
                        reqDiamonds=30;
                    }
                    if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=reqDiamonds){
                        PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, reqDiamonds);
                        playerTeam.getUpgrades().setProtection(playerTeam.getUpgrades().getProtection() + 1);
                        for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                            Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().putIfAbsent(player.getName(), 0);
                            ActivateGameTask.givePlayerArmor(player, Game.getGameInstance().getTeamManager().whichTeam(teamPlayer.getUniqueId()).getUpgrades().getProtection(), Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().get(teamPlayer.getName()));
                            teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Reinforced Armor "+ String.valueOf(playerTeam.getUpgrades().getProtection())+" for the whole team").color(TextColor.color(85, 255, 85))));
                        }
                        openGUIForPlayer(player);
                    } else {
                        player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                        openGUIForPlayer(player);
                        return;
                    }
                }

            } else if (shop.getItem(slot).getType()==Material.REDSTONE_TORCH){
                if (checkForFreeTrapSlots(playerTeam)){
                    player.sendMessage(Component.text("You already have 3 traps equiped").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                } else {
                    if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=2){
                        PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, 2);
                        playerTeam.getUpgrades().getActivatedTraps().add(TeamUpgrades.traps.ALARM);
                        for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                            teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Alarm Trap for the whole team").color(TextColor.color(85, 255, 85))));
                        }
                        openGUIForPlayer(player);
                    } else {
                        player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                        openGUIForPlayer(player);
                        return;
                    }
                }

            } else if (shop.getItem(slot).getType()==Material.TRIPWIRE_HOOK){
                if (checkForFreeTrapSlots(playerTeam)){
                    player.sendMessage(Component.text("You already have 3 traps equiped").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                } else {
                    if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=2){
                        PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, 2);
                        playerTeam.getUpgrades().getActivatedTraps().add(TeamUpgrades.traps.BLINDNESS);
                        for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                            teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought It's a trap! for the whole team").color(TextColor.color(85, 255, 85))));
                        }
                        openGUIForPlayer(player);
                    } else {
                        player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                        openGUIForPlayer(player);
                        return;
                    }
                }

            } else if (shop.getItem(slot).getType()==Material.IRON_PICKAXE){
                if (checkForFreeTrapSlots(playerTeam)){
                    player.sendMessage(Component.text("You already have 3 traps equiped").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                } else {
                    if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=2){
                        PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, 2);
                        playerTeam.getUpgrades().getActivatedTraps().add(TeamUpgrades.traps.MINING_FATIGUE);
                        for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                            teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Miner Fatigue Trap for the whole team").color(TextColor.color(85, 255, 85))));
                        }
                        openGUIForPlayer(player);
                    } else {
                        player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                        openGUIForPlayer(player);
                        return;
                    }
                }

            } else if (shop.getItem(slot).getType()==Material.FEATHER){
                if (checkForFreeTrapSlots(playerTeam)){
                    player.sendMessage(Component.text("You already have 3 traps equiped").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                } else {
                    if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=2){
                        PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, 2);
                        playerTeam.getUpgrades().getActivatedTraps().add(TeamUpgrades.traps.OFFENSIVE);
                        for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                            teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Counter-Offensive Trap for the whole team").color(TextColor.color(85, 255, 85))));
                        }
                        openGUIForPlayer(player);
                    } else {
                        player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                        openGUIForPlayer(player);
                        return;
                    }
                }

            }
        }
        openGUIForPlayer(player);
    }
}
