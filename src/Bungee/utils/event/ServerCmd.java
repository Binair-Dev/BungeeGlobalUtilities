package Bungee.utils.event;

import java.util.ArrayList;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCmd extends Command
{
	private Main m;
    public ServerCmd(Main m) 
    {
        super(CommandManager.server, "", AliasesManager.getAliases(AliasesManager.server));
        this.m = m;
    }
    
    @SuppressWarnings({ "deprecation", "unused" })
	public void execute(final CommandSender sender, final String[] args)
    {
    	if (args.length == 0) 
        {
            sender.sendMessage(ConfigManager.getMessages(m, "server", "availlable"));

            ArrayList<String> joinable = new ArrayList<String>();
        	String propre = null;

            for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
            {
            	if(sender.hasPermission(PermissionsManager.server + "." + serverInfo.getName()))
            	{
                    joinable.add(serverInfo.getName());
                    String join = joinable.toString();
            	}
            } 
            for (String s : joinable)
            {
            	propre = propre + ", " + s;
            }
            if(propre == null)
            {
            	propre = "Aucun";
                sender.sendMessage("§b" + propre.replace("null, ", "") + "§7.");
            }
            else
            {
                sender.sendMessage("§b" + propre.replace("null, ", "") + "§7.");
            }
        }
        else 
        {
        	ProxiedPlayer p = (ProxiedPlayer)sender;
        	if(ProxyServer.getInstance().getServers().containsKey(args[0].toString()))
        	{
            	if (p.getServer().getInfo().getName().equalsIgnoreCase(args[0].toString())) 
            	{
                    p.sendMessage(ConfigManager.getMessages(m, "server", "dejaon"));
            	}
            	else
            	{
            		ServerInfo target = ProxyServer.getInstance().getServerInfo(args[0].toString());

            		if(sender.hasPermission(PermissionsManager.server + "." + target.getName()))
                	{
                        p.sendMessage(ConfigManager.getMessages(m, "server", "sendedto").replace("%server%", args[0].toString()));
                		p.connect(target);
                	}
            		else
            		{
                        p.sendMessage(ConfigManager.getMessages(m, "server", "cantgoon").replace("%server%", target.getName()));
            		}
            	}
        	}
        	else
        	{
                p.sendMessage(ConfigManager.getMessages(m, "server", "notexist"));
        	}
        }
    }
}
