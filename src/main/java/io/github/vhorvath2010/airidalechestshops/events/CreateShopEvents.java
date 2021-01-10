package io.github.vhorvath2010.airidalechestshops.events;

import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class CreateShopEvents implements Listener {

    @EventHandler
    public void onCreate(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getState() instanceof Sign) {
            Sign sign = (Sign) e.getBlockPlaced().getState();
            // Check if placing on chest
            if (e.getBlockAgainst().getState() instanceof Chest) {
                Chest chest = (Chest) e.getBlockAgainst().getState();
                // Try to create shops
                String item = sign.getLine(2);
                boolean isEnchanted = item.contains("[E] ");
                item = item.replace("[E] ", "");
                if (isNumeric(sign.getLine(1)) && isNumeric(sign.getLine(3)) &&
                        (InventoryUtils.getMaterial(item) != null || InventoryUtils.getCUI(item) != null)) {
                    ChestShopManager chestShopManager = AiridaleChestShops.getPlugin().getChestShopManager();
                    int amt = (int) Double.parseDouble(sign.getLine(1));
                    double value = Double.parseDouble(sign.getLine(3));
                    // Stop if placing on someone else's chest
                    for (UUID ownerID : chestShopManager.getIDS()) {
                        if (!ownerID.equals(e.getPlayer().getUniqueId())) {
                            for (ChestShop shop : chestShopManager.getShops(ownerID)) {
                                if (shop.getChest().equals(chest)) {
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                        }
                    }
                    // Try to create buy shop
                    if (sign.getLine(0).equalsIgnoreCase("(buy)")) {
                        BuyChestShop shop = new BuyChestShop(sign, chest, item, amt, value, e.getPlayer().getUniqueId(), isEnchanted);
                        chestShopManager.registerShop(shop);
                        e.getPlayer().sendMessage(ChatColor.GREEN + "Chest shop created!");
                    } else if (sign.getLine(0).equalsIgnoreCase("(sell)")) {
                        SellChestShop shop = new SellChestShop(sign, chest, item, amt, value, e.getPlayer().getUniqueId(), isEnchanted);
                        chestShopManager.registerShop(shop);
                        e.getPlayer().sendMessage(ChatColor.GREEN + "Chest shop created!");
                    }
                }
            }
        }
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
