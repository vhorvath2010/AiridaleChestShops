package io.github.vhorvath2010.airidalechestshops.util;

import io.github.vhorvath2010.airidalechestshops.AiridaleChestShops;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChestShopManager {

    // Store chest shops by player
    private HashMap<UUID, ArrayList<ChestShop>> activeShops;

    public ChestShopManager() {
        activeShops = new HashMap<>();
    }

    public void registerShop(ChestShop shop) {
        ArrayList<ChestShop> shops = new ArrayList<>();
        if (activeShops.containsKey(shop.owner)) {
            shops = activeShops.get(shop.owner);
        }
        shops.add(shop);
        activeShops.put(shop.owner, shops);
    }

    public ArrayList<ChestShop> getShops(UUID playerID) {
        return activeShops.get(playerID);
    }

    public void removeShop(ChestShop shop) {
        if (activeShops.containsKey(shop.owner)) {
            activeShops.get(shop.owner).remove(shop);
        }
    }

    public void saveShops() throws IOException {
        File buyShopFile = new File(AiridaleChestShops.getPlugin().getDataFolder(), "buyShops.yml");
        File sellShopFile = new File(AiridaleChestShops.getPlugin().getDataFolder(), "sellShops.yml");
        YamlConfiguration buyConfig = new YamlConfiguration();
        YamlConfiguration sellConfig = new YamlConfiguration();
        // Save each player's items
        for (UUID id : activeShops.keySet()) {
            ArrayList<SellChestShop> sellShops = new ArrayList<>();
            ArrayList<BuyChestShop> buyShops = new ArrayList<>();
            // Add player's shops to proper list
            for (ChestShop shop : activeShops.get(id)) {
                if (shop instanceof BuyChestShop) {
                    buyShops.add((BuyChestShop) shop);
                } else {
                    sellShops.add((SellChestShop) shop);
                }
            }
            // Save lists
            sellConfig.set("shops." + id.toString(), sellShops);
            buyConfig.set("shops." + id.toString(), buyShops);
        }
        // Save to files
        buyConfig.save(buyShopFile);
        sellConfig.save(sellShopFile);
    }

    public void loadShops() {
        File buyShopFile = new File(AiridaleChestShops.getPlugin().getDataFolder(), "buyShops.yml");
        File sellShopFile = new File(AiridaleChestShops.getPlugin().getDataFolder(), "sellShops.yml");
        // Load buy shops
        if (buyShopFile.exists()) {
            YamlConfiguration buyConfig = new YamlConfiguration();
            for (String IDString : buyConfig.getConfigurationSection("shops").getKeys(false)) {
                // Register players shops
                ArrayList<BuyChestShop> buyShops = (ArrayList<BuyChestShop>) buyConfig.get("shops." + IDString);
                for (ChestShop shop : buyShops) {
                    registerShop(shop);
                }
            }
        }
        // Load sell shops
        if (sellShopFile.exists()) {
            YamlConfiguration sellConfig = new YamlConfiguration();
            for (String IDString : sellConfig.getConfigurationSection("shops").getKeys(false)) {
                // Register players shops
                ArrayList<SellChestShop> sellShops = (ArrayList<SellChestShop>) sellConfig.get("shops." + IDString);
                for (ChestShop shop : sellShops) {
                    registerShop(shop);
                }
            }
        }
    }

}
