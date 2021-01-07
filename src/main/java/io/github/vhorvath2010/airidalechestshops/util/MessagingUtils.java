package io.github.vhorvath2010.airidalechestshops.util;

import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import org.bukkit.ChatColor;

public class MessagingUtils {

    public static String getConfigMsg(String path) {
        String msg = AiridaleChestShops.getPlugin().getConfig().getString(path);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String parsePlaceholders(String text, double value, int amt, String item, String ownerName) {
        text = text.replaceAll("%value%", value + "");
        text = text.replaceAll("%amt%", amt + "");
        text = text.replaceAll("%item%", item + "");
        text = text.replaceAll("%owner%", ownerName);
        return text;
    }

}
