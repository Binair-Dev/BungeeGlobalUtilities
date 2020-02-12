package Bungee.config;

import java.util.List;

public class AliasesManager 
{
	  public static List<String> alert;
	  public static List<String> build;
	  public static List<String> find;
	  public static List<String> glist;
	  public static List<String> gtp;
	  public static List<String> gtphere;
	  public static List<String> ipcheck;
	  public static List<String> lookup;
	  public static List<String> namecheck;
	  public static List<String> semirp;
	  public static List<String> send;
	  public static List<String> server;
	  public static List<String> uuid;
	  public static List<String> vanish;
	  
	  public static String[] getAliases(List<String> list)
	  {
		  String[] strarray = list.toArray(new String[0]);
		  return strarray;
	  }
}
