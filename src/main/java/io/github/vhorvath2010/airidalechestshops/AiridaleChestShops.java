package io.github.vhorvath2010.airidalechestshops;

import org.bukkit.plugin.java.JavaPlugin;

public class AiridaleChestShops extends JavaPlugin {

    private static AiridaleChestShops plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
    }

    public static AiridaleChestShops getPlugin() {
        return plugin;
    }
}
