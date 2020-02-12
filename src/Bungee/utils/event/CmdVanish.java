package Bungee.utils.event;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.ServerInfo;
import java.util.concurrent.*;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import Bungee.teleport.bungee.BungeeSend;
import net.md_5.bungee.api.scheduler.*;

public class CmdVanish extends Command
{    
	private Main m;
    public CmdVanish(Main m)
    {
        super(CommandManager.vanish, "", AliasesManager.getAliases(AliasesManager.vanish));
        this.m = m;
    }
    
    @SuppressWarnings({ "deprecation", "static-access" })
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.vanish))
    	{
    		if (!(sender instanceof ProxiedPlayer)) 
            {
                sender.sendMessage(ConfigManager.getMessages(m, "vanish", "onlyplayer"));
                return;
            }
            if (args.length < 1) 
            {
            	sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "vanish", "usage"));
                return;
            }
            if (args.length == 1) 
            {
            	final ProxiedPlayer from = ProxyServer.getInstance().getPlayer(args[0]);
            	if(args[0].equalsIgnoreCase("list"))
            	{
                	if(sender.hasPermission(ConfigManager.getMessages(m, "vanish", "permissionsee")))
            		{
                    	String propre = null;
                        for (String s : ConfigManager.getVanishList(m))
                        {
                        	propre = propre + ", " + s;
                        }
                        if(propre != null)
                        {
                            sender.sendMessage(ConfigManager.getMessages(m, "vanish", "list").replace("-", ":"));
                            sender.sendMessage(ConfigManager.getMessages(m, "vanish", "enum") + propre.replace("null, ", ""));
                        }
                        else
                        {
                            sender.sendMessage(ConfigManager.getMessages(m, "vanish", "nooneisvanished"));
                        }
            			return;
            		}
            	}
                if (from == null)
                {
                    sender.sendMessage(ConfigManager.getMessages(m, "vanish", "notfound"));
                    return;
                }
                if (from == sender)
                {
                	if(from.hasPermission(PermissionsManager.vanish + "." + from.getServer().getInfo().getName()) || from.hasPermission(PermissionsManager.vanish + ".*"))
                	{
                        if(m.vanished.contains(from.getName()))
                        {
                            vanishall(from, "non");
                    		for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                            {
                    			for(ProxiedPlayer p : serverInfo.getPlayers())
                    			{
                    				if(p.getName() != sender.getName())
                    				{
                                    	if(p.hasPermission(ConfigManager.getMessages(m, "vanish", "permissionsee")))
                    					{
                                            p.sendMessage(ConfigManager.getMessages(m, "vanish", "remdmyvanish").replace("%player%", sender.getName()));
                                        	
                    					}
                    				}
                    			}
                    		}
                    		m.vanished.remove(from.getName());
                        	ConfigManager.saveVanished(m, m.vanished);
                        }
                        else
                        {
                            vanishall(from, "oui");
                    		for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                            {
                    			for(ProxiedPlayer p : serverInfo.getPlayers())
                    			{
                    				if(p.getName() != sender.getName())
                    				{
                                    	if(p.hasPermission(ConfigManager.getMessages(m, "vanish", "permissionsee")))
                    					{
                                            p.sendMessage(ConfigManager.getMessages(m, "vanish", "addmyvanish").replace("%player%", sender.getName()));

                    					}
                    				}
                    			}
                    		}
                        	m.vanished.add(from.getName());
                        	ConfigManager.saveVanished(m, m.vanished);
                        }
                        return;
                	}
                	else
                	{
                		sender.sendMessage(ConfigManager.getPermissionMessage(m));
                	}
                }
                else
                {
                	if(from.hasPermission(PermissionsManager.vanish + "." + from.getServer().getInfo().getName()) || from.hasPermission(PermissionsManager.vanish + ".*"))
                	{
                    	if(sender.hasPermission(ConfigManager.getMessages(m, "vanish", "permissionother")))
                    	{
                            if(m.vanished.contains(from.getName()))
                            {
                                vanishall(from, "non");

                        		for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                                {
                        			for(ProxiedPlayer p : serverInfo.getPlayers())
                        			{
                        				if(p.getName() != sender.getName())
                        				{
                                        	if(p.hasPermission(ConfigManager.getMessages(m, "vanish", "permissionsee")))
                        					{
                                                p.sendMessage(ConfigManager.getMessages(m, "vanish", "remvanishother").replace("%sender%", sender.getName()).replace("%target%", from.getName()));

                        					}
                        				}
                        			}
                        		}
                            	m.vanished.remove(from.getName());
                            	ConfigManager.saveVanished(m, m.vanished);
                            	
                            }
                            else
                            {
                                vanishall(from, "oui");
                        		for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                                {
                        			for(ProxiedPlayer p : serverInfo.getPlayers())
                        			{
                        				if(p.getName() != sender.getName())
                        				{
                                        	if(p.hasPermission(ConfigManager.getMessages(m, "vanish", "permissionsee")))
                        					{
                                                p.sendMessage(ConfigManager.getMessages(m, "vanish", "addvanishother").replace("%sender%", sender.getName()).replace("%target%", from.getName()));

                        					}
                        				}
                        			}
                        		}
                            	m.vanished.add(from.getName());
                            	ConfigManager.saveVanished(m, m.vanished);
                            }
                            return;
                    	}
                    	else
                    	{
                    		sender.sendMessage(ConfigManager.getPermissionMessage(m));
                    	}
                	}
                }
            }
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
        
    }
    
    
    @SuppressWarnings("unused")
	public static void vanishall(final ProxiedPlayer from, String state)
    {
        	final ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> BungeeSend.vanishToAllServer(from.getName(), state), 1L, TimeUnit.SECONDS);
    }
}
