package Bungee.listeners;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import Bungee.config.ConfigManager;
import Bungee.main.Main;
import Bungee.teleport.bungee.BungeeSend;
import Bungee.utils.Aliases;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.PlayerListItem;

public class GlobalUtilitiesListener implements Listener
{
	private Main m;
    public static List<Aliases> aliases;
    
	public GlobalUtilitiesListener(Main m) 
	{
		this.m = m;
	}
	@SuppressWarnings("static-access")
	@EventHandler
    public void onJoin(PostLoginEvent e)
    {
    	//Lookup
		UUID offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + e.getPlayer().getName()).getBytes(StandardCharsets.UTF_8));
    	
    	if(m.data.getString("Lookup." + offlineId + ".First").isEmpty())
    	{
    		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
    		Date date = new Date();
    		System.out.println(dateFormat.format(date));
    		m.getConfig().update("Lookup." + offlineId, "First" , dateFormat.format(date));
    	}
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
    	Date date = new Date();
    	System.out.println(dateFormat.format(date));
    	m.getConfig().update("Lookup." + offlineId, "Last" , dateFormat.format(date));
    }
	
    
    @SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler(priority = 65)
    public void onChatEvent(final ChatEvent event) 
    {
    	if (event.getMessage().contains("gamemode") || event.getMessage().contains("gm") || event.getMessage().contains("gmc") || event.getMessage().contains("gms")) 
        {
            final ProxiedPlayer proxiedPlayer = (ProxiedPlayer)event.getSender();
    		if(m.vanished.contains(proxiedPlayer.getName()))
    		{
    			event.setCancelled(true);
    		}
        }
        if (event.isCancelled()) 
        {
            return;
        }
        for (final Aliases alias : this.aliases) 
        {
            if (alias.alias.equalsIgnoreCase(event.getMessage())) 
            {
                final ProxyServer proxyServer = m.getProxy();
                final ServerInfo serverInfo = proxyServer.getServerInfo(alias.server);
                if (serverInfo == null) 
                {
                    return;
                }
                final ProxiedPlayer proxiedPlayer = (ProxiedPlayer)event.getSender();
                if (alias.permission != null && !proxiedPlayer.hasPermission(alias.permission)) 
                {
                    
                    proxiedPlayer.sendMessage(ConfigManager.getMessages(m, "server", "cancel"));
                    if (alias.forward) 
                    {
                        continue;
                    }
                    event.setCancelled(true);
                }
                else if (alias.server.equalsIgnoreCase(proxiedPlayer.getServer().getInfo().getName())) 
                {
                    proxiedPlayer.sendMessage(ConfigManager.getMessages(m, "server", "dejaon"));
                    if (alias.forward) 
                    {
                        continue;
                    }
                    event.setCancelled(true);
                }
                else 
                {
                    proxiedPlayer.sendMessage(ConfigManager.getMessages(m, "server", "sendedto").replace("%server%", alias.server));
                    if (!event.isCancelled()) 
                    {
                        event.setCancelled(true);
                    }
                    proxiedPlayer.connect(serverInfo);
                }
            }
        }
    }
    
    
    @EventHandler
    public void onDisconnect(ServerDisconnectEvent e)
    {
    	ServerInfo server = e.getPlayer().getServer().getInfo();
    	ServerInfo target = e.getTarget();
    	
    	if(!m.vanished.contains(e.getPlayer().getName()))
    	{
    		if(server != target)
    		{
        		for(ProxiedPlayer p : server.getPlayers())
        		{
        	        p.sendMessage(ConfigManager.join.replace("%player%", e.getPlayer().getName()));
        		}
        		for(ProxiedPlayer p : target.getPlayers())
        		{
        	        p.sendMessage(ConfigManager.leave.replace("%player%", e.getPlayer().getName()));
        		}
    		}
    		else
    		{
        		for(ProxiedPlayer p : target.getPlayers())
        		{
        	        p.sendMessage(ConfigManager.leave.replace("%player%", e.getPlayer().getName()));
        		}
    		}
    	}
    }
    
    @EventHandler
    public void onConnect(ServerConnectEvent e)
    {
    	if(m.vanished.contains(e.getPlayer().getName()))
    	{
        	final ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> BungeeSend.vanishToAllServer(e.getPlayer().getName(), "oui"), 1L, TimeUnit.SECONDS);
    	}
    	else
    	{
    		for(String pseudos : m.vanished)
    		{
            	final ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> BungeeSend.vanishToAllServer(pseudos, "oui"), 1L, TimeUnit.SECONDS);
    		}
        	final ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> BungeeSend.vanishToAllServer(e.getPlayer().getName(), "non"), 1L, TimeUnit.SECONDS);
        	if(e.getReason() == Reason.JOIN_PROXY)
        	{
            	ServerInfo server = e.getTarget();
            	if(!m.vanished.contains(e.getPlayer().getName()))
            	{
            		for(ProxiedPlayer p : server.getPlayers())
            		{
            	        p.sendMessage(ConfigManager.join.replace("%player%", e.getPlayer().getName()));
            		}
            	}
        	}
    	}
    }
}
