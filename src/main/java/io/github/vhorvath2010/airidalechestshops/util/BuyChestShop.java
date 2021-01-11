package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuyChestShop extends ChestShop {

    public BuyChestShop(Block sign, Block chest, String item, int amt, double value, UUID owner, boolean enchanted) {
        this.sign = sign;
        this.chest = chest;
        this.item = item;
        this.transactionAmount = amt;
        this.value = value;
        this.owner = owner;
        this.isEnchanted = enchanted;
    }

    public BuyChestShop(Map<String, Object> serializedChestShop) {
        // Validate chests and signs:
        Location signLoc = (Location) serializedChestShop.get("signLoc");
        Location chestLoc = (Location) serializedChestShop.get("chestLoc");
        if (signLoc.getBlock().getState() instanceof Sign) {
            this.sign = signLoc.getBlock();
        }
        if (chestLoc.getBlock().getState() instanceof Chest) {
            this.chest = chestLoc.getBlock();
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
        Inventory shopInv;
        if (getChest() != null) {
            shopInv = getChest().getInventory();
        } else {
            shopInv = getBarrel().getInventory();
        }
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
        Inventory shopInv;
        if (getChest() != null) {
            shopInv = getChest().getInventory();
        } else {
            shopInv = getBarrel().getInventory();
        }
        if (InventoryUtils.countItems(shopInv, item, isEnchanted) < transactionAmount) {
            setSignState("no_item");
            return;
        }
        if (isEnchanted) {
            setSignState("buy_ench");
            return;
        }
        setSignState("buy");
    }
}
