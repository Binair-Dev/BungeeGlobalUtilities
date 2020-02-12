package Bungee.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class UsernameHistory
{
    String uuid;
    ArrayList<Username> history;
    
    public UsernameHistory(final String name) throws IOException, ParseException, java.text.ParseException {
        this.uuid = (String)((JSONObject)JSONUtility.getJsonObject("https://api.mojang.com/users/profiles/minecraft/" + name)).get((Object)"id");
        this.loadHistory();
    }
    
    public UsernameHistory(final UUID uuid) throws IOException, ParseException, java.text.ParseException {
        this.uuid = uuid.toString();
        this.loadHistory();
    }
    
    public ArrayList<Username> getHistory() {
        return this.history;
    }
    
    public void loadHistory() throws IOException, ParseException, java.text.ParseException {
        this.history = new ArrayList<Username>();
        final String url = "https://api.mojang.com/user/profiles/" + this.uuid + "/names";
        final JSONArray response = (JSONArray)JSONUtility.getJsonObject(url);
        for (final Object objN : response) {
            final JSONObject obj = (JSONObject)objN;
            if (obj.containsKey((Object)"error")) {
                throw new IOException("No player exists with that username!");
            }
            final String name = (String)obj.get((Object)"name");
            long changedToAt = 0L;
            if (obj.containsKey((Object)"changedToAt")) {
                changedToAt = (long)obj.get((Object)"changedToAt");
            }
            final Username usrn = new Username(name, changedToAt);
            this.history.add(usrn);
        }
    }
}
