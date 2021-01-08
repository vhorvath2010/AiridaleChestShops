package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;

public class SellChestShop extends ChestShop {

    @Override
    public void conductTransaction(Player seller) throws UserDoesNotExistException, NoLoanPermittedException {
        Inventory sellerInv = seller.getInventory();
        Inventory shopInv = chest.getBlockInventory();
        // Ensure owner has enough money
        if (Economy.getMoneyExact(owner.getUniqueId()).compareTo(BigDecimal.valueOf(value)) > 0) {
            // Ensure chest has space
            if (InventoryUtils.hasEmpty(shopInv.getStorageContents())) {
                // Ensure player has enough items
                if (InventoryUtils.countItems(sellerInv, item, isEnchanted) >= transactionAmount) {
                    // Conduct transaction
                    Economy.subtract(owner.getUniqueId(), BigDecimal.valueOf(value));
                    Economy.add(seller.getUniqueId(), BigDecimal.valueOf(value));
                    InventoryUtils.transferItems(sellerInv, shopInv, item, isEnchanted, transactionAmount);
                    seller.sendMessage(ChatColor.GREEN + "Purchase complete!");
                } else {
                    seller.sendMessage(ChatColor.RED + "You do not have enough items to sell!");
                }
            } else {
                setSignState("full");
            }
        } else {
            setSignState("no_money");
        }
    }
}
