package com.andrew121410.mc.doubleasumo.commands;

import com.andrew121410.mc.doubleasumo.DoubleASumo;
import com.andrew121410.mc.doubleasumo.objects.SumoMatch;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SumoCMD implements CommandExecutor {

    private DoubleASumo plugin;

    public SumoCMD(DoubleASumo plugin) {
        this.plugin = plugin;

        this.plugin.getCommand("sumo").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Translate.color("&6/sumo create"));
            player.sendMessage(Translate.color("&6/sumo join"));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
            if (this.plugin.getCurrentMatch() != null) {
                player.sendMessage(Translate.color("&4Sorry a game is already created by: " + this.plugin.getCurrentMatch().getPlayerHost().getDisplayName()));
                return true;
            }
            SumoMatch sumoMatch = new SumoMatch(this.plugin, player);
            this.plugin.setCurrentMatch(sumoMatch);
            this.plugin.getServer().broadcastMessage(Translate.color("[&6Sumo&r] &2" + player.getDisplayName() + " has created a sumo match to join do /sumo join"));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (plugin.getCurrentMatch() != null) {
                        if (plugin.getCurrentMatch().getOtherPlayer() == null) {
                            plugin.setCurrentMatch(null);
                            plugin.getServer().broadcastMessage(Translate.color("[&6Sumo&r] &cLooks like nobody joined the sumo match in time."));
                            this.cancel();
                        }
                    }
                }
            }.runTaskLater(this.plugin, 1200L);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("join")) {
            SumoMatch sumoMatch = this.plugin.getCurrentMatch();

            if (sumoMatch == null) {
                player.sendMessage(Translate.color("&cNobody has created a sumo match to make one do /sumo create"));
                return true;
            }

            if (sumoMatch.getPlayerHost().getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(Translate.color("&cYou are the host why are you trying to join it?"));
                return true;
            }

            if (!sumoMatch.joinMatch(player)) {
                player.sendMessage(Translate.color("&cThere's already 2 players in the sumo match"));
                return true;
            }
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            if (!player.hasPermission("doublea.sumo.admin")) {
                return true;
            }
            String point = args[1];

            switch (point.toLowerCase()) {
                case "a":
                    this.plugin.getSumoConfig().setPointA(player.getLocation().clone());
                    player.sendMessage(Translate.color("&6Point A has been set."));
                    break;
                case "b":
                    this.plugin.getSumoConfig().setPointB(player.getLocation().clone());
                    player.sendMessage(Translate.color("&6Point B has been set."));
                    break;
            }

        }
        return true;
    }
}