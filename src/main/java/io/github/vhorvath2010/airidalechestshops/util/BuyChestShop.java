package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;

public class BuyChestShop extends ChestShop {

    @Override
    public void conductTransaction(Player buyer) throws UserDoesNotExistException, NoLoanPermittedException {
        Inventory buyerInv = buyer.getInventory();
        Inventory shopInv = chest.getBlockInventory();
        // Ensure player has enough money
        if (Economy.getMoneyExact(buyer.getUniqueId()).compareTo(BigDecimal.valueOf(value)) > 0) {
            // Ensure shop has items to sell
            if (InventoryUtils.countItems(shopInv, item, isEnchanted) >= transactionAmount) {
                // Ensure player has space
                if (InventoryUtils.hasEmpty(buyer.getInventory().getStorageContents())) {
                    // Conduct transaction
                    Economy.subtract(buyer.getUniqueId(), BigDecimal.valueOf(value));
                    Economy.add(owner.getUniqueId(), BigDecimal.valueOf(value));
                    InventoryUtils.transferItems(shopInv, buyerInv, item, isEnchanted, transactionAmount);
                    buyer.sendMessage(ChatColor.GREEN + "Purchase complete!");
                } else {
                    buyer.sendMessage(ChatColor.RED + "You do not have enough space!");
                }
            } else {
                setSignState("no_item");
            }
        } else {
            buyer.sendMessage(ChatColor.RED + "You do not have enough money!");
        }

    }
}
