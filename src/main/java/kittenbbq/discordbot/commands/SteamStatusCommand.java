package kittenbbq.discordbot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;
import com.google.gson.JsonParser;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class SteamStatusCommand extends AbstractCommandHandler{

    public SteamStatusCommand(BotBase bot) {
        super(bot);
    }


    @Override
    public void handleCommand(String command, MessageReceivedEvent event) {

        IMessage message = event.getMessage();

            try {String sURL = "https://steamgaug.es/api/v2";

                URL url = new URL(sURL);
                HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
                request.connect();

                JsonParser jp = new JsonParser();
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                JsonObject rootobj = root.getAsJsonObject();

                EmbedBuilder builder = new EmbedBuilder();

                if (Integer.parseInt(rootobj.getAsJsonObject("ISteamClient").getAsJsonPrimitive("online").toString()) == 1) {
                    builder.appendField("Steam Status:", "Steam is ONLINE", true);
                    builder.withColor(0, 255, 0);
                }
                else {
                    builder.appendField("Steam Status:", "Steam is OFFLINE", true);
                    builder.withColor(255, 0, 0);
                }


                RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));

            }
            catch(Exception e) {

                e.getStackTrace();
                Reply(message, "an error occurred, "+e,message.getChannel());

            }

        }
    }
