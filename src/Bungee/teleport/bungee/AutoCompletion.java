package Bungee.teleport.bungee;

import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.event.*;

public class AutoCompletion implements Listener
{
    @EventHandler(priority = 32)
    public void onTab(final TabCompleteEvent e) 
    {
        final String[] args = e.getCursor().toLowerCase().split(" ");
        if (args.length >= 1 && args[0].startsWith("/") && e.getCursor().contains(" ")) 
        {
            e.getSuggestions().clear();
            @SuppressWarnings("unused")
			final ProxiedPlayer p = (ProxiedPlayer)e.getSender();
            if (args.length == 1) {
                for (final ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) 
                {
                    e.getSuggestions().add(all.getName());
                }
                return;
            }
            if (args.length == 2 && getSpace(e.getCursor()) == 1) 
            {
                this.addSuggestions(e, args);
                return;
            }
            if (args.length == 2) 
            {
                for (final ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
                {
                    e.getSuggestions().add(all.getName());
                }
                return;
            }
            if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                this.addSuggestions(e, args);
            }
        }
    }
    
    private void addSuggestions(final TabCompleteEvent e, final String[] args) 
    {
        final String check = args[args.length - 1];
        for (final ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) 
        {
            if (all.getName().toLowerCase().startsWith(check)) 
            {
                e.getSuggestions().add(all.getName());
            }
        }
    }
    
    public static int getSpace(final String s) 
    {
        int space = 0;
        for (int i = 0; i < s.length(); ++i) 
        {
            if (Character.isWhitespace(s.charAt(i)))
            {
                ++space;
            }
        }
        return space;
    }
}
