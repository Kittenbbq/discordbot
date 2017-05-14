package kittenbbq.discordbot.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
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

        try {
            String sURL = "https://steamgaug.es/api/v2";
            HttpResponse<JsonNode> jsonResponse = Unirest.get(sURL).asJson();

            if(jsonResponse.getStatus() == HttpsURLConnection.HTTP_OK) {
                JSONObject steamClient = jsonResponse.getBody().getObject().getJSONObject("ISteamClient");
                EmbedBuilder builder = new EmbedBuilder();

                if (steamClient.getInt("online") == 1) {
                    builder.appendField("Steam Status:", "Steam is ONLINE", true);
                    builder.withColor(30, 170, 120);
                }
                else {
                    builder.appendField("Steam Status:", "Steam is OFFLINE", true);
                    builder.withColor(170, 60, 60);
                }

                sendMessage(builder.build());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            IMessage message = event.getMessage();
            bot.reply(message, "an error occurred, "+e);
        }
    }
}
