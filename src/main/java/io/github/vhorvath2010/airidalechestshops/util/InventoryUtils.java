package io.github.vhorvath2010.airidalechestshops.util;

import com.jojodmo.itembridge.ItemBridge;
import com.jojodmo.itembridge.ItemBridgeKey;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryUtils {

    // This method will return a material based on its alias
    public static Material getMaterial(String type) {
        FileConfiguration config = AiridaleChestShops.getPlugin().getConfig();
        for (String materialString : config.getConfigurationSection("aliases").getKeys(false)) {
            if (config.contains("aliases." + materialString + "." + type.toLowerCase())) {
                return Material.valueOf(materialString.toUpperCase());
            }
        }
        if (Arrays.asList(Material.values()).contains(type.toUpperCase())) {
            return Material.valueOf(type.toUpperCase());
        }
        return null;
    }

    // This method will return a custom items name based on its alias
    public static String getCUI(String type) {
        FileConfiguration config = AiridaleChestShops.getPlugin().getConfig();
        for (String itemString : config.getConfigurationSection("aliases").getKeys(false)) {
            if (config.contains("aliases." + itemString + "." + type.toLowerCase())) {
                return itemString;
            }
        }
        return type;
    }

    // This method counts the number of "target" items in "inventory"
    public static int countItems(Inventory inventory, String target, boolean enchanted) {
        int count = 0;
        // Count the valid items
        ItemStack[] items = getValidItems(inventory, target, enchanted);
        for (ItemStack item : items) {
            count += item.getAmount();
        }
        return count;
    }

    // This method returns the valid items in an inventory
    public static ItemStack[] getValidItems(Inventory inventory, String target, boolean enchanted) {
        // Create list of items to check in
        ItemStack[] items = inventory.getStorageContents();
        List<ItemStack> validItems = new ArrayList<ItemStack>();

        // Count for custom items
        String customItem = getCUI(target);
        if (ItemBridge.getItemStack(customItem) != null) {
            for (ItemStack item : items) {
                if (item != null) {
                    ItemBridgeKey itemKey = ItemBridge.getItemKey(item);
                    if (itemKey.getItem() != null && itemKey.getItem().equalsIgnoreCase(customItem)) {
                        validItems.add(item);
                    }
                }
            }
        }

        // Otherwise count for regular item
        else {
            Material search = getMaterial(target);
            if (search != null) {
                for (ItemStack item : items) {
                    if (item != null) {
                        if (item.getType() == search) {
                            if (item.getEnchantments().size() == 0) {
                                if (!enchanted) {
                                    validItems.add(item);
                                }
                            } else {
                                if(enchanted) {
                                    validItems.add(item);
                                }
                            }
                        }
                    }
                }
            }
        }
        ItemStack[] returnItems = new ItemStack[validItems.size()];
        for (int i = 0; i < returnItems.length; ++i) {
            returnItems[i] = validItems.get(i);
        }
        return returnItems;
    }

    // This method transfer a certain number of target items between inventories.
    // It will return true if the transfer was successful, otherwise false
    public static boolean transferItems(Inventory from, Inventory to, String target, boolean enchanted, int amt) {
        // Stop if not enough items
        if (countItems(from, target, enchanted) < amt) {
            return false;
        }
        // Perform removal
        ItemStack[] validItems = getValidItems(from, target, enchanted);
        for (ItemStack item : validItems) {
            // Stop and change amount if we found enough
            ItemStack toItem = item.clone();
            if (item.getAmount() > amt) {
                item.setAmount(item.getAmount() - amt);
                toItem.setAmount(amt);
                to.addItem(toItem);
                break;
            }
            // Otherwise fully transfer item
            else {
                amt -= item.getAmount();
                to.addItem(item.clone());
                item.setAmount(0);
            }

        }
        return true;
    }

    // This method will check for an empty item in an ItemStack array
    public static boolean hasEmpty(ItemStack[] items) {
        for (ItemStack item : items) {
            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }

}
