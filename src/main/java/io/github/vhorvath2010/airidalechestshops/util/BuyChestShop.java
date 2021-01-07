package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BuyChestShop extends ChestShop {

    @Override
    public void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException {
        Inventory playerInv = player.getInventory();
        Inventory shopInv = chest.getBlockInventory();
        // Ensure shop has items to sell

    }
}
