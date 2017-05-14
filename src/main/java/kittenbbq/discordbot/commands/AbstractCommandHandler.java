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

    public void sendHelpMessage(String command, MessageReceivedEvent event) {
        this.event = event;
        bot.sendMessage(getHelpMessage(command), event.getChannel());
    }

    protected void sendMessage(String message) {
        bot.sendMessage(message, event.getChannel(), getResponseDeleteTime());
    }

    protected void sendMessage(EmbedObject embedObject) {
        bot.sendMessage(embedObject, event.getChannel(), getResponseDeleteTime());
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
