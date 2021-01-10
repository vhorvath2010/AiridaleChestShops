package io.github.vhorvath2010.airidalechestshops;

import io.github.vhorvath2010.airidalechestshops.events.BalanceEvents;
import io.github.vhorvath2010.airidalechestshops.events.ChestEvents;
import io.github.vhorvath2010.airidalechestshops.util.BuyChestShop;
import io.github.vhorvath2010.airidalechestshops.util.ChestShopManager;
import io.github.vhorvath2010.airidalechestshops.util.SellChestShop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class AiridaleChestShops extends JavaPlugin {

    private static AiridaleChestShops plugin;
    private ChestShopManager chestShopManager;

    @Override
    public void onEnable() {
        plugin = this;
        ConfigurationSerialization.registerClass(SellChestShop.class);
        ConfigurationSerialization.registerClass(BuyChestShop.class);
        saveDefaultConfig();
        // Load chest shops
        chestShopManager = new ChestShopManager();
        chestShopManager.loadShops();
        // Register events
        Bukkit.getPluginManager().registerEvents(new BalanceEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ChestEvents(), this);
    }

    public static AiridaleChestShops getPlugin() {
        return plugin;
    }

    public ChestShopManager getChestShopManager() {
        return chestShopManager;
    }
}
