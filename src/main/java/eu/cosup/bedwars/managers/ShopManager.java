package eu.cosup.bedwars.managers;

import eu.cosup.bedwars.Bedwars;
import eu.cosup.bedwars.Game;
import eu.cosup.bedwars.objects.Team;
import eu.cosup.bedwars.objects.TeamColor;
import eu.cosup.bedwars.utility.PlayerInventoryUtility;
import eu.cosup.bedwars.utility.ShopItemsUtility;
import eu.cosup.bedwars.utility.ShopTabUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Bed;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.units.qual.N;
import org.w3c.dom.Text;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    private static ShopManager instance;
    public YamlConfiguration shopConfig;
    public Component title;
    public int offset=0;
    private PlayerInventoryUtility playerInventoryUtility = new PlayerInventoryUtility();
    public ShopManager(){
        instance=this;
        loadConfig();
    }
    public void loadConfig(){
        //load the configs from shop.yml
        File shopConfigFile = new File(Bedwars.getInstance().getDataFolder(), "shop.yml");
        if (shopConfigFile==null){
            Bukkit.getLogger().severe("We were not able to find a shop configuration file");
            return;
        }
        ItemStack item = new ItemStack(Material.TERRACOTTA);
        Bukkit.getLogger().severe(item.getType().name());
        YamlConfiguration shopConfig=YamlConfiguration.loadConfiguration(shopConfigFile);
        this.shopConfig=shopConfig;
        title=Component.text("Shop");

        //load items from config
        ConfigurationSection shop = getShopConfig().getConfigurationSection("shop");
        List<String> tabs= ShopManager.getInstance().getShopConfig().getStringList("tabList");
        String f="{";
        for (String tab: tabs){
            ConfigurationSection thisTab = shop.getConfigurationSection(tab);
            getItems().put(tab, new ShopTabUtility(ShopManager.getInstance().readItemStackFromConfig("shop."+tab+".icon", shopConfig), thisTab.getConfigurationSection("items"), tab));
            f+="\""+tab+"\":"+getItems().get(tab).getStringBuilt()+", ";
        }
        f+="}";
        //Bukkit.getLogger().warning(f);
    }
    public static ShopManager getInstance() {
        return instance;
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
        for (String tab: ShopManager.getInstance().getShopConfig().getStringList("tabList")){
            //show tabs at the top
            ItemStack icon = items.get(tab).icon.clone();
            if (tab==currentTab){
                ItemMeta itemMeta = icon.getItemMeta();
                if (itemMeta==null){
                    Bukkit.getLogger().warning(icon.getType().name());
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

            //show item price
            ItemMeta itemMeta= itemPreview.getItemMeta();
            //TODO REMPOe
            System.out.println(item.isRespectTeamColor());
            if (item.isRespectTeamColor()){
                String modifyItemType = item.getItem().getType().name();
                modifyItemType= modifyItemType.replaceFirst("WHITE", getTeamColor(player));
                System.out.println(modifyItemType);
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
            return Component.text(String.valueOf(amount)+ "x Thig bruh this is weird").color(TextColor.color(255, 255, 255));
        }
    }
    public String getTeamColor(Player player){
        Team team= Game.getGameInstance().getTeamManager().whichTeam(player);
        System.out.println(team);
        if (team==null){
            return "GRAY";
        } else {
        return team.getColor().toString();
        }
    }
    public void interactWithShop(int slot, Player player, Inventory shop){
        if (slot<8) {
            String openTab = getClickedTab(slot);
            if (openTab == null) {
                openShopForPlayer(player, getOpenedTab(shop));
            }
            openShopForPlayer(player, openTab);
        } else if (slot<17&&slot>9) {
            String openTab = getClickedTab(slot-9);
            if (openTab == null) {
                openShopForPlayer(player, getOpenedTab(shop));
            }
            openShopForPlayer(player, openTab);

        } else if ((slot>27 && slot<35) || (slot>36 && slot<44)){
          if (shop.getItem(slot).getType()==Material.GRAY_STAINED_GLASS_PANE){
              openShopForPlayer(player, getOpenedTab(shop));
          } else {
              ShopItemsUtility itemsUtility=getClickedItem(slot, shop);
              if (PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, itemsUtility.getPriceItem(), itemsUtility.getPrice())){
                  player.getInventory().addItem(itemsUtility.getItem());
              } else {
                  player.sendMessage(Component.text("You can't afford this item").color(TextColor.color(255, 85, 85)));
              }
              openShopForPlayer(player, getOpenedTab(shop));
              //Material priceItem=
              //if (PlayerInventoryUtility.getInstance().takeMaterialFromPlayer(player, ))
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
                    System.out.println(potionMeta.addCustomEffect(new PotionEffect(type, duration, amplifier, ambient, particles, icon), overwrite));
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
