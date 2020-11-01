package com.andrew121410.mc.doubleasumo.events;

import com.andrew121410.mc.doubleasumo.DoubleASumo;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreprocessEvent implements Listener {

    private DoubleASumo plugin;

    public OnPlayerCommandPreprocessEvent(DoubleASumo plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onCommandEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getSetListMap().getDisableStuff().contains(player.getUniqueId())) {
            player.sendMessage(Translate.color("[&6Sumo] &cYou are unable to do commands while in a sumo match."));
            event.setCancelled(true);
        }
    }
}
