package kittenbbq.discordbot.commands;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kittenbbq.discordbot.BotBase;
import kittenbbq.discordbot.BotConfig;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

public abstract class AbstractCommandHandler {
    
    protected final IDiscordClient client;
    protected final BotConfig config;
    protected final ScheduledThreadPoolExecutor scheduler;
    protected MessageReceivedEvent event;

    public AbstractCommandHandler(BotBase bot){
        client = bot.getClient();
        config = bot.getConfig();
        scheduler = bot.getBotScheduler();
    }

    class DeleteMessageRunnable implements Runnable {

        private IMessage message;

        public DeleteMessageRunnable(IMessage _message) {
            this.message = _message;
        }

        public void run() {
            try{
                Discord4J.LOGGER.debug("removing message");
                message.delete();

            }catch(Exception e){

            }
        }
    };

    public abstract String getHelpMessage(String command);

    public abstract String[] getCommandList();
    
    public void executeCommand(String command, MessageReceivedEvent event){
        this.event = event;
        handleCommand(command);
    }
    
    protected abstract void handleCommand(String command);
    
    
    protected boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
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

    protected void Reply(IMessage message, String content) {
        String replyContent = message.getAuthor().toString() + ", " + content;
        sendMessage(replyContent);
    }
    
    protected void sendMessage(String message){
        sendMessage(message, event.getMessage().getChannel(), config.getCmdDeleteTime());
    }
    
    protected void sendMessage(String message, IChannel channel){
        sendMessage(message, channel, config.getCmdDeleteTime());
    }

    protected void sendMessage(String message, IChannel channel, int deleteTime){
        RequestBuffer.request(() ->{
            try {
                IMessage messageToDelete = new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
                DeleteMessageRunnable runner = new DeleteMessageRunnable(messageToDelete);
                scheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendHelpMessage(String command, MessageReceivedEvent event) {
        this.event = event;
        sendMessage(getHelpMessage(command));
    }
}
