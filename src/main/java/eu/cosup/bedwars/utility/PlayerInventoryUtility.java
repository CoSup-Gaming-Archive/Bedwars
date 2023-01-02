package eu.cosup.bedwars.utility;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInventoryUtility {
    private static PlayerInventoryUtility instance;

    public PlayerInventoryUtility(){
        instance=this;
    }
    public static PlayerInventoryUtility getInstance(){
        return instance;
    }
    public ItemStack getItemWithEmptyName(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.lore(null);
        itemMeta.displayName(Component.text(" "));
        item.setItemMeta(itemMeta);
        return item;
    }
    public int getAmountOfMaterial(Player player, Material material){
        Inventory inventory= player.getInventory();
        int total=0;
        for (ItemStack item: inventory.getContents()){
            if (item==null){
                continue;
            }
            if (item.getType()==material){
                total+= item.getAmount();
            }
        }
        return total;
    }
    public void removeMaterialFromPlayerInventory(Player player, Material material, int amount){
        Inventory inventory = player.getInventory();
        for (ItemStack item: inventory.getContents()){
            if (item==null){
                continue;
            } else if (item.getType()==material){
                int stackSize=item.getAmount();
                if (amount!=0){
                    if (stackSize>amount){
                        stackSize-=amount;
                        amount=0;
                    } else if (stackSize==amount){
                        stackSize=0;
                        amount=0;
                    } else {
                        amount-=stackSize;
                        stackSize=0;
                    }
                    item.setAmount(stackSize);

                }
            }
        }
    }
    public boolean takeMaterialFromPlayer(Player player, Material material, int amount){
        Inventory inventory=player.getInventory();
        if (getAmountOfMaterial(player, material)>=amount){
            removeMaterialFromPlayerInventory(player, material, amount);
            return true;
        } else {
            return false;
        }
    }
}
