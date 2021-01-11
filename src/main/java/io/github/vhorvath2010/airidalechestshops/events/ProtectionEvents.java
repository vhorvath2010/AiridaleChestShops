package io.github.vhorvath2010.airidalechestshops.events;

import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.ChestShop;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.UUID;

public class ProtectionEvents implements Listener {

    @EventHandler
    public void breakChest(BlockBreakEvent e) {
        Block broken = e.getBlock();
        if (!e.isCancelled()) {
            // Check all sign shops for consequences
            ArrayList<ChestShop> toRemove = new ArrayList<>();
            for (UUID shopPlayerID : AiridaleChestShops.getPlugin().getChestShopManager().getIDS()) {
                // Stop break if not owner
                if (!e.getPlayer().getUniqueId().equals(shopPlayerID)) {
                    for (ChestShop shop : AiridaleChestShops.getPlugin().getChestShopManager().getShops(shopPlayerID)) {
                        if (shop.getContainer().equals(broken) || shop.getSign().getBlock().equals(broken)) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                } else {
                    // Otherwise, unregister shop
                    for (ChestShop shop : AiridaleChestShops.getPlugin().getChestShopManager().getShops(shopPlayerID)) {
                        if (shop.getContainer().equals(broken) || shop.getSign().getBlock().equals(broken)) {
                            toRemove.add(shop);
                        }
                    }
                }
            }
            // Unregister all broken shops
            for (ChestShop shop : toRemove) {
                AiridaleChestShops.getPlugin().getChestShopManager().removeShop(shop);
            }
        }
    }

}
