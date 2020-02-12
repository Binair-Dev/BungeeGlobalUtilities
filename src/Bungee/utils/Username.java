package Bungee.utils;

import java.text.*;
import java.util.*;

public class Username
{
    String name;
    long changedToAt;
    
    public Username(final String name, final long changedToAt) 
    {
        this.name = name;
        this.changedToAt = changedToAt;
    }
    
    public String getName() 
    {
        return this.name;
    }
    
    public String getTime() {
        if (this.changedToAt == 0L) 
        {
            return "original";
        }
        return new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss z").format(new Date(this.changedToAt));
    }
}
