package Bungee.utils;

import java.net.*;
import java.io.*;
import org.json.simple.parser.*;

public class JSONUtility
{
    public static Object getJsonObject(final String url) throws IOException, ParseException {
        final URL url2 = new URL(url);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
        final JSONParser parser = new JSONParser();
        final Object obj = parser.parse((Reader)reader);
        return obj;
    }
}
