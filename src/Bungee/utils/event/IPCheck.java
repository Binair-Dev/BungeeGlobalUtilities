package Bungee.utils.event;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;

public class IPCheck extends Command
{
	private Main m;
    public IPCheck(Main m) 
    {
        super(CommandManager.ipcheck, "", AliasesManager.getAliases(AliasesManager.ipcheck));
    	this.m = m;
    }
    
    @SuppressWarnings({ "deprecation", "static-access" })
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.ipcheck))
    	{
    		if (args.length == 0) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "ipcheck", "usage"));
                return;
            }
            else 
            {
            	ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0].toString());
            	if(ProxyServer.getInstance().getPlayers().contains(p))
            	{
                	String ip = p.getAddress().getAddress().toString().replace("/", "");
                    sender.sendMessage(ConfigManager.getMessages(m, "ipcheck", "ipmessage").replace("%player%", args[0].toString()).replace("%ip%", ip));
                    return;
            	}
            	else
            	{
            		sender.sendMessage(ConfigManager.getMessages(m, "ipcheck", "nothere"));
            	}

            }

    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
}
