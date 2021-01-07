package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public abstract class ChestShop {

    public Sign sign;
    public Chest chest;
    public String item;
    public int transactionAmount;
    public double value;
    public Player owner;
    public boolean isEnchanted;

    public abstract void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException;

    // This method will return true if the sign and chest for a shop still exist
    public boolean isValid() {
        return sign == null || chest == null;
    }

}
