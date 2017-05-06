package kittenbbq.discordbot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


public class PollCommandHandler extends AbstractBuiltinCommandHandler{


    private String name;

    private String question;

    private int yesVotes;

    private int noVotes;

    private Map<String, Integer> options;


    public String getName() {
        return name;
    }

    public String getQuestion() {
        return question;
    }


    public int getYesVotes() {
        return yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public Map<String, Integer> getOptions() {
        return options;
    }




    public PollCommandHandler(IDiscordClient client) {
        super(client);
        this.yesVotes = 0;
        this.noVotes = 0;
        this.name = "";
        this.question = "";
        this.options = new TreeMap<>();
    }

    @Override
    String getHelpMessage() {
        switch(getCommand()) {
            case "vote":
                return "!vote [optionName] e.g. !vote yes or !vote taneli";
            case "poll":
                return "!poll [pollName] [question] | [optionsDividedWithSpaces] e.g. !poll whobest Who is the best? | tane taneli tantteli viitanen";
            default:
                return "";
        }

    }

    private String getResults() {
        if (name.isEmpty() || question.isEmpty() || options.isEmpty()) {
            return "No poll results :/";
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format(Locale.ENGLISH, ":bar_chart: %s: %s\n", this.name, this.question));

        for( Map.Entry<String, Integer> entry : options.entrySet()) {
            strBuilder.append(String.format(Locale.ENGLISH, "   %s: %s", entry.getKey(), MyTools.numberToNumberEmoticon(entry.getValue())));
        }

        return strBuilder.toString();
    }

    @Override
    void handle(MessageReceivedEvent event, String command) {
        parseMessage(event.getMessage());

        //!poll whocaptain who is the captain | matti jussi kussi pussi
        if(getCommand() == null || getCommand().isEmpty()) return;
        boolean pass = true;

        switch(getCommand()) {
            case "vote":
                if (getParams() == null) return;

                if(getParams().length == 1) {
                    Integer count = options.get(getParams()[0]);
                    if(count != null) {
                        options.put(getParams()[0], count + 1);
                        sendMessage("Vote given to: " + getParams()[0], getMessage().getChannel());
                    }
                } else {
                    pass = false;
                    break;
                }
                break;

            case "poll":
                //If no params, send the information about the latest poll
                if (getParams() == null || getParams().length == 0) {
                    sendMessage(getResults(), getMessage().getChannel());
                    break;
                }

                if(getParams() != null && getParams().length == 1) {
                    if (getParams()[0].toLowerCase().equals("results")) {
                        sendMessage(getResults(), getMessage().getChannel());
                        break;
                    }
                    //get the param and find the poll with that name
                    pass = false;
                    break;
                }

                String[] arr = getContent().split("\\|");
                if (arr.length == 2) {
                    String[] firstPart = arr[0].split(" ", 2);
                    String[] secondPart = arr[1].split(" ");

                    for(int i = 0; i < secondPart.length; i++) {
                        System.out.println(secondPart[i]);
                    }

                    if(firstPart.length != 2 || secondPart.length < 2) {
                        pass = false;
                        break;
                    }

                    this.options = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                    this.name = firstPart[0];
                    this.question = firstPart[1];

                    for(int i = 0; i < secondPart.length; i++) {
                        String key = secondPart[i].trim();
                        if(!key.isEmpty()) {
                            options.put(secondPart[i], 0);
                        }
                    }

                    sendMessage("Poll created", getMessage().getChannel());

                } else {
                    pass = false;
                    break;
                }
                break;
        }

        if (!pass) {
            sendMessage(getHelpMessage(), getMessage().getChannel());
        }
    }
}
