package Bungee.utils.event;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import Bungee.utils.Username;
import Bungee.utils.UsernameHistory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class NameCheck extends Command
{
	private Main m;
    public NameCheck(Main m) 
    {
        super(CommandManager.namecheck, "", AliasesManager.getAliases(AliasesManager.namecheck));
        this.m = m;
    }
    
    @SuppressWarnings({ "deprecation", "unused" })
	public void execute(final CommandSender sender, final String[] args) 
    {
    	if(sender.hasPermission(PermissionsManager.namecheck))
    	{
    		UUID offlineId = null;
            if (args.length == 0) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "namecheck", "usage"));
                return;
            }
            else
            {
            	offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[0]).getBytes(StandardCharsets.UTF_8));
            	sender.sendMessage(ConfigManager.information + ConfigManager.getMessages(m, "namecheck", "information").replace("%player%", args[0].toString()));
            	List<String> usernames = null;
				try 
				{
					usernames = getHistory(args[0]);
				} 
				catch (ParseException e) {e.printStackTrace();}
				
                if (usernames != null) 
                {
                	String propre = null;
                    sender.sendMessage(ConfigManager.getMessages(m, "namecheck", "historique").replace("%player%", args[0].toString()));
                    for (String s : usernames)
                    {
                    	propre = propre + ", " + s;
                    }
                    sender.sendMessage("§7" + propre.replace("null, ", ""));
                    return;
                }    
            }
            sender.sendMessage(ConfigManager.getMessages(m, "namecheck", "neverchanged"));
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
    
    
    public ArrayList<String> getHistory(String name) throws ParseException
    {
    	List<String> user = new ArrayList<String>();
    	
        final String username = name;
        UsernameHistory history = null;
        try 
        {
            try
            {
				history = new UsernameHistory(username);
			} 
            catch (org.json.simple.parser.ParseException e) 
            {
				e.printStackTrace();
			}
        }
        catch (IOException e) 
        {
        	user.add("An error occurred while trying to connect to Mojang's servers: " + e.getLocalizedMessage());
            return (ArrayList<String>) user;
        }
        for (final Username usr : history.getHistory()) 
        {
        	user.add(usr.getName());
        }
		return (ArrayList<String>)user;
    }
}
