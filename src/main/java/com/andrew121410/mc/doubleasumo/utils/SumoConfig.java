package com.andrew121410.mc.doubleasumo.utils;

import com.andrew121410.mc.doubleasumo.DoubleASumo;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.Location;

public class SumoConfig {

    private DoubleASumo plugin;

    private CustomYmlManager ymlManager;

    private Location pointA;
    private Location pointB;

    public SumoConfig(DoubleASumo plugin) {
        this.plugin = plugin;

        this.ymlManager = new CustomYmlManager(this.plugin, false);
        this.ymlManager.setup("sumo.yml");
        this.ymlManager.saveConfig();
        this.ymlManager.reloadConfig();

        this.pointA = this.ymlManager.getConfig().getLocation("pointA");
        this.pointB = this.ymlManager.getConfig().getLocation("pointB");
    }

    public void saveConfig() {
        this.ymlManager.getConfig().set("pointA", this.pointA);
        this.ymlManager.getConfig().set("pointB", this.pointB);
        this.ymlManager.saveConfig();
    }

    public Location getPointA() {
        return pointA;
    }

    public Location getPointB() {
        return pointB;
    }

    public void setPointA(Location pointA) {
        this.pointA = pointA;
    }

    public void setPointB(Location pointB) {
        this.pointB = pointB;
    }
}
