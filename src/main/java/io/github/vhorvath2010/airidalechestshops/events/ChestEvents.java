package io.github.vhorvath2010.airidalechestshops.events;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.ChestShop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.UUID;

public class ChestEvents implements Listener {

    @EventHandler
    public void onChestChange(InventoryCloseEvent e) throws UserDoesNotExistException {
        Inventory shopInv = e.getPlayer().getOpenInventory().getTopInventory();
        UUID playerID = e.getPlayer().getUniqueId();
        // Check for shops with this inventory
        ArrayList<ChestShop> shops = AiridaleChestShops.getPlugin().getChestShopManager().getShops(playerID);
        if (shops != null) {
            for (ChestShop shop : shops) {
                e.getPlayer().sendMessage(shop.getChest().getInventory().equals(shopInv) + "");
                if (shop.getChest().getInventory().equals(shopInv)) {
                    shop.updateSign(Economy.getMoneyExact(playerID));
                }
            }
        }
    }

}
