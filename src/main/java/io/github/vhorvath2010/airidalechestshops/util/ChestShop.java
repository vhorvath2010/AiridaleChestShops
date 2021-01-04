package io.github.vhorvath2010.airidalechestshops.util;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ChestShop {

    public Sign sign;
    public Chest chest;
    public ItemStack item;
    public int transactionAmount;
    public double value;
    public Player owner;

    public abstract void conductTransaction(Player player) throws UserDoesNotExistException, NoLoanPermittedException;

}
