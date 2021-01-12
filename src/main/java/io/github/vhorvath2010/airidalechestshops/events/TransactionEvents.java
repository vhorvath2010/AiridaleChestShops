package io.github.vhorvath2010.airidalechestshops.events;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.ChestShop;
import io.github.vhorvath2010.airidalechestshops.util.ChestShopManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class TransactionEvents implements Listener {

    @EventHandler
    public void clickShopSign(PlayerInteractEvent e) throws UserDoesNotExistException, NoLoanPermittedException {
        Block clicked = e.getClickedBlock();
        if (clicked != null) {
            ChestShopManager chestShopManager = AiridaleChestShops.getPlugin().getChestShopManager();
            for (UUID ownerID : chestShopManager.getIDS()) {
                for (ChestShop shop : chestShopManager.getShops(ownerID)) {
                    if (!ownerID.equals(e.getPlayer().getUniqueId()) && shop.getSign().getBlock().equals(clicked)) {
                        shop.conductTransaction(e.getPlayer());
                    }
                }
            }
        }
    }

}
