package io.github.vhorvath2010.airidalechestshops.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    // This method counts the number of "target" items in "inventory"
    public static int countItems(Inventory inventory, ItemStack target) {
        int count = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                if (item.equals(target)) {
                    count++;
                }
            }
        }
        return count;
    }

}
