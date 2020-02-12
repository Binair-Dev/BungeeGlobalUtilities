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

public class Gtp extends Command
{    
	private Main m;
    public Gtp(Main m)
    {
        super(CommandManager.gtp, "", AliasesManager.getAliases(AliasesManager.gtp));
        this.m = m;
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.gtp))
    	{
    		if (!(sender instanceof ProxiedPlayer)) 
            {
                sender.sendMessage(ConfigManager.getMessages(m, "gtp", "playeronly"));
                return;
            }
            if (args.length < 1) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "gtp", "usage"));
                return;
            }
            if (args.length != 1) 
            {
                if (args.length == 2) 
                {
                    final ProxiedPlayer from = ProxyServer.getInstance().getPlayer(args[0]);
                    final ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[1]);
                    if (from == null)
                    {
                        sender.sendMessage(ConfigManager.getMessages(m, "gtp", "notconnected").replace("%player%", args[0]));
                        return;
                    }
                    if (to == null) 
                    {
                        sender.sendMessage(ConfigManager.getMessages(m, "gtp", "notconnected").replace("%player%", args[1]));
                        return;
                    }
                    if (from == to) 
                    {
                        sender.sendMessage(ConfigManager.getMessages(m, "gtp", "notyourself"));
                        return;
                    }
                    if (to == sender) 
                    {
                    	if(sender.hasPermission(PermissionsManager.gtphere))
                    	{
                            teleport(from, to);
                            sender.sendMessage(ConfigManager.getMessages(m, "gtp", "toyou").replace("%player%", from.getName()));
                            return;
                    	}
                    	else
                    	{
                    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
                    	}
                    }
                    if (to != sender) 
                    {
                        teleport(from, to);
                        sender.sendMessage(ConfigManager.getMessages(m, "gtp", "tosomeone").replace("%from%", from.getName()).replace("%to%", to.getName()));
                        return;
                    }
                }
                return;
            }
            final ProxiedPlayer from = (ProxiedPlayer)sender;
            final ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[0]);
            if (args[0] != null && to == null) {
                from.sendMessage(ConfigManager.getMessages(m, "gtp", "nothere"));
                return;
            }
            if (from == to) 
            {
                sender.sendMessage(ConfigManager.getMessages(m, "gtp", "notyourself"));
                return;
            }
            else
            {
                teleport(from, to);
                from.sendMessage(ConfigManager.getMessages(m, "gtp", "meto").replace("%player%", to.getName()));
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
