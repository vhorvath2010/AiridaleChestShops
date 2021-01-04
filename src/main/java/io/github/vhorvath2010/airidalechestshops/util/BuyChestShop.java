package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.MessagingUtils;
import net.ess3.api.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class BuyChestShop extends ChestShop {

    @Override
    public void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException {
        // Ensure enough items, money, and inventory space
        Inventory chestInv = chest.getBlockInventory();
        Inventory playerInv = player.getInventory();
        if (InventoryUtils.countItems(chestInv, item) >= transactionAmount) {
            if (Economy.hasEnough(player.getUniqueId(), BigDecimal.valueOf(value))) {
                if (playerInv.firstEmpty() != -1) {
                    // Conduct transaction
                    Economy.subtract(player.getUniqueId(), BigDecimal.valueOf(value));
                    Economy.add(owner.getUniqueId(), BigDecimal.valueOf(value));
                    ItemStack saleItem = item.clone();
                    saleItem.setAmount(transactionAmount);
                    chestInv.remove(saleItem);
                    playerInv.addItem(saleItem);
                } else {
                    player.sendMessage(MessagingUtils.getConfigMsg("messages.no_space"));
                }
            } else {
                player.sendMessage(MessagingUtils.getConfigMsg("messages.no_money"));
            }
        }
    }
}
