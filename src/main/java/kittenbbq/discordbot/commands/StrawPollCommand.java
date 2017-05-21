package kittenbbq.discordbot.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import kittenbbq.discordbot.BotBase;
import kittenbbq.discordbot.StrawPoll;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import javax.net.ssl.HttpsURLConnection;
import java.util.Locale;

public class StrawPollCommand extends AbstractCommandHandler {
    private String latestPollId;
    private IMessage message;
    private String apiBaseUrl = "https://strawpoll.me/api/v2/polls";
    private String pollBaseUrl = "https://strawpoll.me/";

    public StrawPollCommand(BotBase bot) {
        super(bot);

        Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom()
                .setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
                .build());
    }

    @Override
    public String getHelpMessage(String command) {
        switch(command) {
            case "spoll":
                return "!spoll [title] | [commaDividedOptions]";
            case "spollresults":
                return "!spollresults";
            default:
                return "!spoll [title] | [commaDividedOptions] to create new poll\n!spollresults to query result from the latest poll";
        }
    }

    @Override
    protected int getCommandDeleteTime() {
        return 1;
    }

    @Override
    protected int getResponseDeleteTime() {
        return 5;
    }

    @Override
    public String[] getCommandList() {
        return new String[] {"spoll", "spollresults"};
    }

    @Override
    protected void handleCommand(String command) {

        switch(command) {
            case "spoll":
                try {

                    String[] sections = getCommandContent().trim().split("\\|");

                    if(sections.length == 2) {
                        String title = sections[0].trim();
                        String[] options = sections[1].trim().split("[,]");

                        for(int i = 0; i < options.length; i++) {
                            options[i] = options[i].trim();
                        }

                        JSONObject data = new JSONObject();
                        data.put("title", title);
                        data.put("options", options);

                        HttpResponse<JsonNode> jsonResponse = Unirest.post(apiBaseUrl)
                                .header("Content-Type", "application/json")
                                .body(data)
                                .asJson();

                        if(jsonResponse.getStatus() == HttpsURLConnection.HTTP_OK) {
                            String id = Integer.toString(jsonResponse.getBody().getObject().getInt("id"));
                            if(!id.isEmpty()) {
                                latestPollId = id;
                                sendMessage(String.format(Locale.ENGLISH,"Poll \"%s\" created\n%s", title, pollBaseUrl + id));
                            }
                        } else {
                            sendMessage("Something went wrong when creating the poll :/");
                        }

                    } else {
                        sendMessage(getHelpMessage(command));
                    }


                } catch(Exception e) {
                    e.printStackTrace();
                    sendMessage("Something went terribly wrong :(");
                }
                break;
            case "spollresults":
                try {
                    String url = apiBaseUrl + "/" + latestPollId;
                    HttpResponse<String> jsonResponse = Unirest.get(url).asString();

                    if(jsonResponse.getStatus() == HttpsURLConnection.HTTP_OK) {
                        Gson gson = new GsonBuilder().create();
                        StrawPoll poll = gson.fromJson(jsonResponse.getBody(), StrawPoll.class);

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.withTitle(String.format(Locale.ENGLISH,"Results for \"%s\"", poll.getTitle()));
                        builder.withUrl(pollBaseUrl + poll.getId() + "/r");
                        builder.withColor(30, 170, 120);

                        for(int i = 0; i < poll.getOptions().size(); i++) {
                            builder.appendField(poll.getOptions().get(i), Long.toString(poll.getVotes().get(i)), true);
                        }

                        if(message != null && !message.isDeleted()) {
                            message.delete();

                        }

                        message = sendMessage(builder.build());

                    } else {
                        sendMessage("Could not fetch results");
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                    sendMessage("Could not fetch results");
                }
                break;
        }


    }
}
