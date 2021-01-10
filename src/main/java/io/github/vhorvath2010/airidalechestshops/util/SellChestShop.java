package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
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

public class SellChestShop extends ChestShop {

    public SellChestShop(Map<String, Object> serializedChestShop) {
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
    public void conductTransaction(Player seller) throws UserDoesNotExistException, NoLoanPermittedException {
        Inventory sellerInv = seller.getInventory();
        Inventory shopInv = chest.getBlockInventory();
        // Ensure owner has enough money
        if (Economy.getMoneyExact(owner).compareTo(BigDecimal.valueOf(value)) >= 0) {
            // Ensure chest has space
            if (InventoryUtils.hasEmpty(shopInv.getStorageContents())) {
                // Ensure player has enough items
                if (InventoryUtils.countItems(sellerInv, item, isEnchanted) >= transactionAmount) {
                    // Conduct transaction
                    Economy.subtract(owner, BigDecimal.valueOf(value));
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

    @Override
    public void updateSign(BigDecimal newBalance) throws UserDoesNotExistException {
        if (newBalance.compareTo(BigDecimal.valueOf(value)) < 0) {
            setSignState("no_money");
            return;
        }
        if (!InventoryUtils.hasEmpty(chest.getBlockInventory().getStorageContents())) {
            setSignState("full");
            return;
        }
        setSignState("sell");
    }

}
