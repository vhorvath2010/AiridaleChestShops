package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuyChestShop extends ChestShop {

    public BuyChestShop(Map<String, Object> serializedChestShop) {
        // Validate chests and signs:
        Location signLoc = (Location) serializedChestShop.get("signLoc");
        Location chestLoc = (Location) serializedChestShop.get("chestLoc");
        if (signLoc.getBlock().getType().toString().contains("_SIGN")) {
            this.sign = (Sign) signLoc.getBlock();
        }
        if (chestLoc.getBlock().getType() == Material.CHEST) {
            this.chest = (Chest) chestLoc.getBlock();
        }
        // Get other fields
        this.item = (String) serializedChestShop.get("item");
        this.transactionAmount = (int) serializedChestShop.get("transactionAmount");
        this.value = (double) serializedChestShop.get("value");
        this.owner = UUID.fromString((String) serializedChestShop.get("owner"));
        this.isEnchanted = (boolean) serializedChestShop.get("isEnchanted");
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> mapSerializer = new HashMap<>();
        // Add values
        mapSerializer.put("signLoc", sign.getLocation());
        mapSerializer.put("chestLoc", chest.getLocation());
        mapSerializer.put("item", item);
        mapSerializer.put("transactionAmount", transactionAmount);
        mapSerializer.put("value", value);
        mapSerializer.put("owner", owner.toString());
        mapSerializer.put("isEnchanted", isEnchanted);
        return mapSerializer;
    }

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
                    Economy.add(owner, BigDecimal.valueOf(value));
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

    @Override
    public void updateSign(BigDecimal newBalance) {
        if (InventoryUtils.countItems(chest.getBlockInventory(), item, isEnchanted) < transactionAmount) {
            setSignState("no_item");
            return;
        }
        setSignState("buy");
    }
}
