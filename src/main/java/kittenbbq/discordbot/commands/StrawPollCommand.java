package kittenbbq.discordbot.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import kittenbbq.discordbot.BotBase;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;
import java.util.Locale;

public class StrawPollCommand extends AbstractCommandHandler {
    private String latestPollUrl;

    public StrawPollCommand(BotBase bot) {
        super(bot);
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
                String url = "https://strawpoll.me/api/v2/polls";
                String pollBaseUrl = "https://strawpoll.me/";
                try {

                    String[] sections = getCommandContent().trim().split("\\|");

                    if(sections.length == 2) {
                        String title = sections[0].trim();
                        String[] options = sections[1].trim().split("[,]");

                        for(int i = 0; i < options.length; i++) {
                            options[i] = options[i].trim();
                        }


                        Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom()
                                .setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
                                .build());

                        JSONObject data = new JSONObject();
                        data.put("title", title);
                        data.put("options", options);

                        HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                                .header("Content-Type", "application/json")
                                .body(data)
                                .asJson();

                        if(jsonResponse.getStatus() == HttpsURLConnection.HTTP_OK) {
                            String id = Integer.toString(jsonResponse.getBody().getObject().getInt("id"));
                            if(!id.isEmpty()) {
                                latestPollUrl = pollBaseUrl + id;
                                sendMessage(String.format(Locale.ENGLISH,"Poll \"%s\" created\n%s", title, latestPollUrl));
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
                sendMessage(latestPollUrl);
                break;
        }


    }
}
