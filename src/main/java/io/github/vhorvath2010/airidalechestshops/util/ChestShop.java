package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ChestShop implements ConfigurationSerializable {

    public Sign sign;
    public Chest chest;
    public String item;
    public int transactionAmount;
    public double value;
    public UUID owner;
    public boolean isEnchanted;

    public abstract void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException;

    // This method will return true if the sign and chest for a shop still exist
    public boolean isValid() {
        return sign == null || chest == null;
    }

    public Chest getChest() {
        return chest;
    }

    public Sign getSign() {
        return sign;
    }

    // This method will update the sign states
    public abstract void updateSign(BigDecimal newBalance) throws UserDoesNotExistException;

    // This method will set the lines of the shop to a certain config section
    public void setSignState(String state) {
        // Get/Set line data
        List<String> lines = new ArrayList<>();
        for (int lineNum = 0; lineNum < 4; ++lineNum) {
            sign.setLine(lineNum, MessagingUtils.parsePlaceholders(MessagingUtils.getConfigMsg("formats." + state + "." + lineNum), value, transactionAmount, item, Bukkit.getOfflinePlayer(owner).getName()));
        }
    }

}
