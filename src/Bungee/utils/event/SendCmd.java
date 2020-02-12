package Bungee.utils.event;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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

public class SendCmd extends Command
{
	private Main m;
    public SendCmd(Main m) 
    {
        super(CommandManager.send, "", AliasesManager.getAliases(AliasesManager.send));
        this.m = m;
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args)
    {
    	if(sender.hasPermission(PermissionsManager.send))
    	{
    		if (args.length == 0) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "send", "usage"));
            }
            else 
            {
            	if(args[0].equalsIgnoreCase("all"))
            	{

            		if(args.length == 2)
                	{
            			if(sender.hasPermission(PermissionsManager.server + "." + args[1].toString()))
            			{
            				if(serverExist(args[1].toString()))
                			{
                    			ServerInfo target = ProxyServer.getInstance().getServerInfo(args[1].toString());
                    			String ip = target.getAddress().getAddress().getHostAddress();
                    			int port = target.getAddress().getPort();
                    			
                            	if(isServerOnline(ip, port))
                            	{
                            		if(!isEveryoneHere(args[1].toString()))
                            		{
                                        for (final ProxiedPlayer p1 : ProxyServer.getInstance().getPlayers()) 
                                        {
                                            send(p1.getName(), args[1].toString());
                                        }
                                        sender.sendMessage(ConfigManager.getMessages(m, "send", "allto").replace("%server%", args[1].toString()));
                            		}
                                	else
                                	{
                                	    sender.sendMessage(ConfigManager.getMessages(m, "send", "alreadyallhere"));
                                	}
                            	}
                            	else
                            	{
                            	    sender.sendMessage(ConfigManager.getMessages(m, "send", "notexist"));
                            	}
                			}
                        	else
                        	{
                        	    sender.sendMessage(ConfigManager.getMessages(m, "send", "notonline"));
                        	}
            			}
            			else
            			{
            	    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
            			}
                	}
                	else
                	{
                	    sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "send", "sendallusage"));
                	}
            	}
            	
            	else if(args[0].equalsIgnoreCase("current"))
            	{
            		if(args.length == 2)
                	{
                    	if(serverExist(args[1].toString()))
            			{
                			if(sender.hasPermission(PermissionsManager.server + "." + args[1].toString()))
                			{
                				if(!isEveryoneHere(args[1].toString()))
                        		{
                            		ServerInfo target = ((ProxiedPlayer) sender).getServer().getInfo();

                                    for (final ProxiedPlayer p1 : target.getPlayers()) 
                                    {
                                    	if(!p1.getServer().getInfo().getName().equalsIgnoreCase(args[1].toString()))
                                    	{
                                            send(p1.getName(), args[1].toString());
                                    	}
                                    }
                                    sender.sendMessage(ConfigManager.getMessages(m, "send", "allto").replace("%server%", args[1].toString()));

                        		}
                            	else
                            	{
                            	    sender.sendMessage(ConfigManager.getMessages(m, "send", "alreadyallhere"));
                            	}
                			}
                			else
                			{
                	    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
                			}
                    		
                    	}
                    	else
                    	{
                    	    sender.sendMessage(ConfigManager.getMessages(m, "send", "notexist"));
                    	}
                	}
                	else
                	{
                	    sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "send", "sendcurrentusage"));
                	}
            	}
            	else if(args[0] != null && !args[0].equalsIgnoreCase("all") && !args[0].equalsIgnoreCase("current"))
            	{
            		if(args.length == 2)
                	{
            			if(sender.hasPermission(PermissionsManager.server + "." + args[1].toString()))
            			{
                        	if(serverExist(args[1].toString()))
                        	{
                        		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0].toString());
                        		if(p.isConnected())
                        		{
                        			ServerInfo target = ProxyServer.getInstance().getServerInfo(args[1].toString());
                        			
                        			if(p.getServer().getInfo() != target)
                        			{
                                        send(p.getName(), args[1].toString());
                                	    sender.sendMessage(ConfigManager.getMessages(m, "send", "sendsomeone").replace("%target%", p.getName()).replace("%server%", args[1].toString()));
                        			}
                        			else
                        			{
                                	    sender.sendMessage(ConfigManager.getMessages(m, "send", "alreadyonthisserver"));
                        			}
                        		}
                        		else
                        		{
                            	    sender.sendMessage(ConfigManager.getMessages(m, "send", "nothere"));
                        		}
                        	}
                        	else
                        	{
                        	    sender.sendMessage(ConfigManager.getMessages(m, "send", "notexist"));
                        	}
            			}
            			else
            			{
            	    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
            			}
                	}
                	else
                	{
                	    sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "send", "sendelse"));
                	}
            	}
            }
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
    
    @SuppressWarnings("deprecation")
	public void send(String pseudo, String info)
    {
    	ProxiedPlayer p = ProxyServer.getInstance().getPlayer(pseudo);
	    p.sendMessage(ConfigManager.getMessages(m, "send", "sendedto").replace("%server%", info));
		ServerInfo target = ProxyServer.getInstance().getServerInfo(info);
		p.connect(target);
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
    
    public boolean isServerOnline(String ip, int port)
    {
        boolean tor = true;
        Socket socket = new Socket();
        try
        {
            socket.connect(new InetSocketAddress(ip, port), (2 * 1000));
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
            tor = ((str != null && data != null && data.length == 3) ? true : false);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            tor = false;
            
            try 
            {
                socket.close();
            } 
            catch (IOException e) 
            {
            }
        }
        return tor;
    }
    
    public boolean isEveryoneHere(String server)
    {
		ServerInfo target = ProxyServer.getInstance().getServerInfo(server);

    	if(ProxyServer.getInstance().getPlayers().size() == target.getPlayers().size())
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
}
