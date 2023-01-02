package eu.cosup.bedwars.utility;

import eu.cosup.bedwars.managers.ShopManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class ShopTabUtility {
    public ItemStack icon;
    public ArrayList<ShopItemsUtility> items;
    public ShopTabUtility(ItemStack icon, ConfigurationSection items, String tabName){
        this.icon=icon;
        Set<String> itemsInTab=items.getKeys(false);
        this.items=new ArrayList<ShopItemsUtility>();
        int index=0;
        for (String item : itemsInTab){
            Double dx=Math.floor(index/7);
            int y = dx.intValue();
            int x = index%7;

            this.items.add(new ShopItemsUtility(ShopManager.getInstance().readItemStackFromConfig("shop."+tabName+".items."+item + ".item", ShopManager.getInstance().getShopConfig()), items.getString(item+".priceItem"), items.getInt(item+".price"), items.getBoolean(item+".respectTeamColor", false), tabName, (y+1)*9+x+19));
            index++;
        }
    }
    public String getStringBuilt(){
        String f="{";
        f+="\"icon\":\""+icon.getType().name()+"\"";
        f+=", \"items\":[";
        for (ShopItemsUtility item:items){
            f+="\""+item.getItem().getType().name()+"\", ";
        }
        f+="]}";
        return f;
    }

}
