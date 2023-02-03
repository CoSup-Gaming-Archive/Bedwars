package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
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

import java.util.ArrayList;
import java.util.Arrays;
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
        } else if (playerTeam.upgrades.sharpness==1){
            sharpLore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            sharpLore.add(Component.text("Your team already owns this upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        sharpnessMeta.lore(sharpLore);
        sharpnessMeta.displayName(sharpTitle);
        sharpness.setItemMeta(sharpnessMeta);
        gui.setItem(10, sharpness);


        //Protection Upgrade

        ItemStack protection = new ItemStack(Material.IRON_SWORD);
        ItemMeta protectionMeta = sharpness.getItemMeta();
        String add=String.valueOf(playerTeam.upgrades.protection+1);
        if (playerTeam.upgrades.protection==4){
            add="";
        }
        Component protectionTitle = Component.text("Reinforced Armor "+add).color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false);
        //so the tiers u already have are like stikedthru
        boolean strike1=false;
        boolean strike2=false;
        boolean strike3=false;
        boolean strike4=false;
        int requiredDiamonds=5;
        if (playerTeam.upgrades.protection>=4){
            strike4=true;
            requiredDiamonds=0;
        }
        if (playerTeam.upgrades.protection>=3){
            strike3=true;
            requiredDiamonds=30;
        }
        if (playerTeam.upgrades.protection>=2){
            strike2=true;
            requiredDiamonds=20;
        }
        if (playerTeam.upgrades.protection>=1){
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
        } else if (playerTeam.upgrades.sharpness==1){
            sharpLore.add(Component.text("").color(TextColor.color(170, 170, 170)).decoration(TextDecoration.ITALIC, false));
            sharpLore.add(Component.text("Your team already owns this upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
        }
        sharpnessMeta.lore(sharpLore);
        sharpnessMeta.displayName(sharpTitle);
        sharpness.setItemMeta(sharpnessMeta);
        gui.setItem(11, sharpness);



        //fill up empty slots
        for (int index=0; index<9*5; index++){
            if (gui.getItem(index)==null){
                gui.setItem(index, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
            }
        }

        player.openInventory(gui);
    }
    public void onClick(int slot, Player player, Inventory shop){

        if (shop.getItem(slot).getType()!=Material.BLACK_STAINED_GLASS_PANE && shop.getItem(slot).getType()!=Material.LIGHT_GRAY_STAINED_GLASS_PANE && slot<36){
            Team playerTeam = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId());
            if (shop.getItem(slot).getType()==Material.IRON_SWORD){

                if (playerTeam.upgrades.sharpness==1){
                    player.sendMessage(Component.text("You already own that team upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                }
                if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=8){
                    PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, 8);
                    playerTeam.upgrades.sharpness=1;
                    for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                        teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Sharpened Swords for the whole team").color(TextColor.color(85, 255, 85))));
                    }
                    openGUIForPlayer(player);
                } else {
                    player.sendMessage(Component.text("You don't have enough Diamonds!").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                }
            } else if (shop.getItem(slot).getType()==Material.IRON_CHESTPLATE){


                if (playerTeam.upgrades.sharpness>=4){
                    player.sendMessage(Component.text("You already own that team upgrade").color(TextColor.color(255, 85, 85)).decoration(TextDecoration.ITALIC, false));
                    openGUIForPlayer(player);
                    return;
                } else {
                    int reqDiamonds=0;
                    if (playerTeam.upgrades.protection==0){
                        reqDiamonds=5;
                    } else if (playerTeam.upgrades.protection==1){
                        reqDiamonds=10;
                    } else if (playerTeam.upgrades.protection==2){
                        reqDiamonds=20;
                    } else if (playerTeam.upgrades.protection==3){
                        reqDiamonds=30;
                    }
                    if (PlayerInventoryUtility.getInstance().getAmountOfMaterial(player, Material.DIAMOND)>=reqDiamonds){
                        PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, Material.DIAMOND, reqDiamonds);
                        playerTeam.upgrades.sharpness+=1;
                        for ( Player teamPlayer : playerTeam.getAlivePlayers()){
                            teamPlayer.sendMessage(Component.text(player.getName()).color(TextColor.color(255, 255, 85)).append(Component.text(" bought Reinforced Armor "+ String.valueOf(playerTeam.upgrades.protection)+" for the whole team").color(TextColor.color(85, 255, 85))));
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
