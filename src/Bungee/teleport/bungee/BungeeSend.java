package Bungee.teleport.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.*;
import java.io.*;
import Bungee.main.Main;

public class BungeeSend
{
    public static void teleport(final ProxiedPlayer from, final ProxiedPlayer to) 
    {
        final ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(byteArrayOut);
        try 
        {
        	System.out.println("Packet: TP sended");
            out.writeUTF("Teleport");
            out.writeUTF(from.getName());
            out.writeUTF(to.getName());
            from.getServer().getInfo().sendData(Main.oldChannel, byteArrayOut.toByteArray());
            from.getServer().getInfo().sendData(Main.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	public static void vanishToAllServer(String from, String state)
    {
        final ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(byteArrayOut);
        try 
        {
        	System.out.println("Packet: Vanish sended");
            out.writeUTF("VanishAll");
            out.writeUTF(from);
            out.writeUTF(state);
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(from);
            ServerInfo serverInfo = p.getServer().getInfo();
        	serverInfo.sendData(Main.oldChannel, byteArrayOut.toByteArray());
        	serverInfo.sendData(Main.channel, byteArrayOut.toByteArray());
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
