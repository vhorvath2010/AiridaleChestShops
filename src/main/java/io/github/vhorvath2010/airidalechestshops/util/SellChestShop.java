package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.MessagingUtils;
import net.ess3.api.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class SellChestShop extends ChestShop {

    @Override
    public void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException {
        // Ensure enough items, money, and inventory space
        Inventory playerInv = player.getInventory();
        Inventory chestInv = chest.getBlockInventory();
        if (InventoryUtils.countItems(playerInv, item) >= transactionAmount) {
            if (Economy.hasEnough(owner.getUniqueId(), BigDecimal.valueOf(value))) {
                if (chestInv.firstEmpty() != -1) {
                    // Conduct transaction
                    Economy.subtract(owner.getUniqueId(), BigDecimal.valueOf(value));
                    Economy.add(player.getUniqueId(), BigDecimal.valueOf(value));
                    ItemStack saleItem = item.clone();
                    saleItem.setAmount(transactionAmount);
                    playerInv.remove(saleItem);
                    chest.getBlockInventory().addItem(saleItem);
                } else {
                    player.sendMessage(MessagingUtils.getConfigMsg("messages.no_space"));
                }
            }
        } else {
            player.sendMessage(MessagingUtils.getConfigMsg("messages.no_items"));
        }
    }
}
