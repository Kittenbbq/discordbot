package kittenbbq.discordbot.commands;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import kittenbbq.discordbot.BotBase;
import org.apache.http.impl.client.LaxRedirectStrategy;

import javax.net.ssl.HttpsURLConnection;

public class StrawPollCommand extends AbstractCommandHandler {
    public StrawPollCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        switch(command) {
            case "spoll":
                return "!spoll [title] | [spaceDividedOptions]";
            case "spollresults":
                return "!spollresults";
            default:
                return "!spoll [title] | [spaceDividedOptions] to create new poll\n!spollresults to query result from the latest poll";
        }
    }

    @Override
    protected int getCommandDeleteTime() {
        return 0;
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
                try {

                    String[] sections = getCommandContent().trim().split("\\|");

                    if(sections.length == 2) {
                        String title = sections[0].trim();
                        String[] options = sections[1].trim().split("[, ]");

                        for(int i = 0; i < options.length; i++) {
                            options[i] = options[i].trim();
                        }

                        Unirest.setHttpClient(org.apache.http.impl.client.HttpClients.custom()
                                .setRedirectStrategy(LaxRedirectStrategy.INSTANCE)
                                .build());

                        HttpResponse<JsonNode> jsonResponse = Unirest.post(url)
                                .header("Content-Type", "application/json")
                                .field("title", title)
                                .field("options", options)
                                .asJson();

                        if(jsonResponse.getStatus() == HttpsURLConnection.HTTP_OK) {
                            sendMessage("Poll created");
                            sendMessage(jsonResponse.getBody().getObject().toString());
                            sendMessage(url + jsonResponse.getBody().getObject().getInt("id"));

                        } else {
                            sendMessage("" + jsonResponse.getStatus() + ": " + jsonResponse.getStatusText());
                            sendMessage(jsonResponse.getBody().toString());
                        }

                    } else {
                        sendMessage(getHelpMessage(command));
                    }


                } catch(Exception e) {
                    e.printStackTrace();
                    sendMessage("Something went terribly wrong :(");
                }
                break;
            case "!spollresults":
                break;
        }


    }
}
