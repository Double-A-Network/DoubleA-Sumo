package com.andrew121410.mc.doubleasumo.events;

import com.andrew121410.mc.doubleasumo.DoubleASumo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {

    private DoubleASumo plugin;

    public OnPlayerMoveEvent(DoubleASumo plugin){
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if (this.plugin.getSetListMap().getFrozenPlayers().contains(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }
}
