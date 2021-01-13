package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
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

public class SellChestShop extends ChestShop {

    public SellChestShop(Block sign, Block chest, String item, int amt, double value, UUID owner, boolean enchanted) {
        this.sign = sign;
        this.chest = chest;
        this.item = item;
        this.transactionAmount = amt;
        this.value = value;
        this.owner = owner;
        this.isEnchanted = enchanted;
    }

    public SellChestShop(Map<String, Object> serializedChestShop) {
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
    public void conductTransaction(Player seller) throws UserDoesNotExistException, NoLoanPermittedException {
        Inventory sellerInv = seller.getInventory();
        Inventory shopInv;
        if (getChest() != null) {
            shopInv = getChest().getInventory();
        } else {
            shopInv = getBarrel().getInventory();
        }
        // Ensure owner has enough money
        if (Economy.getMoneyExact(owner).compareTo(BigDecimal.valueOf(value)) >= 0) {
            // Ensure chest has space
            if (InventoryUtils.hasEmpty(shopInv.getStorageContents())) {
                // Ensure player has enough items
                if (InventoryUtils.countItems(sellerInv, item, isEnchanted) >= transactionAmount) {
                    // Conduct transaction
                    InventoryUtils.transferItems(sellerInv, shopInv, item, isEnchanted, transactionAmount);
                    Economy.subtract(owner, BigDecimal.valueOf(value));
                    Economy.add(seller.getUniqueId(), BigDecimal.valueOf(value));
                    seller.sendMessage(ChatColor.GREEN + "Sale complete!");
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
        Inventory shopInv;
        if (!isValid()) {
            return;
        }
        if (getChest() != null) {
            shopInv = getChest().getInventory();
        } else {
            shopInv = getBarrel().getInventory();
        }
        if (newBalance.compareTo(BigDecimal.valueOf(value)) < 0) {
            setSignState("no_money");
            return;
        }
        if (!InventoryUtils.hasEmpty(shopInv.getStorageContents())) {
            setSignState("full");
            return;
        }
        if (isEnchanted) {
            setSignState("sell_ench");
            return;
        }
        setSignState("sell");
    }

}
