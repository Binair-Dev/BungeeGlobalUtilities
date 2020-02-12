package Bungee.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import Bungee.config.ConfigManager;
import Bungee.listeners.GlobalUtilitiesListener;
import Bungee.teleport.bungee.AutoCompletion;
import Bungee.teleport.bungee.BungeeReceive;
import Bungee.utils.Aliases;
import Bungee.utils.event.Alert;
import Bungee.utils.event.CmdVanish;
import Bungee.utils.event.Find;
import Bungee.utils.event.Glist;
import Bungee.utils.event.Gtp;
import Bungee.utils.event.Gtphere;
import Bungee.utils.event.IPCheck;
import Bungee.utils.event.Lookup;
import Bungee.utils.event.NameCheck;
import Bungee.utils.event.SendCmd;
import Bungee.utils.event.ServerCmd;
import Bungee.utils.event.UUIDCheck;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin
{
	public static List<String> vanished = new ArrayList<String>();
	
    private static ConfigManager cfgmanager;
    private static Main instance;
    public static Configuration defaultConfig;
    public static Configuration data;
    
    public static String oldChannel;
    public static String channel;

    {
        Main.oldChannel = "BungeeUtilities";
        Main.channel = "bungee:utilities";
    }
    
    @SuppressWarnings("static-access")
	public void onEnable() 
    {
    	//Aliases
    	//Copyright
    	System.out.println("====================================");
    	System.out.println("Chargement de BungeeGlobalUtilities");
    	System.out.println("Auteur: B_nair");
    	System.out.println("====================================");
    	//Listener
        getProxy().getPluginManager().registerListener(this, new GlobalUtilitiesListener(this));
    	//Utils
        Main.instance = this;
        Main.cfgmanager = new ConfigManager(this);
    	//LoadingConfig
    	cfgmanager.createDefaultConfigFile();
    	cfgmanager.createCustomConfigFile("data");
        try 
        {
			Main.defaultConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
		} 
        catch (IOException e1) {e1.printStackTrace();}
        try 
        {
			Main.data = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "data.yml"));
		}
        catch (IOException e1) {e1.printStackTrace();}
    	//LoadConf
    	this.loadConfig();
        cfgmanager.loadDefaultMessages();
        cfgmanager.loadDefaultCommand();
        cfgmanager.loadVanished(this);
    	//Alerte
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Alert(this));
        //Find
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Find(this));
        //UUID
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new UUIDCheck(this));
        //IP
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new IPCheck(this));
        //NameHistory
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new NameCheck(this));
        //Lookup
        addTimeConnect();
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Lookup(this));
        //Server
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new ServerCmd(this));
        //Send
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new SendCmd(this));
        //Channel
        this.getProxy().registerChannel(Main.oldChannel);
        this.getProxy().registerChannel(Main.channel);
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new BungeeReceive());
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new AutoCompletion());
        //Vanish
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new CmdVanish(this));
        //GTP
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Gtp(this));
        //GTPHere
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Gtphere(this));
        //Glist
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new Glist(this));
    }
    
    @SuppressWarnings("static-access")
	public void onDisable() 
    {
    	this.getConfig().saveVanished(this, vanished);
    }
    public static ConfigManager getConfig() 
    {
        return cfgmanager;
    }
    
    public static Main getInstance()
    {
        return Main.instance;
    }
    
    public void addTimeConnect() 
    {
        getProxy().getScheduler().schedule(this, new Runnable() 
        {
            @Override
            public void run() 
            {
            	
            	for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) 
                {
            		UUID offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + p.getName()).getBytes(StandardCharsets.UTF_8));
            		int x = data.getInt("Lookup." + offlineId + ".Time");
            		int fin = x + 1;
            		getConfig().updateTime("Lookup." + offlineId, "Time" , fin);
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
        
    }
    @SuppressWarnings("static-access")
	public void loadConfig() 
	{
    GlobalUtilitiesListener.aliases = new ArrayList<Aliases>();
    try 
    {
        for (final String server : this.defaultConfig.getSection("aliases").getKeys()) 
        {
            for (final String alias : this.defaultConfig.getSection("aliases." + server).getKeys()) 
            {
                String permission = this.defaultConfig.getString("aliases." + server + "." + alias + ".permission");
                if (permission.equals("")) {
                    permission = null;
                }
                GlobalUtilitiesListener.aliases.add(new Aliases(server, alias, this.defaultConfig.getBoolean("aliases." + server + "." + alias + ".forward"), permission));
            }
        }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
}
