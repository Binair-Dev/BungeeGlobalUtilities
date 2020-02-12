package Bungee.utils.event;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;

public class Find extends Command
{
	private Main m;
    public Find(Main m) 
    {
        super(CommandManager.find, "", AliasesManager.getAliases(AliasesManager.find));
        this.m = m;
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.find))
    	{
            if (args.length < 1) 
            {
            	sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "find", "usage"));
            	return;
            }
            for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) 
            {
                if (p.getName().equalsIgnoreCase(args[0])) 
                {
                	sender.sendMessage(ConfigManager.getMessages(m, "find", "finded").replace("%player%", p.getName()).replaceAll("%server%", p.getServer().getInfo().getName()));
                    return;
                }
            }
        	sender.sendMessage(ConfigManager.getMessages(m, "find", "notonline"));
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}

    }
}
