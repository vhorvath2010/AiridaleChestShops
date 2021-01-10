package io.github.vhorvath2010.airidalechestshops.events;

import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import io.github.vhorvath2010.airidalechestshops.util.ChestShop;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class BalanceEvents implements Listener {

    @EventHandler
    public void onBalChange(UserBalanceUpdateEvent e) throws UserDoesNotExistException {
        UUID playerID = e.getPlayer().getUniqueId();
        ArrayList<ChestShop> shops = AiridaleChestShops.getPlugin().getChestShopManager().getShops(playerID);
        for (ChestShop shop : shops) {
            shop.updateSign(e.getNewBalance());
        }
    }

}
