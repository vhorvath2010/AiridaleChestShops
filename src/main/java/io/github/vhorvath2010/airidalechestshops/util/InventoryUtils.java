package io.github.vhorvath2010.airidalechestshops.util;

import com.jojodmo.itembridge.ItemBridge;
import com.jojodmo.itembridge.ItemBridgeKey;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.List;

public class InventoryUtils {

    // This method counts the number of "target" items in "inventory"
    public static int countItems(Inventory inventory, String target, boolean enchanted) {
        int count = 0;
        // Create list of items to check in
        List<ItemStack> items = Arrays.asList(inventory.getContents());
        if (inventory instanceof PlayerInventory) {
            for (ItemStack armor : ((PlayerInventory) inventory).getArmorContents()) {
                if (armor != null) {
                    items.remove(armor);
                }
            }
            if (((PlayerInventory) inventory).getItemInOffHand().getType() != Material.AIR) {
                items.remove(((PlayerInventory) inventory).getItemInOffHand());
            }
        }

        // Count for custom items
        String customItem = getCUI(target);
        if (ItemBridge.getItemStack(customItem) != null) {
            for (ItemStack item : items) {
                if (item != null) {
                    ItemBridgeKey itemKey = ItemBridge.getItemKey(item);
                    if (itemKey.getItem() != null && itemKey.getItem().equalsIgnoreCase(customItem)) {
                        count += item.getAmount();
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
                                    count += item.getAmount();
                                }
                            } else {
                                if(enchanted) {
                                    count += item.getAmount();
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    // This method will return a material based on its alias
    public static Material getMaterial(String type) {
        FileConfiguration config = AiridaleChestShops.getPlugin().getConfig();
        for (String materialString : config.getConfigurationSection("aliases").getKeys(false)) {
            if (config.contains("aliases." + materialString + "." + type.toLowerCase())) {
                return Material.valueOf(materialString);
            }
        }
        return Material.valueOf(type);
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
}
