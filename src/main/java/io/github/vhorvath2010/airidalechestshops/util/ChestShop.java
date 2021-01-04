package io.github.vhorvath2010.airidalechestshops.util;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ChestShop {

    private Sign sign;
    private Chest chest;
    private ItemStack item; 
    private int amt;
    private double value;
    private Player owner;

    public abstract void conductTransaction(Player player);

}
