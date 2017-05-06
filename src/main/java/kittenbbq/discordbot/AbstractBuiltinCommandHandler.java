package kittenbbq.discordbot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MessageBuilder;

public abstract class AbstractBuiltinCommandHandler {

    protected IDiscordClient client;

    private IMessage message;

    private String command;

    private String content;

    private String[] params;

    abstract String getHelpMessage();

    public IMessage getMessage() {
        return message;
    }

    public String getCommand() {
        return command;
    }

    public String getContent() {
        return content;
    }

    public String[] getParams() {
        return params;
    }


    public AbstractBuiltinCommandHandler(IDiscordClient client) {
        this.client = client;
    }

    protected void parseMessage(IMessage message) {
        this.message = message;
        this.command = "";
        this.params = null;
        this.content = "";


        //Trim extra spaces from the start and end
        String msg = message.getContent().trim().replaceAll("\\s+", " ");

        //Split the message into two pieces: command and rest of the content
        String[] arr = msg.split(" ", 2);

        //Remove the prefix (e.g. !)
        if(arr.length >= 1) {
            this.command = arr[0].substring(1);
        }

        if(arr.length >= 2) {
            this.content = arr[1];
            this.params = arr[1].split(" ");
        }

    }

    abstract void handle(MessageReceivedEvent event, String command);

    protected void sendMessage(String message, IChannel channel) {
        try {
            new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
