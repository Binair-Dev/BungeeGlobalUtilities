package Bungee.utils.event;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Lookup extends Command
{
	private Main m;
    public Lookup(Main m) 
    {
        super(CommandManager.lookup, "", AliasesManager.getAliases(AliasesManager.lookup));
        this.m = m;
    }
    
    @SuppressWarnings({ "deprecation", "static-access" })
	public void execute(final CommandSender sender, final String[] args)
    {
    	if(sender.hasPermission(PermissionsManager.lookup))
    	{
    		if (args.length == 0) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "lookup", "usage"));
            }
            else 
            {
        		UUID offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[0].toString()).getBytes(StandardCharsets.UTF_8));

            	if(m.data.getSection("Lookup").contains("" + offlineId))
            	{
                	ProxiedPlayer player = null;
                	if(ProxyServer.getInstance().getPlayer(args[0].toString()) != null)
                	{
                    	player = ProxyServer.getInstance().getPlayer(args[0].toString());
                	}
                	
            		        		
                	String heure = formatter(m.data.getInt("Lookup." + offlineId + ".Time"));

                	if(ProxyServer.getInstance().getPlayers().contains(player))
                	{
                            sender.sendMessage(ConfigManager.getMessages(m, "lookup", "online"));
                	}
                	else
                	{
                        sender.sendMessage(ConfigManager.getMessages(m, "lookup", "offline"));
                	}
                    sender.sendMessage(ConfigManager.getMessages(m, "lookup", "info"));
                    
                    sender.sendMessage(ConfigManager.getMessages(m, "lookup", "premiere").replace("%first%", m.data.getString("Lookup." + offlineId + ".First")));
                    sender.sendMessage(ConfigManager.getMessages(m, "lookup", "derniere").replace("%last%", m.data.getString("Lookup." + offlineId + ".Last")));
                    sender.sendMessage(ConfigManager.getMessages(m, "lookup", "hour").replace("%heure%", heure));
            	}
            	else
            	{
                    sender.sendMessage(ConfigManager.getMessages(m, "lookup", "neverseen"));
            	}
            }
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
    
    private String formatter(int secondtTime)
    {
    	  secondtTime = secondtTime * 60;
	      TimeZone tz = TimeZone.getTimeZone("UTC");
	      SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	      df.setTimeZone(tz);
	      String time = df.format(new Date(secondtTime*1000L));
	      return time;
    }
}
