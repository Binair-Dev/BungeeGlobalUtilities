package Bungee.config;

import java.nio.file.*;
import java.util.List;
import Bungee.main.Main;
import java.io.*;
import net.md_5.bungee.config.*;

public class ConfigManager
{    
	private Main m;
	public ConfigManager(Main m)
	{
		this.m = m;
	}
    public static String usage;
    public static String information;
    public static String perm;
    public static String join;
    public static String leave;
    
    public void createDefaultConfigFile()
    {
        try 
        {
            if (!Main.getInstance().getDataFolder().exists()) 
            {
                Main.getInstance().getDataFolder().mkdir();
            }
            final File f = new File(Main.getInstance().getDataFolder(), "config.yml");
            if (!f.exists()) 
            {
                Files.copy(Main.getInstance().getResourceAsStream("config.yml"), f.toPath(), new CopyOption[0]);
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void createCustomConfigFile(String name)
    {
        try 
        {
            if (!Main.getInstance().getDataFolder().exists()) 
            {
                Main.getInstance().getDataFolder().mkdir();
            }
            final File f = new File(Main.getInstance().getDataFolder(), "data.yml");
            if (!f.exists()) 
            {
                Files.copy(Main.getInstance().getResourceAsStream("data.yml"), f.toPath(), new CopyOption[0]);
            }
        }
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    @SuppressWarnings("static-access")
	public void update(String cat, String player, String donnee)
    {
        m.data.set(cat+ "." + player, (Object)donnee); 
        try 
        {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.data, new File(m.getDataFolder(), "data.yml"));
		} catch (IOException e) {e.printStackTrace();}
    } 
    
    @SuppressWarnings("static-access")
    public void updateTime(String cat, String player, int donnee)
    {
        m.data.set(cat+ "." + player, (Object)donnee); 
        try 
        {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.data, new File(m.getDataFolder(), "data.yml"));
		} catch (IOException e) {e.printStackTrace();}
    } 
    
    @SuppressWarnings("static-access")
    public void loadDefaultMessages()
    {
    	usage = m.defaultConfig.getString("Configuration.usage").replaceAll("&", "§");
    	information = m.defaultConfig.getString("Configuration.information").replaceAll("&", "§");
    	perm = m.defaultConfig.getString("Configuration.perm").replaceAll("&", "§");
    	join = m.defaultConfig.getString("Configuration.join").replaceAll("&", "§");
    	leave = m.defaultConfig.getString("Configuration.leave").replaceAll("&", "§");
    }
    
    @SuppressWarnings({ "unchecked", "static-access" })
	public void loadDefaultCommand()
    {
    	//alert
    	CommandManager.alert = m.defaultConfig.getString("Commandes.alert.commande");
    	AliasesManager.alert = (List<String>) m.defaultConfig.getList("Commandes.alert.aliases");
    	PermissionsManager.alert = m.defaultConfig.getString("Commandes.alert.permission");
    	//build
    	CommandManager.build = m.defaultConfig.getString("Commandes.build.commande");
    	AliasesManager.build = (List<String>) m.defaultConfig.getList("Commandes.build.aliases");
    	PermissionsManager.build = m.defaultConfig.getString("Commandes.build.permission");
    	//find
    	CommandManager.find = m.defaultConfig.getString("Commandes.find.commande");
    	AliasesManager.find = (List<String>) m.defaultConfig.getList("Commandes.find.aliases");
    	PermissionsManager.find = m.defaultConfig.getString("Commandes.find.permission");
    	//glist
    	CommandManager.glist = m.defaultConfig.getString("Commandes.glist.commande");
    	AliasesManager.glist = (List<String>) m.defaultConfig.getList("Commandes.glist.aliases");
    	PermissionsManager.glist = m.defaultConfig.getString("Commandes.glist.permission");
    	//gtp
    	CommandManager.gtp = m.defaultConfig.getString("Commandes.gtp.commande");
    	AliasesManager.gtp = (List<String>) m.defaultConfig.getList("Commandes.gtp.aliases");
    	PermissionsManager.gtp = m.defaultConfig.getString("Commandes.gtFactionConfig.permission");
    	//gtphere
    	CommandManager.gtphere = m.defaultConfig.getString("Commandes.gtphere.commande");
    	AliasesManager.gtphere = (List<String>) m.defaultConfig.getList("Commandes.gtphere.aliases");
    	PermissionsManager.gtphere = m.defaultConfig.getString("Commandes.gtphere.permission");
    	//ipcheck
    	CommandManager.ipcheck = m.defaultConfig.getString("Commandes.ipcheck.commande");
    	AliasesManager.ipcheck = (List<String>) m.defaultConfig.getList("Commandes.ipcheck.aliases");
    	PermissionsManager.ipcheck = m.defaultConfig.getString("Commandes.ipcheck.permission");
    	//ipcheck
    	CommandManager.lookup = m.defaultConfig.getString("Commandes.lookup.commande");
    	AliasesManager.lookup = (List<String>) m.defaultConfig.getList("Commandes.lookup.aliases");
    	PermissionsManager.lookup = m.defaultConfig.getString("Commandes.lookuFactionConfig.permission");
    	//namecheck
    	CommandManager.namecheck = m.defaultConfig.getString("Commandes.namecheck.commande");
    	AliasesManager.namecheck = (List<String>) m.defaultConfig.getList("Commandes.namecheck.aliases");
    	PermissionsManager.namecheck = m.defaultConfig.getString("Commandes.namecheck.permission");
    	//namecheck
    	CommandManager.semirp = m.defaultConfig.getString("Commandes.semirp.commande");
    	AliasesManager.semirp = (List<String>) m.defaultConfig.getList("Commandes.semirp.aliases");
    	PermissionsManager.semirp = m.defaultConfig.getString("Commandes.semirFactionConfig.permission");
    	//send
    	CommandManager.send = m.defaultConfig.getString("Commandes.send.commande");
    	AliasesManager.send = (List<String>) m.defaultConfig.getList("Commandes.send.aliases");
    	PermissionsManager.send = m.defaultConfig.getString("Commandes.send.permission");
    	//server
    	CommandManager.server = m.defaultConfig.getString("Commandes.server.commande");
    	AliasesManager.server = (List<String>) m.defaultConfig.getList("Commandes.server.aliases");
    	PermissionsManager.server = m.defaultConfig.getString("Commandes.server.permission");
    	//server
    	CommandManager.uuid = m.defaultConfig.getString("Commandes.uuid.commande");
    	AliasesManager.uuid = (List<String>) m.defaultConfig.getList("Commandes.uuid.aliases");
    	PermissionsManager.uuid = m.defaultConfig.getString("Commandes.uuid.permission");
    	//vanish
    	CommandManager.vanish = m.defaultConfig.getString("Commandes.vanish.commande");
    	AliasesManager.vanish = (List<String>) m.defaultConfig.getList("Commandes.vanish.aliases");
    	PermissionsManager.vanish = m.defaultConfig.getString("Commandes.vanish.permission");
    	PermissionsManager.vanishall = m.defaultConfig.getString("Commandes.vanish.permissionall");
    }
    
    @SuppressWarnings("static-access")
	public static String getMessages(Main m, String commande, String info)
    {
    	if(m.defaultConfig.getString("Commandes." + commande + "." + info) == null)
    	{
    		return "Aucune configuration existante!";
    	}
    	else
    	{
        	return m.defaultConfig.getString("Commandes." + commande + "." + info).replaceAll("&", "§");
    	}
    }
    
    @SuppressWarnings("static-access")
    public static String getPermissionMessage(Main m)
    {
    	return m.defaultConfig.getString("Configuration.perm").replace("Z", "§");
    }
    
    @SuppressWarnings("static-access")
    public static void saveVanished(Main m, List<String> list)
    {
    	if(!m.vanished.isEmpty())
    	{
        	m.data.set("Vanished", list);
            try 
            {
    			ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.data, new File(m.getDataFolder(), "data.yml"));
    		} catch (IOException e) {e.printStackTrace();}
    	}
    }
    
    @SuppressWarnings({ "static-access", "unchecked", "unused" })
    public static void loadVanished(Main m)
    {
    	List<String> pseudos = (List<String>) m.data.getList("Vanished");
    }
    
    @SuppressWarnings({ "unchecked", "static-access" })
	public static List<String> getVanishList(Main m)
    {
    	return (List<String>) m.data.getList("Vanished");
    }
}
