package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import kittenbbq.discordbot.JSONGetter;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;


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

                JSONGetter getStatus = new JSONGetter();
                JSONObject jsonResponse = getStatus.getJSONObject("https://steamgaug.es/api/v2");

                if (jsonResponse != null) {

                EmbedBuilder builder = new EmbedBuilder();

                if (jsonResponse.getJSONObject("ISteamClient").getInt("online") == 1) {
                    builder.appendField("Steam Status:", "ONLINE", true);
                    builder.withColor(30, 170, 120);
                }
                else {
                    builder.appendField("Steam Status:", "OFFLINE", true);
                    builder.withColor(170, 60, 60);
                }

                if (jsonResponse.getJSONObject("SteamCommunity").getInt("online") == 1) {
                    builder.appendField("Steam Community Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Community Status:", "OFFLINE", false);
                    builder.appendField("Error:", jsonResponse.getJSONObject("SteamCommunity").getString("error"), false);
                }

                if (jsonResponse.getJSONObject("SteamStore").getInt("online") == 1) {
                    builder.appendField("Steam Store Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Store Status:", "OFFLINE", false);
                    builder.appendField("Error:", jsonResponse.getJSONObject("SteamStore").getString("error"), false);
                }

                if (jsonResponse.getJSONObject("IEconItems").getJSONObject("440").getInt("online") == 1) {
                    builder.appendField("Steam Inventory Status:", "ONLINE", false);
                }
                else {
                    builder.appendField("Steam Inventory Status:", "OFFLINE", false);
                    builder.appendField("Error:", jsonResponse.getJSONObject("IEconItems").getJSONObject("440").getString("error"), false);
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
