package kittenbbq.discordbot.commands;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kittenbbq.discordbot.BotBase;
import kittenbbq.discordbot.BotConfig;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

public abstract class AbstractCommandHandler {
    
    //protected final IDiscordClient client;
    protected final BotBase bot;
    protected final BotConfig config;
    //protected final ScheduledThreadPoolExecutor scheduler;
    protected MessageReceivedEvent event;

    public AbstractCommandHandler(BotBase bot){
        this.bot = bot;
        config = bot.getConfig();
    }



    public abstract String getHelpMessage(String command);

    public abstract String[] getCommandList();
    
    public void executeCommand(String command, MessageReceivedEvent event){
        this.event = event;
        handleCommand(command);
    }
    
    protected abstract void handleCommand(String command);

    public void sendHelpMessage(String command, MessageReceivedEvent event) {
        this.event = event;
        bot.sendMessage(getHelpMessage(command), event.getChannel());
    }

    protected void sendMessage(String message) {
        bot.sendMessage(message, event.getChannel());
    }

    protected void sendMessage(EmbedObject embedObject) {
        bot.sendMessage(embedObject, event.getChannel());
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
