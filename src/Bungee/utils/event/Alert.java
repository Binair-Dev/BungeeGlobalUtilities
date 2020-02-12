package Bungee.utils.event;

import net.md_5.bungee.api.plugin.*;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;

public class Alert extends Command
{
	private Main m;
    public Alert(Main m) 
    {
        super(CommandManager.alert, "", AliasesManager.getAliases(AliasesManager.alert));
        this.m = m;
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args)
    {
    	if(sender.hasPermission(PermissionsManager.alert))
    	{
            if (args.length == 0) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "alert", "usage"));
            }
            else 
            {
                final StringBuilder builder = new StringBuilder();
                final String[] arrayOfString = args;
                for (int j = args.length, i = 0; i < j; ++i) 
                {
                    final String s = arrayOfString[i];
                    builder.append(ChatColor.translateAlternateColorCodes('&', s));
                    builder.append(" ");
                }
                final String message = builder.substring(0, builder.length() - 1);
                for (final ProxiedPlayer p1 : ProxyServer.getInstance().getPlayers()) 
                {
                    p1.sendMessage(ConfigManager.getMessages(m, "alert", "prefix") + message);
                }
            }
    	}
    	else
    	{
    		sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
}
