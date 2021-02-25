package io.github.vhorvath2010.airidalechestshops.events;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.ChestShop;
import io.github.vhorvath2010.airidalechestshops.util.ChestShopManager;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class TransactionEvents implements Listener {

    private ArrayList<Block> recentlyInvalid;

    public TransactionEvents() {
        recentlyInvalid = new ArrayList<>();
    }

    @EventHandler
    public void clickShopSign(PlayerInteractEvent e) throws UserDoesNotExistException, NoLoanPermittedException {
        Block clicked = e.getClickedBlock();
        if (!recentlyInvalid.contains(clicked) && clicked != null && clicked.getState() instanceof Sign) {
            ChestShopManager chestShopManager = AiridaleChestShops.getPlugin().getChestShopManager();
            for (UUID ownerID : chestShopManager.getIDS()) {
                for (ChestShop shop : chestShopManager.getShops(ownerID)) {
                    if (shop != null && shop.isValid() && !ownerID.equals(e.getPlayer().getUniqueId()) && shop.getSign().getBlock().equals(clicked)) {
                        shop.conductTransaction(e.getPlayer());
                        return;
                    }
                }
            }
            recentlyInvalid.add(clicked);
            new BukkitRunnable() {
                @Override
                public void run() {
                    recentlyInvalid.remove(clicked);
                }
            }.runTaskLater(AiridaleChestShops.getPlugin(), 100);
        }
    }

}
