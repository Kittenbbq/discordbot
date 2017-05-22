package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public abstract class AbstractCommandHandler {
    
    protected final BotBase bot;
    protected MessageReceivedEvent event;

    public AbstractCommandHandler(BotBase bot){
        this.bot = bot;
    }

    public abstract String getHelpMessage(String command);

    public abstract String[] getCommandList();

    protected int getCommandDeleteTime() {
        return bot.getConfig().getCommandDeleteTime();
    }

    protected int getResponseDeleteTime() {
        return bot.getConfig().getResponseDeleteTime();
    }
    
    public void executeCommand(String command, MessageReceivedEvent event){
        this.event = event;
        bot.deleteMessage(event.getMessage(), getCommandDeleteTime());
        handleCommand(command);
    }
    
    protected abstract void handleCommand(String command);

    public IMessage sendHelpMessage(String command, MessageReceivedEvent event) {
        this.event = event;
        return bot.sendMessage(getHelpMessage(command), event.getChannel());
    }

    protected IMessage sendMessage(String message) {
        return bot.sendMessage(message, event.getChannel(), getResponseDeleteTime());
    }

    protected IMessage sendMessage(EmbedObject embedObject) {
        return bot.sendMessage(embedObject, event.getChannel(), getResponseDeleteTime());
    }

    protected IMessage sendMessage(String message, int deleteTime) {
        return bot.sendMessage(message, event.getChannel(), deleteTime);
    }

    protected IMessage sendMessage(EmbedObject embedObject, int deleteTime) {
        return bot.sendMessage(embedObject, event.getChannel(), deleteTime);
    }

    protected String getCommandContent(IMessage message){
        String[] tmp = message.getContent().split(" ", 2);
        if(tmp.length > 1){
            return tmp[1];
        }
        return "";
    }
    
    protected String getCommandContent(){
        String[] tmp = event.getMessage().getContent().split(" ", 2);
        if(tmp.length > 1){
            return tmp[1];
        }
        return "";
    }
    
    protected String[] getCommandArgs(IMessage message){
        return getCommandContent(message).trim().replaceAll("\\s+", " ").split(" ");
    }
    
    protected String[] getCommandArgs(){
        return getCommandContent().trim().replaceAll("\\s+", " ").split(" ");
    }
}
