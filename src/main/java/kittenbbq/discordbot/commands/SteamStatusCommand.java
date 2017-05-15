package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class SteamStatusCommand extends AbstractCommandHandler {

    public SteamStatusCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        return "`!steam` gets steamstatus information.";
    }

    @Override
    public String[] getCommandList() {
        return new String[]{"steam"};
    }

    @Override
    public void handleCommand(String command) {

        IMessage message = event.getMessage();

        try {

            String sURL = "https://steamgaug.es/api/v2";

            URL url = new URL(sURL);
            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.connect();

            if(request.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append('\r');
                }

                br.close();
                String jsonResponse = sb.toString();

                JSONObject rootObj = new JSONObject(jsonResponse);

                EmbedBuilder builder = new EmbedBuilder();

                if (rootObj.getJSONObject("ISteamClient").getInt("online") == 1) {
                    builder.appendField("Steam Status:", "ONLINE", true);
                    builder.withColor(30, 170, 120);
                }
                else {
                    builder.appendField("Steam Status:", "OFFLINE", true);
                    builder.withColor(170, 60, 60);
                }

                if (rootObj.getJSONObject("SteamCommunity").getInt("online") == 1) {
                    builder.appendField("Steam Community Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Community Status:", "OFFLINE", false);
                    builder.appendField("Error:", rootObj.getJSONObject("SteamCommunity").getString("error"), false);
                }

                if (rootObj.getJSONObject("SteamStore").getInt("online") == 1) {
                    builder.appendField("Steam Store Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Store Status:", "OFFLINE", false);
                    builder.appendField("Error:", rootObj.getJSONObject("SteamStore").getString("error"), false);
                }

                if (rootObj.getJSONObject("IEconItems").getJSONObject("440").getInt("online") == 1) {
                    builder.appendField("Steam Inventory Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Inventory Status:", "OFFLINE", false);
                    builder.appendField("Error:", rootObj.getJSONObject("IEconItems").getJSONObject("440").getString("error"), false);
                }

                sendMessage(builder.build());

            } else {
                sendMessage("Could not connect to Steam API");
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            bot.reply(message, "an error occurred, "+e);
        }
    }
}
