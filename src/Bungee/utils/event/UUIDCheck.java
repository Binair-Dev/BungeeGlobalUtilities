package Bungee.utils.event;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import Bungee.config.AliasesManager;
import Bungee.config.CommandManager;
import Bungee.config.ConfigManager;
import Bungee.config.PermissionsManager;
import Bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UUIDCheck extends Command
{
	private Main m;
	
    public UUIDCheck(Main m) 
    {
        super(CommandManager.uuid, "", AliasesManager.getAliases(AliasesManager.uuid));
        this.m = m;
    }
    
    @SuppressWarnings("deprecation")
	public void execute(final CommandSender sender, final String[] args)
    {
    	if(sender.hasPermission(PermissionsManager.uuid))
    	{
            if (args.length == 0) 
            {
                sender.sendMessage(ConfigManager.usage + ConfigManager.getMessages(m, "uuid", "usage"));
            }
            else 
            {
            	UUID offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + args[0]).getBytes(StandardCharsets.UTF_8));
            	String uuidWithoutDashes = offlineId.toString();
            	String uuidWithDashes = uuidWithoutDashes.replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
            	UUID uuid = UUID.fromString(uuidWithDashes);
            	sender.sendMessage(ConfigManager.getMessages(m, "uuid", "message").replace("%pseudo%", args[0].toString()).replace("-", "").replace("%uuid%", uuid.toString()));
            }
    	}
    	else
    	{
    	    sender.sendMessage(ConfigManager.getPermissionMessage(m));
    	}
    }
}
