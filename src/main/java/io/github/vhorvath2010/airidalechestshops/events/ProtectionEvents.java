package io.github.vhorvath2010.airidalechestshops.events;

import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.ChestShop;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.UUID;

public class ProtectionEvents implements Listener {

    @EventHandler
    public void breakChest(BlockBreakEvent e) {
        Block broken = e.getBlock();
        if (!e.isCancelled() && (broken.getState() instanceof Chest || broken.getState() instanceof Barrel || broken.getState() instanceof Sign)) {
            // Check all sign shops for consequences
            ArrayList<ChestShop> toRemove = new ArrayList<>();
            for (UUID shopPlayerID : AiridaleChestShops.getPlugin().getChestShopManager().getIDS()) {
                // unregister broken shop
                for (ChestShop shop : AiridaleChestShops.getPlugin().getChestShopManager().getShops(shopPlayerID)) {
                    if (shop != null && shop.isValid() && (shop.getContainer().equals(broken) || shop.getSign().getBlock().equals(broken))) {
                        toRemove.add(shop);
                    }
                }
            }
            // Unregister all broken shops
            for (ChestShop shop : toRemove) {
                AiridaleChestShops.getPlugin().getChestShopManager().removeShop(shop);
            }
        }
    }

    @EventHandler
    public void breakChest(BlockExplodeEvent e) {
        Block broken = e.getBlock();
        if (!e.isCancelled() && (broken.getState() instanceof Chest || broken.getState() instanceof Barrel || broken.getState() instanceof Sign)) {
            // Check all sign shops for consequences
            ArrayList<ChestShop> toRemove = new ArrayList<>();
            for (UUID shopPlayerID : AiridaleChestShops.getPlugin().getChestShopManager().getIDS()) {
                // Stop explosions
                for (ChestShop shop : AiridaleChestShops.getPlugin().getChestShopManager().getShops(shopPlayerID)) {
                    if (shop != null && shop.isValid() && (shop.getContainer().equals(broken) || shop.getSign().getBlock().equals(broken))) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void openShop(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clicked = e.getClickedBlock();
            if (clicked == null) {
                return;
            }
            if ((clicked.getState() instanceof Chest || clicked.getState() instanceof Barrel)) {
                for (UUID shopPlayerID : AiridaleChestShops.getPlugin().getChestShopManager().getIDS()) {
                    // Stop open if not owner
                    if (!e.getPlayer().getUniqueId().equals(shopPlayerID) && !e.getPlayer().hasPermission("shop.admin")) {
                        for (ChestShop shop : AiridaleChestShops.getPlugin().getChestShopManager().getShops(shopPlayerID)) {
                            if (shop != null && shop.isValid() && shop.getContainer().equals(clicked)) {
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

}
