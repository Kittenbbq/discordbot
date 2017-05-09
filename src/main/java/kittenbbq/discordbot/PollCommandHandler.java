package kittenbbq.discordbot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import javax.print.DocFlavor;
import java.util.*;


public class PollCommandHandler extends AbstractBuiltinCommandHandler{


    private TreeMap<String, Poll> polls;

    public PollCommandHandler(IDiscordClient client) {
        super(client);
        polls = new TreeMap<String, Poll>(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    String getHelpMessage() {
        switch(getCommand()) {
            case "vote":
                return "!vote [pollName] [optionName] e.g. !vote yes or !vote taneli";
            case "poll":
                return "!poll [pollName] [question] | [optionsDividedWithSpaces] e.g. !poll whobest Who is the best? | tane taneli tantteli viitanen";
            default:
                return "";
        }
    }


    @Override
    void handle(MessageReceivedEvent event, String command) {
        parseMessage(event.getMessage());

        //!poll whocaptain who is the captain | matti jussi kussi pussi
        if(getCommand() == null || getCommand().isEmpty()) return;
        boolean pass = true;

        switch(getCommand()) {
            case "vote":
                if(getParams() == null || getParams().length < 1) {
                    pass = false;
                    break;
                }

                String pollName = "";
                String pollVoteFor = "";
                Poll poll = null;

                if(getParams().length == 1) {
                    poll = polls.get(polls.lastKey());
                    if (poll != null) {
                        pollName = poll.getName();
                        pollVoteFor = getParams()[0];
                    } else {
                        break;
                    }

                } else {
                    pollName = getParams()[0];
                    pollVoteFor = getParams()[1];
                }

                if (poll == null) {
                    poll = polls.get(pollName);
                }

                if(poll.addVote(pollVoteFor)) {
                    sendMessage(String.format(Locale.ENGLISH, "Vote given to %s in %s poll", pollVoteFor, pollName));
                    break;
                } else {
                    sendMessage(String.format(Locale.ENGLISH, "%s option was not found for poll %s", pollVoteFor, pollName));
                }

                break;

            case "poll":
                //If no params, send the information about the latest poll
                if (getParams() == null || getParams().length == 0) {
                    Poll latestPoll = polls.get(polls.lastKey());
                    if(latestPoll != null) {
                        sendMessage(polls.get(polls.lastKey()).getResults());
                    }
                    break;
                }

                //If one param, get the poll with that name and send results
                if(getParams() != null && getParams().length == 1) {
                    //get the param and find the poll with that name
                    String resultsForPoll = getParams()[0];
                    Poll existingPoll = polls.get(resultsForPoll);

                    if(existingPoll != null) {
                        sendMessage(existingPoll.getResults());
                    } else {
                        sendMessage(String.format(Locale.ENGLISH, "%s poll was not found.", resultsForPoll));
                    }

                    break;
                }

                //Create new poll
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

                    //firstpart[0] = name, firstpart[1] = question, secondPart = options
                    Poll newPoll = new Poll(firstPart[0], firstPart[1], secondPart);
                    polls.put(firstPart[0], newPoll);

                    sendMessage("Poll created");

                } else {
                    pass = false;
                    break;
                }
                break;
        }

        if (!pass) {
            sendMessage(getHelpMessage());
        }
    }
}
