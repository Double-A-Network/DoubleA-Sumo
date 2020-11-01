package com.andrew121410.mc.doubleasumo.utils;

import com.andrew121410.mc.doubleahub.DoubleAHub;
import com.andrew121410.mc.doubleasumo.DoubleASumo;
import org.bukkit.plugin.Plugin;

public class OtherPlugins {

    private DoubleASumo plugin;

    private DoubleAHub doubleAHub;

    public OtherPlugins(DoubleASumo plugin) {
        this.plugin = plugin;

        setupDoubleAHub();
    }

    private void setupDoubleAHub() {
        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin("DoubleA-Hub");

        if (plugin instanceof DoubleAHub) {
            this.doubleAHub = (DoubleAHub) plugin;
        }
    }

    public DoubleAHub getDoubleAHub() {
        return doubleAHub;
    }
}
