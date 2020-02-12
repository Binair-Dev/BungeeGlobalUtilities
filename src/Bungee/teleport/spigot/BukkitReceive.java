package Bungee.teleport.spigot;

import org.bukkit.plugin.java.*;
import org.bukkit.plugin.messaging.*;
import org.bukkit.plugin.*;
import java.util.*;
import java.io.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class BukkitReceive extends JavaPlugin implements PluginMessageListener, Listener
{
    private static Plugin instance;
    private static String channel;
    public static String join;
    public static String leave;
    public static List<String> vanishstate;
    
    static {
        BukkitReceive.vanishstate = new ArrayList<String>();
        BukkitReceive.channel = "bungee:utilities";
    }
    
    public void onEnable() {
        BukkitReceive.instance = (Plugin)this;
        if (MCVersion.get().isInferior(MCVersion.v1_13)) {
            BukkitReceive.channel = "BungeeUtilities";
        }
        Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin)this, BukkitReceive.channel);
        Bukkit.getMessenger().registerIncomingPluginChannel((Plugin)this, BukkitReceive.channel, (PluginMessageListener)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    }
    
    public void onDisable() {
        BukkitReceive.instance = null;
    }
    
    public static Plugin getInstance() {
        return BukkitReceive.instance;
    }
    
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        if (!channel.equals(BukkitReceive.channel)) {
            return;
        }
        String action = null;
        final ArrayList<String> received = new ArrayList<String>();
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            action = in.readUTF();
            while (in.available() > 0) {
                received.add(in.readUTF());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (action == null) {
            return;
        }
        if (action.equalsIgnoreCase("teleport")) {
            System.out.println("Packet: TP received");
            final Player from = Bukkit.getServer().getPlayer((String)received.get(0));
            final Player to = Bukkit.getServer().getPlayer((String)received.get(1));
            from.teleport((Entity)to);
        }
        if (action.equalsIgnoreCase("VanishAll")) {
            if (MCVersion.get().isInferior(MCVersion.v1_8)) {
                final String from2 = received.get(0);
                final String vansihed = received.get(1);
                final Player p = Bukkit.getPlayer(from2);
                if (vansihed.equals("oui")) {
                    Player[] onlinePlayers;
                    for (int length = (onlinePlayers = Bukkit.getServer().getOnlinePlayers()).length, i = 0; i < length; ++i) {
                        final Player target = onlinePlayers[i];
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1));
                        target.hidePlayer(p);
                    }
                }
                else {
                    Player[] onlinePlayers2;
                    for (int length2 = (onlinePlayers2 = Bukkit.getServer().getOnlinePlayers()).length, j = 0; j < length2; ++j) {
                        final Player target = onlinePlayers2[j];
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        target.showPlayer(p);
                    }
                }
            }
            else {
                final String from2 = received.get(0);
                final String vansihed = received.get(1);
                final Player p = Bukkit.getPlayer(from2);
                if (vansihed.equals("oui")) {
                    p.setGameMode(GameMode.CREATIVE);
                    Player[] onlinePlayers3;
                    for (int length3 = (onlinePlayers3 = Bukkit.getServer().getOnlinePlayers()).length, k = 0; k < length3; ++k) {
                        final Player target = onlinePlayers3[k];
                        target.hidePlayer(p);
                    }
                }
                else {
                    p.setGameMode(GameMode.SURVIVAL);
                    Player[] onlinePlayers4;
                    for (int length4 = (onlinePlayers4 = Bukkit.getServer().getOnlinePlayers()).length, l = 0; l < length4; ++l) {
                        final Player target = onlinePlayers4[l];
                        target.showPlayer(p);
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent e) {
        e.setJoinMessage((String)null);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
        e.setQuitMessage((String)null);
    }
}
