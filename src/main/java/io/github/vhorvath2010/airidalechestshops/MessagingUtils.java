package io.github.vhorvath2010.airidalechestshops;

import org.bukkit.ChatColor;

public class MessagingUtils {

    public static String getConfigMsg(String path) {
        String msg = AiridaleChestShops.getPlugin().getConfig().getString(path);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
