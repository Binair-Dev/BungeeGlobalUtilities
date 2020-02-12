package Bungee.utils.event;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import java.util.concurrent.*;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import Bungee.teleport.bungee.BungeeSend;
import net.md_5.bungee.api.scheduler.*;

public class Gtphere extends Command
{    
	private Main m;
    public Gtphere(Main m)
    {
        super(CommandManager.gtphere, "", AliasesManager.getAliases(AliasesManager.gtphere));
        this.m = m;
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.gtphere))
    	{
    		if (!(sender instanceof ProxiedPlayer)) 
            {
                sender.sendMessage(ConfigManager.getMessages(m, "gtphere", "playeronly"));
                return;
            }
            if (args.length < 1) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "gtphere", "usage"));
                return;
            }
            if (args.length == 1) 
            {
            	final ProxiedPlayer from = ProxyServer.getInstance().getPlayer(args[0]);
            	
                if (from == null)
                {
                    sender.sendMessage(ConfigManager.getMessages(m, "gtphere", "notconnected").replace("%player%", args[0]));
                    return;
                }
                if (from == sender) 
                {
                    sender.sendMessage(ConfigManager.getMessages(m, "gtphere", "notyourself"));
                    return;
                }
                if (from != sender) 
                {
                    teleport(from, (ProxiedPlayer)sender);
                    sender.sendMessage(ConfigManager.getMessages(m, "gtphere", "toyou").replace("%player%", from.getName()));
                    return;
                }
                return;
            }
            final ProxiedPlayer from = (ProxiedPlayer)sender;
            final ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[0]);
            if (args[0] != null && to == null) {
                sender.sendMessage(ConfigManager.getMessages(m, "gtphere", "nothere"));
                return;
            }
            if (from == to) 
            {
                sender.sendMessage(ConfigManager.getMessages(m, "gtphere", "notyourself"));
                return;
            }
            else
            {
                teleport(from, to);
                from.sendMessage(ConfigManager.getMessages(m, "gtphere", "meto").replace("%player%", to.getName()));
            }
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
        
    }
    
    @SuppressWarnings("unused")
    public static void teleport(final ProxiedPlayer from, final ProxiedPlayer to) 
    {
        if (from.getServer().getInfo() != to.getServer().getInfo()) 
        {
            from.connect(to.getServer().getInfo());
        }
		final ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> BungeeSend.teleport(from, to), 1L, TimeUnit.SECONDS);
    }
}
