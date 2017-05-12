package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
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
        return "!steamstatus";
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
                    builder.appendField("Steam Status:", "Steam is ONLINE", true);
                    builder.withColor(30, 170, 120);
                }
                else {
                    builder.appendField("Steam Status:", "Steam is OFFLINE", true);
                    builder.withColor(170, 60, 60);
                }

                RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));

            } else {
                sendMessage("Could not connect to Steam API");
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            Reply(message, "an error occurred, "+e);

        }

    }
}
