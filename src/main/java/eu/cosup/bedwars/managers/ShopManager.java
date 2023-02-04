package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.tasks.ActivateGameTask;
import eu.cosup.bedwars.utility.PlayerInventoryUtility;
import eu.cosup.bedwars.utility.ShopItemsUtility;
import eu.cosup.bedwars.utility.ShopTabUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

/*
Format in Config:
shop:
  <tab name>:
    <name (doesn't have to be itemname>:
        item: <ItemStack>
        priceItem: <Material>
        price: <int>
 */
public class ShopManager {
    public HashMap<String, ShopTabUtility> items=new HashMap<>();
    public YamlConfiguration shopConfig;
    public Component title;
    private HashMap<String, Integer> playerSwordUpgrades = new HashMap<>();
    private HashMap<String, Integer> playerArmorUpgrade = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> playerTools = new HashMap<>();

    public int offset=0;
    private PlayerInventoryUtility playerInventoryUtility = new PlayerInventoryUtility();
    public ShopManager(){

    }

    public HashMap<String, Integer> getPlayerArmorUpgrade() {
        return playerArmorUpgrade;
    }

    public HashMap<String, Integer> getPlayerSwordUpgrades() {
        return playerSwordUpgrades;
    }

    public HashMap<String, HashMap<String, Integer>> getPlayerTools() {
        return playerTools;
    }

    public void loadConfig(){
        //load the configs from shop.yml
        File shopConfigFile = new File(Bedwars.getInstance().getDataFolder(), "shop.yml");
        if (!shopConfigFile.exists()){
            Bukkit.getLogger().severe("We were not able to find a shop configuration file");
            return;
        }
        YamlConfiguration shopConfig=YamlConfiguration.loadConfiguration(shopConfigFile);
        this.shopConfig=shopConfig;
        title=Component.text("Shop");

        //load items from config
        ConfigurationSection shop = getShopConfig().getConfigurationSection("shop");
        List<String> tabs= this.getShopConfig().getStringList("tabList");
        String f="{";
        for (String tab: tabs){
            ConfigurationSection thisTab = shop.getConfigurationSection(tab);
            getItems().put(tab, new ShopTabUtility(this.readItemStackFromConfig("shop."+tab+".icon", shopConfig), thisTab.getConfigurationSection("items"), tab));
            f+="\""+tab+"\":"+getItems().get(tab).getStringBuilt()+", ";
        }
        f+="}";
        //Bukkit.getLogger().warning(f);
    }
    public HashMap<String, ShopTabUtility> getItems(){
        return items;
    }
    public void openShopForPlayer(Player player, @Nullable String currentTab){
        if (currentTab==null){
            currentTab=getShopConfig().getStringList("tabList").toArray()[0].toString();
        }

        Inventory shop=Bukkit.createInventory(null, 6*9, title);
        int index=0;
        //little script to have the tabs centered
        Double beingAtD=Math.floor((items.size()/2));
        int beginAt=4-beingAtD.intValue();
        offset=beginAt;
        for (String tab: this.getShopConfig().getStringList("tabList")){
            //show tabs at the top
            ItemStack icon = items.get(tab).icon.clone();
            if (tab==currentTab){
                ItemMeta itemMeta = icon.getItemMeta();
                if (itemMeta==null){
                    itemMeta=Bukkit.getItemFactory().getItemMeta(icon.getType());
                }
                itemMeta.addEnchant(Enchantment.DURABILITY, 0, false);
                icon.setItemMeta(itemMeta);
                shop.setItem(index+beginAt+9, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.LIME_STAINED_GLASS_PANE)));
            } else {
                shop.setItem(index+beginAt+9, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE)));
            }

            shop.setItem(index+beginAt, icon);

            index++;
        }

        //show items in menu
        index=0;
        for (ShopItemsUtility item: items.get(currentTab).items){
            ItemStack itemPreview=item.getItem().clone();
            ItemMeta itemMeta= itemPreview.getItemMeta();
            if (item.isRespectTeamColor()){
                String modifyItemType = item.getItem().getType().name();
                modifyItemType= modifyItemType.replaceFirst("WHITE", getTeamColor(player));
                itemPreview.setType(Material.getMaterial(modifyItemType));
            }
            if (itemMeta.hasLore()){
                List<Component> lore = itemMeta.lore();
                lore.add(Component.text("   "));
                lore.add(Component.text("Cost: ").color(TextColor.color(255, 170, 0)).decoration(TextDecoration.ITALIC, false).append(getPriceItemColor(item.getPriceItem(), item.getPrice())));
                itemMeta.lore(lore);
            } else {
                List<Component> lore = List.of(Component.text("   "), Component.text("Cost: ").color(TextColor.color(255, 170, 0)).decoration(TextDecoration.ITALIC, false).append(getPriceItemColor(item.getPriceItem(), item.getPrice())));
                itemMeta.lore(lore);
            }
            itemPreview.setItemMeta(itemMeta);
            shop.setItem(item.getSlot(), itemPreview);

            index++;
        }

        for (int i=0; i<6*9; i++){
            if (shop.getItem(i)==null){
                if ((i>0 && i<8)|| (i>27 && i< 35)|| (i>36 && i<44)){
                    shop.setItem(i, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
                } else if ((i>9 && i<17)){
                    shop.setItem(i, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE)));
                } else {
                    shop.setItem(i, PlayerInventoryUtility.getInstance().getItemWithEmptyName(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
                }
            }
        }
        player.openInventory(shop);
    }
    public Component getPriceItemColor(Material material, int amount){
        //just so when u need emeralds the Cost: 1x Emerald is in green an stuff
        if (material==Material.EMERALD){
            return Component.text(String.valueOf(amount)+ "x Emerald").color(TextColor.color(0, 170, 0));
        } else if (material==Material.GOLD_INGOT){
            return Component.text(String.valueOf(amount)+ "x Gold Ingot").color(TextColor.color(255, 170, 0));
        } else if (material==Material.IRON_INGOT){
            return Component.text(String.valueOf(amount)+ "x Iron Ingot").color(TextColor.color(255, 255, 255));
        } else {
            return Component.text(String.valueOf(amount)+ "x "+material.name()).color(TextColor.color(255, 255, 255));
        }
    }
    public String getTeamColor(Player player){
        Team team= Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId());
        if (team==null){
            return "GRAY";
        } else {
            return team.getColor().toString();
        }
    }
    public void interactWithShop(int slot, Player player, Inventory shop){
        if (shop==null){
            return;
        }
        if (slot<8) {
            String openTab = getClickedTab(slot);
            if (openTab == null) {
                openShopForPlayer(player, getOpenedTab(shop));
            }
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 2F);
            openShopForPlayer(player, openTab);
        } else if (slot<17&&slot>9) {
            String openTab = getClickedTab(slot-9);
            if (openTab == null) {
                openShopForPlayer(player, getOpenedTab(shop));
            }
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 2F);
            openShopForPlayer(player, openTab);

        } else if ((slot>27 && slot<35) || (slot>36 && slot<44)){
          assert shop.getItem(slot) != null;
          if (shop.getItem(slot).getType()==Material.GRAY_STAINED_GLASS_PANE){
              openShopForPlayer(player, getOpenedTab(shop));
          } else {
              ShopItemsUtility itemsUtility=getClickedItem(slot, shop);
              if (PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, itemsUtility.getPriceItem(), itemsUtility.getPrice())){
                  ItemStack boughtItem=itemsUtility.getItem().clone();
                  if (itemsUtility.isRespectTeamColor()){
                      String name = boughtItem.getType().name();
                      name= name.replaceFirst("WHITE", Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor().toString().toUpperCase());
                      boughtItem.setType(Material.getMaterial(name));
                  }

                  HashMap<Integer, ItemStack> itemThatDidntFit = new HashMap<>();

                  Game.getGameInstance().getShopManager().getPlayerTools().computeIfAbsent(player.getName(), k -> new HashMap());
                  Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().putIfAbsent(player.getName(), 0);
                  Game.getGameInstance().getShopManager().getPlayerSwordUpgrades().putIfAbsent(player.getName(), 0);



                  if (boughtItem.getType().toString().contains("BOOTS")) {
                      switch (boughtItem.getType()) {
                          case DIAMOND_BOOTS -> {
                              if (Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().get(player.getName()) < 3) {
                                  Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().put(player.getName(), 3);
                              }
                          }
                          case IRON_BOOTS -> {
                              if (Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().get(player.getName()) < 2) {
                                  Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().put(player.getName(), 2);
                              }
                          }
                          case CHAINMAIL_BOOTS -> {
                              if (Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().get(player.getName()) < 1) {
                                  Game.getGameInstance().getShopManager().getPlayerArmorUpgrade().put(player.getName(), 1);
                              }
                          }
                      }


                      ActivateGameTask.givePlayerArmor(player, Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getUpgrades().getProtection(), getPlayerArmorUpgrade().get(player.getName()));

                  } else if (boughtItem.getType().toString().contains("SHEARS") || boughtItem.getType().toString().contains("AXE")) {
                      switch (boughtItem.getType()) {
                          case WOODEN_AXE, WOODEN_PICKAXE -> {
                              Game.getGameInstance().getShopManager().getPlayerTools().putIfAbsent(player.getName(), new HashMap<>());
                              if (Game.getGameInstance().getShopManager().getPlayerTools().get(player.getName()).get(boughtItem.getType().toString()) < 1) {
                                  Game.getGameInstance().getShopManager().getPlayerTools().get(player.getName()).put(boughtItem.getType().toString().replace("WOODEN_", ""),
                                          Game.getGameInstance().getShopManager().getPlayerTools().get(player.getName()).get(boughtItem.getType().toString().replace("WOODEN_", "") + 1));

                              }
                          }
                          case SHEARS -> Game.getGameInstance().getShopManager().getPlayerTools().get(player.getName()).put(boughtItem.getType().toString(), 0);
                      }
                      ActivateGameTask.givePlayerTools(player, Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getUpgrades().getSharpness(), Game.getGameInstance().getShopManager().getPlayerTools().get(player.getName()));
                  } else {
                      // its just an item
                      itemThatDidntFit = player.getInventory().addItem(boughtItem);
                  }

                  for (ItemStack itemToDrop :itemThatDidntFit.values()){
                      player.getWorld().dropItemNaturally(player.getLocation(), itemToDrop);
                  }
                  player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 200f, 2f);
              } else {
                  player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 0.9f);
                  player.sendMessage(Component.text("You can't afford this item").color(TextColor.color(255, 85, 85)));
              }
              openShopForPlayer(player, getOpenedTab(shop));
          }
        } else {
            openShopForPlayer(player, getOpenedTab(shop));
        }
    }
    public ShopItemsUtility getClickedItem(int slot, Inventory shop){
        String tab=getOpenedTab(shop);
        for (ShopItemsUtility shopItemsUtility:items.get(tab).items){
            if (shopItemsUtility.getSlot()==slot){
                return shopItemsUtility;
            }
        }
        return null;
    }
    public String getClickedTab(int clickedSlot){
        if (!(clickedSlot>0 && clickedSlot<8)){
            return null;
        }
        String index;
        try {
            index=getShopConfig().getStringList("tabList").toArray()[clickedSlot-offset].toString();
        } catch (Exception e){
            index=null;
        }
        return index;
    }

    public YamlConfiguration getShopConfig() {
        return shopConfig;
    }
    public String getOpenedTab(Inventory shop){
        for (int i = 0; i<7; i++){
            ItemStack item = shop.getItem(9+offset+i);
            if (item.getType()==Material.LIME_STAINED_GLASS_PANE){
                return getShopConfig().getStringList("tabList").toArray()[i].toString();
            }
        }
        return null;
    }
    public ItemStack readItemStackFromConfig(String path, YamlConfiguration config){

        //This is kinda useless because i could just use config.getItemStack but
        // 1. I couldn't find much about it on the internet (Syntax wise)
        // 2. I got an error and i rly dont know how to fix it because point 1
        //I am planning to rewrite the whole config reader because i can

        ConfigurationSection section=null;
        try {
            section = config.getConfigurationSection(path);
        } catch (Exception e){
            Bukkit.getLogger().info(e.toString());
            return null;
        }
        Material material=null;
        int amount=0;
        int damage=0;
        String displayName=null;
        List<String> lore=null;
        ConfigurationSection enchants=null;
        ConfigurationSection potions=null;
        ConfigurationSection potionColor=null;

        try {
            material = Material.getMaterial(section.getString("type"));
        }catch (Exception e){
            material = Material.BARRIER;
        }
        amount=section.getInt("amount");
        damage=section.getInt("damage");
        ConfigurationSection meta=section.getConfigurationSection("meta");
        if (meta != null) {
            displayName=meta.getString("display-name");
            lore = meta.getStringList("lore");
            enchants=meta.getConfigurationSection("enchants");
            potions=meta.getConfigurationSection("potions");
            potionColor=meta.getConfigurationSection("potioncolor");
        }


        ItemStack item = new ItemStack(material);

        if (damage!=0){
            try {
                Damageable itemMeta = (Damageable) item.getItemMeta();
                itemMeta.setDamage(damage);
                item.setItemMeta(itemMeta);
            } catch (Exception e){
                Bukkit.getLogger().warning("Exception at: "+path+" (damage)");
                Bukkit.getLogger().warning(e.toString());
            }
        }
        item.setAmount(amount);
        ItemMeta itemMeta =item.getItemMeta();
        if (displayName!=null){
            itemMeta.displayName(Component.text(displayName));
        }
        if (lore.size()>0){
            List<Component> itemLore=List.of(Component.text(lore.get(0)));
            lore.remove(0);
            for (String loreLine:lore){
                itemLore.add(Component.text(loreLine));
            }
            itemMeta.lore(itemLore);
        }
        if (enchants!=null){
            Set<String> enchantNames=enchants.getKeys(false);
            for (String name:enchantNames){
                try {
                    itemMeta.addEnchant(Enchantment.getByKey(new NamespacedKey("minecraft", name.toLowerCase())), enchants.getInt(name), false);
                } catch (Exception e){
                    Bukkit.getLogger().warning("Exception at: "+path+" (enchants: "+enchantNames+")");
                    Bukkit.getLogger().warning(e.toString());
                }
            }
        }
        item.setItemMeta(itemMeta);
        if (potions!=null){
            Set<String> potionNames=potions.getKeys(false);
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            for (String name:potionNames){
                PotionEffectType type=null;
                int duration=0;
                int amplifier=0;
                boolean icon=true;
                boolean ambient=true;
                boolean particles=true;
                boolean overwrite=true;

                try {
                    type=PotionEffectType.getByKey(new NamespacedKey("minecraft", name.toLowerCase()));
                } catch (Exception e){
                    type=PotionEffectType.SATURATION;
                    Bukkit.getLogger().warning("Exception at: "+path+" (potions: "+name+")");
                    Bukkit.getLogger().warning(e.toString());
                }
                duration=potions.getInt(name+".duration");
                amplifier=potions.getInt(name+".amplifier");
                icon= potions.getBoolean(name+".icon", true);
                ambient= potions.getBoolean(name+".ambient", true);
                particles= potions.getBoolean(name+".particles", true);
                overwrite= potions.getBoolean(name+".particles", true);
                try {
                    potionMeta.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient, particles, icon), overwrite);
                    //itemMeta.addEnchant(Enchantment.getByKey(new NamespacedKey("minecraft", name.toLowerCase())), enchants.getInt(name), false);
                } catch (Exception e){
                    Bukkit.getLogger().warning("Exception at: "+path+" (potions: "+name+")");
                    Bukkit.getLogger().warning(e.toString());
                }
            }
            item.setItemMeta(potionMeta);
        }
        if (potionColor!=null){
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(Color.fromRGB(potionColor.getInt("r"), potionColor.getInt("g"), potionColor.getInt("b")));
            item.setItemMeta(potionMeta);
        }

        return item;

    }
}
