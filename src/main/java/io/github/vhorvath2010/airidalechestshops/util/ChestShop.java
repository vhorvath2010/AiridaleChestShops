package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ChestShop implements ConfigurationSerializable {

    public Block sign;
    public Block chest;
    public String item;
    public int transactionAmount;
    public double value;
    public UUID owner;
    public boolean isEnchanted;

    public abstract void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException;

    // This method will return true if the sign and chest for a shop still exist
    public boolean isValid() {
        return (sign != null && chest != null);
    }

    public Chest getChest() {
        if (isValid() && chest.getState() instanceof Chest) {
            return (Chest) chest.getState();
        }
        return null;
    }

    public Barrel getBarrel() {
        if (isValid() && chest.getState() instanceof Barrel) {
            return (Barrel) chest.getState();
        }
        return null;
    }

    public Block getContainer() {
        return chest;
    }

    public Sign getSign() {
        if (isValid() && sign.getState() instanceof Sign) {
            return (Sign) sign.getState();
        }
        return null;
    }

    public Block getSignBlock() {
        if (isValid()) {
            return sign;
        }
        return null;
    }

    // This method will update the sign states
    public abstract void updateSign(BigDecimal newBalance) throws UserDoesNotExistException;

    // This method will set the lines of the shop to a certain config section
    public void setSignState(String state) {
        if (!isValid()) {
            return;
        }
        if (!(sign.getState() instanceof Sign)){
            return;
        }
        // Get/Set line data
        List<String> lines = new ArrayList<>();
        Sign sign = getSign();
        for (int lineNum = 0; lineNum < 4; ++lineNum) {
            String lineMsg = MessagingUtils.parsePlaceholders(AiridaleChestShops.getPlugin().getConfig().getString("formats." + state + "." + lineNum), value, transactionAmount, item, Bukkit.getOfflinePlayer(owner).getName());
            lineMsg = ChatColor.translateAlternateColorCodes('&', lineMsg);
            sign.setLine(lineNum, lineMsg);
        }
        sign.update();
    }

}
