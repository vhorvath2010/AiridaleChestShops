package io.github.vhorvath2010.airidalechestshops.util;

import java.util.ArrayList;

public class ChestShopManager {

    private ArrayList<ChestShop> activeShops;

    public ChestShopManager() {
        activeShops = new ArrayList<>();
    }

    public void registerShop(ChestShop shop) {
        activeShops.add(shop);
    }

    public void removeShop(ChestShop shop) {
        activeShops.remove(shop);
    }

    public void saveShops() {

    }

    public void loadShops() {

    }

}
