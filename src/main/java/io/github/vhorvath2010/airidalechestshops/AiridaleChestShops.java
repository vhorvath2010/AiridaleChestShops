package io.github.vhorvath2010.airidalechestshops;

import io.github.vhorvath2010.airidalechestshops.util.BuyChestShop;
import io.github.vhorvath2010.airidalechestshops.util.SellChestShop;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class AiridaleChestShops extends JavaPlugin {

    private static AiridaleChestShops plugin;

    @Override
    public void onEnable() {
        plugin = this;
        ConfigurationSerialization.registerClass(SellChestShop.class);
        ConfigurationSerialization.registerClass(BuyChestShop.class);
        saveDefaultConfig();
    }

    public static AiridaleChestShops getPlugin() {
        return plugin;
    }
}
