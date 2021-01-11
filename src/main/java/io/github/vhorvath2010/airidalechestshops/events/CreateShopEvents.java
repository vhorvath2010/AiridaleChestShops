package io.github.vhorvath2010.airidalechestshops.events;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.jojodmo.itembridge.ItemBridge;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CreateShopEvents implements Listener {

    @EventHandler
    public void onCreate(SignChangeEvent e) {
        Block signBlock = e.getBlock();
        if (!e.isCancelled() && signBlock.getState() instanceof Sign) {
            // Check if all lines are present
            for (int i = 0; i < 4; i++) {
                if (e.getLine(i) == null) {
                    return;
                }
            }
            Sign sign = (Sign) signBlock.getState();
            // Check if placing on chest
            Block placedOn = signBlock.getRelative(0, -1, 0);
            if (sign.getBlockData() instanceof Directional) {
                 placedOn = sign.getBlock().getRelative(((Directional) sign.getBlockData()).getFacing().getOppositeFace());
            }
            if (placedOn.getState() instanceof Chest && e.getLine(2) != null) {
                Chest chest = (Chest) placedOn.getState();
                // Try to create shops
                String item = e.getLine(2);
                boolean isEnchanted = item.contains("E ");
                item = item.replace("E ", "");
                if (isNumeric(e.getLine(1)) && isNumeric(e.getLine(3)) &&
                        (InventoryUtils.getMaterial(item) != null || InventoryUtils.getCUI(item) != null)) {
                    ChestShopManager chestShopManager = AiridaleChestShops.getPlugin().getChestShopManager();
                    int amt = (int) Double.parseDouble(e.getLine(1));
                    double value = Double.parseDouble(e.getLine(3));
                    // Stop if placing on someone else's chest
                    UUID placerID = e.getPlayer().getUniqueId();
                    for (UUID ownerID : chestShopManager.getIDS()) {
                        if (!ownerID.equals(placerID)) {
                            for (ChestShop shop : chestShopManager.getShops(ownerID)) {
                                if (shop.getChest().equals(chest)) {
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                        }
                    }
                    // Stop if stack size is too big
                    if (InventoryUtils.getMaterial(item) != null) {
                        if (amt > InventoryUtils.getMaterial(item).getMaxStackSize()) {
                            e.getPlayer().sendMessage(ChatColor.RED + "That sale size is above the item's stack size!");
                            return;
                        }
                    } else {
                        if (amt > ItemBridge.getItemStack(InventoryUtils.getCUI(item)).getMaxStackSize()) {
                            e.getPlayer().sendMessage(ChatColor.RED + "That sale size is above the item's stack size!");
                            return;
                        }
                    }
                    // Try to create buy shop
                    if (e.getLine(0).equalsIgnoreCase("(buy)")) {
                        BuyChestShop shop = new BuyChestShop(sign.getBlock(), chest.getBlock(), item, amt, value, placerID, isEnchanted);
                        chestShopManager.registerShop(shop);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    shop.updateSign(Economy.getMoneyExact(placerID));
                                } catch (UserDoesNotExistException userDoesNotExistException) {
                                    userDoesNotExistException.printStackTrace();
                                }
                            }
                        }.runTaskLater(AiridaleChestShops.getPlugin(), 10L);
                        e.getPlayer().sendMessage(ChatColor.GREEN + "Chest shop created!");
                    } else if (e.getLine(0).equalsIgnoreCase("(sell)")) {
                        SellChestShop shop = new SellChestShop(sign.getBlock(), chest.getBlock(), item, amt, value, placerID, isEnchanted);
                        chestShopManager.registerShop(shop);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                try {
                                    shop.updateSign(Economy.getMoneyExact(placerID));
                                } catch (UserDoesNotExistException userDoesNotExistException) {
                                    userDoesNotExistException.printStackTrace();
                                }
                            }
                        }.runTaskLater(AiridaleChestShops.getPlugin(), 10L);
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
