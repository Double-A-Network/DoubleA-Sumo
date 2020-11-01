package com.andrew121410.mc.doubleasumo;

import com.andrew121410.mc.doubleasumo.commands.SumoCMD;
import com.andrew121410.mc.doubleasumo.events.OnPlayerCommandPreprocessEvent;
import com.andrew121410.mc.doubleasumo.events.OnPlayerMoveEvent;
import com.andrew121410.mc.doubleasumo.objects.SumoMatch;
import com.andrew121410.mc.doubleasumo.utils.OtherPlugins;
import com.andrew121410.mc.doubleasumo.utils.SetListMap;
import com.andrew121410.mc.doubleasumo.utils.SumoConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class DoubleASumo extends JavaPlugin {

    private SetListMap setListMap;
    private OtherPlugins otherPlugins;
    private SumoConfig sumoConfig;

    private SumoMatch currentMatch = null;

    @Override
    public void onEnable() {
        this.setListMap = new SetListMap();
        this.otherPlugins = new OtherPlugins(this);
        this.sumoConfig = new SumoConfig(this);
        registerEvents();
        registerCommands();
        this.getServer().getConsoleSender().sendMessage("Double-A Sumo has been loaded.");
    }

    private void registerEvents() {
        new OnPlayerMoveEvent(this);
        new OnPlayerCommandPreprocessEvent(this);
    }

    private void registerCommands() {
        new SumoCMD(this);
    }

    @Override
    public void onDisable() {
        this.sumoConfig.saveConfig();
    }

    public OtherPlugins getOtherPlugins() {
        return otherPlugins;
    }

    public SumoConfig getSumoConfig() {
        return sumoConfig;
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public SumoMatch getCurrentMatch() {
        return currentMatch;
    }

    public void setCurrentMatch(SumoMatch currentMatch) {
        this.currentMatch = currentMatch;
    }
}
