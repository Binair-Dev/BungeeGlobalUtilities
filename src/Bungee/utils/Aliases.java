package Bungee.utils;

public class Aliases
{
    public String server;
    public String alias;
    public boolean forward;
    public String permission;
    
    public Aliases(final String server, final String alias, final boolean forward, final String permission) 
    {
        this.server = server;
        this.alias = alias;
        this.forward = forward;
        this.permission = permission;
    }
}
