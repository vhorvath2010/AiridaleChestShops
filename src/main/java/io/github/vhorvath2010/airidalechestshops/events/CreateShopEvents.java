package io.github.vhorvath2010.airidalechestshops.events;

import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.UUID;

public class CreateShopEvents implements Listener {

    @EventHandler
    public void onCreate(SignChangeEvent e) {
        if (!e.isCancelled() && e.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getBlock().getState();
            // Check if placing on chest
            Block placedOn = sign.getBlock().getRelative(((Directional) sign.getBlockData()).getFacing().getOppositeFace());
            if (placedOn.getState() instanceof Chest) {
                Chest chest = (Chest) placedOn.getState();
                // Try to create shops
                String item = sign.getLine(2);
                e.getPlayer().sendMessage(item);
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
