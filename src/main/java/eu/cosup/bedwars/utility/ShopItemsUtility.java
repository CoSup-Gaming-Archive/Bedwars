package eu.cosup.bedwars.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class ShopItemsUtility {
    private ItemStack item;
    private Material priceItem;
    private int price;
    private boolean respectTeamColor;
    public String tab;
    private int slot;
    public ShopItemsUtility(ItemStack item, String priceItemName, Integer price, boolean respectTeamColor, String tab, int slot){
        this.item=item;
        priceItem=Material.getMaterial(priceItemName);

        if (priceItem==null){
            priceItem=Material.BARRIER;
        }

        this.price=price;
        this.tab=tab;
        this.slot=slot;
        this.respectTeamColor=respectTeamColor;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPriceItem(Material priceItem) {
        this.priceItem = priceItem;
    }

    public int getSlot() {
        return slot;
    }
    public boolean isRespectTeamColor(){
        return respectTeamColor;
    }

    public Material getPriceItem() {
        return priceItem;
    }

    public ItemStack getItem() {
        return item;
    }
    public void print(){
        Bukkit.getLogger().info("Item:"+item.getType().name()+" Costs: "+String.valueOf(price)+"x "+priceItem.name());
    }
}
