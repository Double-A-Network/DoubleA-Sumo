package com.andrew121410.mc.doubleasumo.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SetListMap {

    private List<UUID> frozenPlayers;
    private List<UUID> disableStuff; //Lol very descriptive
    private Map<UUID, ItemStack[]> playerInvSave;
    private List<UUID> opsList;

    public SetListMap() {
        this.frozenPlayers = new ArrayList<>();
        this.disableStuff = new ArrayList<>();
        this.playerInvSave = new HashMap<>();
        this.opsList = new ArrayList<>();
    }

    public List<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    public Map<UUID, ItemStack[]> getPlayerInvSave() {
        return playerInvSave;
    }

    public List<UUID> getDisableStuff() {
        return disableStuff;
    }

    public List<UUID> getOpsList() {
        return opsList;
    }
}
