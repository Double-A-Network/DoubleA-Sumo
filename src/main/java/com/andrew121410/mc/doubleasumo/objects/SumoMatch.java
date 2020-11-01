package com.andrew121410.mc.doubleasumo.objects;

import com.andrew121410.mc.doubleasumo.DoubleASumo;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.runnable.CountdownTimer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SumoMatch {

    private DoubleASumo plugin;

    private Player playerHost;
    private Player otherPlayer;

    private boolean isMatchRunning = false;

    public SumoMatch(DoubleASumo plugin, Player playerHost) {
        this.plugin = plugin;
        this.playerHost = playerHost;
    }

    public boolean joinMatch(Player otherPlayer) {
        if (this.otherPlayer == null) {
            this.otherPlayer = otherPlayer;
            setupGame();
            return true;
        }
        return false;
    }

    private void setupGame() {
        new CountdownTimer(this.plugin, 5, () -> {
            this.playerHost.teleport(this.plugin.getSumoConfig().getPointA());
            this.otherPlayer.teleport(this.plugin.getSumoConfig().getPointB());
            this.playerHost.setGameMode(GameMode.SURVIVAL);
            this.otherPlayer.setGameMode(GameMode.SURVIVAL);

            if (playerHost.isOp()) {
                this.plugin.getSetListMap().getOpsList().add(playerHost.getUniqueId());
                playerHost.setOp(false);
            }
            if (otherPlayer.isOp()) {
                this.plugin.getSetListMap().getOpsList().add(otherPlayer.getUniqueId());
                otherPlayer.setOp(false);
            }

            this.plugin.getSetListMap().getFrozenPlayers().add(playerHost.getUniqueId());
            this.plugin.getSetListMap().getFrozenPlayers().add(otherPlayer.getUniqueId());
            this.plugin.getOtherPlugins().getDoubleAHub().getSetListMap().getNoDoubleJumpUUID().add(playerHost.getUniqueId());
            this.plugin.getOtherPlugins().getDoubleAHub().getSetListMap().getNoDoubleJumpUUID().add(otherPlayer.getUniqueId());

            this.plugin.getSetListMap().getPlayerInvSave().put(playerHost.getUniqueId(), playerHost.getInventory().getContents());
            this.plugin.getSetListMap().getPlayerInvSave().put(otherPlayer.getUniqueId(), otherPlayer.getInventory().getContents());
            playerHost.getInventory().clear();
            otherPlayer.getInventory().clear();

            ItemStack woodStick = createItem(Material.STICK, 1, "Stick", "Knock Knock");
            woodStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);

            this.playerHost.getInventory().addItem(woodStick);
            this.otherPlayer.getInventory().addItem(woodStick);
        }, this::startGame, (timer) -> {
            onEverySecondTimer(this.playerHost, timer);
            onEverySecondTimer(this.otherPlayer, timer);
        }).scheduleTimer();
    }

    private void onEverySecondTimer(Player player, CountdownTimer countdownTimer) {
        player.sendTitle(Translate.color("&cStarting in: &2" + countdownTimer.getSecondsLeft()), null, 10, 10, 10);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    private void startGame() {
        this.isMatchRunning = true;
        this.plugin.getSetListMap().getDisableStuff().add(playerHost.getUniqueId());
        this.plugin.getSetListMap().getDisableStuff().add(otherPlayer.getUniqueId());
        this.plugin.getSetListMap().getFrozenPlayers().remove(playerHost.getUniqueId());
        this.plugin.getSetListMap().getFrozenPlayers().remove(otherPlayer.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isMatchRunning) {
                    this.cancel();
                    return;
                }
                Player whoWon = checkIfFall();
                stopGame(whoWon);
            }
        }.runTaskTimer(this.plugin, 0L, 10L);
    }

    private void stopGame(Player whoWon) {
        if (whoWon == null) return;
        this.isMatchRunning = false;

        for (UUID uuid : this.plugin.getSetListMap().getOpsList()) {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player == null) return;
            player.setOp(true);
        }
        this.plugin.getSetListMap().getOpsList().clear();

        this.plugin.getSetListMap().getDisableStuff().clear();
        this.plugin.getOtherPlugins().getDoubleAHub().getSetListMap().getNoDoubleJumpUUID().remove(playerHost.getUniqueId());
        this.plugin.getOtherPlugins().getDoubleAHub().getSetListMap().getNoDoubleJumpUUID().remove(otherPlayer.getUniqueId());
        this.playerHost.getInventory().clear();
        this.otherPlayer.getInventory().clear();
        this.playerHost.getInventory().setContents(this.plugin.getSetListMap().getPlayerInvSave().get(this.playerHost.getUniqueId()));
        this.otherPlayer.getInventory().setContents(this.plugin.getSetListMap().getPlayerInvSave().get(this.otherPlayer.getUniqueId()));
        this.plugin.getSetListMap().getPlayerInvSave().clear();
        this.plugin.getServer().broadcastMessage(Translate.color("[&6Sumo&r] &9" + whoWon.getDisplayName() + " has won the sumo match!"));
        this.plugin.setCurrentMatch(null);
    }

    private Player checkIfFall() {
        if (playerHost.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK) {
            return otherPlayer;
        } else if (otherPlayer.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK) {
            return playerHost;
        }
        return null;
    }

    public static ItemStack createItem(Material material, int amount, String displayName, String... loreString) {
        List<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Translate.color(displayName));
        for (String s : loreString) lore.add(Translate.chat(s));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public Player getPlayerHost() {
        return playerHost;
    }

    public Player getOtherPlayer() {
        return otherPlayer;
    }

    public boolean isMatchRunning() {
        return isMatchRunning;
    }
}
