package Bungee.utils.event;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Collection;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;

public class Glist extends Command
{
	private Main m;
    public Glist(Main m) 
    {
        super(CommandManager.glist, "", AliasesManager.getAliases(AliasesManager.glist));
        this.m = m;
    }
    
    @SuppressWarnings({ "deprecation", "unused" })
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.glist))
    	{
    		if (args.length < 1) 
            {
            	sender.sendMessage(ConfigManager.getMessages(m, "glist", "global").replace("%global%", "" + m.getProxy().getServers().size()));
            	int allSlots = 0;
                for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                {
                	int maxco = maxSlots(serverInfo.getAddress().getAddress().getHostAddress(), serverInfo.getAddress().getPort());
                	allSlots = 0 + maxco;
                } 
                for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                {
                	String nom = serverInfo.getName();
                	int co = serverInfo.getPlayers().size();
                	int maxco = maxSlots(serverInfo.getAddress().getAddress().getHostAddress(), serverInfo.getAddress().getPort());
                	String pourcent = pourcentage(co, ProxyServer.getInstance().getPlayers().size());
                	sender.sendMessage(ConfigManager.getMessages(m, "glist", "noargs").replace("%name%", nom).replace("%co%", "" + co).replace("%pourcent%", "" + pourcent));
                } 
                return;
            }
            else
            {
                for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) 
                {
                	if(args[0].toString() != null)
                	{
                		if(serverExist(args[0].toString()))
                		{
                    		if(serverInfo.getName().equalsIgnoreCase(args[0].toString()))
                    		{
                    			Collection<ProxiedPlayer> joueurs = serverInfo.getPlayers();
                    			String players = null;
                    			if(joueurs.size() != 0)
                    			{
                                	for (ProxiedPlayer player : serverInfo.getPlayers()) 
                                    {
                                		players = players + player + ", ";
                                    }
                                	sender.sendMessage(ConfigManager.getMessages(m, "glist", "listof").replace("%name%", serverInfo.getName()));
                                	sender.sendMessage("§7" + players.replace("null", ""));
                                	return;
                    			}
                    			else
                    			{
                                	sender.sendMessage(ConfigManager.getMessages(m, "glist", "none"));
                                	return;
                    			}

                    		}
                		}
                		else
                		{
                        	sender.sendMessage(ConfigManager.getMessages(m, "glist", "notexist"));

                        	return;
                		}
                	}

                } 
                return;
            }
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
    
    @SuppressWarnings({ "unused", "resource" })
	public int maxSlots(String ip, int port) 
    {
    	int max = 0;
        try 
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1 * 1000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out.write(0xFE);
            StringBuilder str = new StringBuilder();
            int b;
            while ((b = in.read()) != -1) 
            {
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) 
                    {
                            str.append((char) b);
                    }
            }
            String[] data = str.toString().split("§");
			String motd = data[0];
            int onlinePlayers = Integer.valueOf(data[1]);
            int maxPlayers = Integer.valueOf(data[2]);
            max = maxPlayers;      
        } 
        catch (Exception e) 
        {
                e.printStackTrace();
        }
		return max;
    }
    
    @SuppressWarnings("deprecation")
	public boolean serverExist(String server)
    {
    	if(ProxyServer.getInstance().getServers().containsKey(server))
    	{
    		return true;
    	}
    	else
    	{
    	    return false;
    	}
    }
    
    private static String pourcentage(int a,int b){
        double c = new Double(b);
        double resultat = a/c;
        double resultatFinal = resultat*100;
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(resultatFinal) + " %";
    }
}
