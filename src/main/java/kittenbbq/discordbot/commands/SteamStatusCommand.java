package kittenbbq.discordbot.commands;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import kittenbbq.discordbot.BotBase;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import javax.net.ssl.HttpsURLConnection;


public class SteamStatusCommand extends AbstractCommandHandler {

    public SteamStatusCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        return "`!steam` gets Steam status information.";
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

                JSONObject jsonObj = jsonResponse.getBody().getObject();

                JSONObject steamClient = jsonObj.getJSONObject("ISteamClient");
                JSONObject steamCommunity = jsonObj.getJSONObject("SteamCommunity");
                JSONObject steamStore = jsonObj.getJSONObject("SteamStore");
                JSONObject steamInventory = jsonObj.getJSONObject("IEconItems");

                EmbedBuilder builder = new EmbedBuilder();

                if (steamClient.getInt("online") == 1) {
                    builder.appendField("Steam Status:", "ONLINE", true);
                    builder.withColor(30, 170, 120);
                }
                else {
                    builder.appendField("Steam Status:", "OFFLINE", true);
                    builder.withColor(170, 60, 60);
                }

                if (steamCommunity.getInt("online") == 1) {
                    builder.appendField("Steam Community Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Community Status:", "OFFLINE", false);
                    builder.appendField("Error:", steamCommunity.getString("error"), false);
                }

                if (steamStore.getInt("online") == 1) {
                    builder.appendField("Steam Store Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Store Status:", "OFFLINE", false);
                    builder.appendField("Error:", steamStore.getString("error"), false);
                }

                if (steamInventory.getJSONObject("440").getInt("online") == 1) {
                    builder.appendField("Steam Inventory Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Inventory Status:", "OFFLINE", false);
                    builder.appendField("Error:", steamInventory.getJSONObject("440").getString("error"), false);
                }

                sendMessage(builder.build());
            } else {
                sendMessage("Could not connect to Steam API.");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            IMessage message = event.getMessage();
            bot.reply(message, "an error occurred, "+e);
        }
    }
}
