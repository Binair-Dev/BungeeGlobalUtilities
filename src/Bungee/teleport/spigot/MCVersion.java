package Bungee.teleport.spigot;

import org.bukkit.*;

public enum MCVersion
{
    v0("null"), 
    v1_7("1.7"), 
    v1_8("1.8"), 
    v1_9("1.9"), 
    v1_10("1.10"), 
    v1_11("1.11"), 
    v1_12("1.12"), 
    v1_13("1.13");
    
    private String version;
    
    private MCVersion(final String version) 
    {
        this.version = version;
    }
    
    private String getVersion() 
    {
        return this.version;
    }
    
    public static MCVersion get()
    {
        final String v = Bukkit.getVersion().split("MC: ")[1].replaceAll("\\)", "");
        for (final MCVersion value : values()) 
        {
            if (v.startsWith(value.getVersion())) 
            {
                return value;
            }
        }
        return MCVersion.v0;
    }
    
    public boolean isSuperior(final MCVersion a)
    {
        return this.ordinal() > a.ordinal();
    }
    
    public boolean isInferior(final MCVersion a)
    {
        return this.ordinal() < a.ordinal();
    }
}
