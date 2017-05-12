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
        return message.getContent().split(" ", 2)[1];
    }
    
    protected String getCommandContent(){
        return event.getMessage().getContent().split(" ", 2)[1];
    }
    
    protected String[] getCommandArgs(IMessage message){
        return getCommandContent(message).split(" ");
    }
    
    protected String[] getCommandArgs(){
        return getCommandContent().split(" ");
    }

    protected void sendMessage(String message, IChannel channel, int deleteTime){
        RequestBuffer.request(() ->{
            try {
                IMessage messageToDelete = new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
                DeleteMessageRunnable runner = new DeleteMessageRunnable(messageToDelete);
<<<<<<< HEAD:src/main/java/kittenbbq/discordbot/AbstractCommandHandler.java
                scheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
=======
                scheduler.schedule(runner, config.getCmdDeleteTime(), TimeUnit.MINUTES);
>>>>>>> origin/master:src/main/java/kittenbbq/discordbot/commands/AbstractCommandHandler.java
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    protected void sendMessage(String message){
        sendMessage(message, event.getMessage().getChannel(), config.getCmdDeleteTime());
    }
    
    protected void sendMessage(String message, IChannel channel){
        sendMessage(message, channel, config.getCmdDeleteTime());
    }

    protected void Reply(IMessage message, String content, IChannel channel) {
        String replyTarget = message.getAuthor().toString();
        RequestBuffer.request(() ->{
            try {
                String replyContent = replyTarget+", "+content;
                IMessage messageToDelete = new MessageBuilder(this.client).withChannel(channel).withContent(replyContent).build();
                DeleteMessageRunnable runner = new DeleteMessageRunnable(messageToDelete);
<<<<<<< HEAD:src/main/java/kittenbbq/discordbot/AbstractCommandHandler.java
                scheduler.schedule(runner, config.getCmdDeleteTime(), TimeUnit.MINUTES);
=======
                scheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
>>>>>>> origin/master:src/main/java/kittenbbq/discordbot/commands/AbstractCommandHandler.java
            }catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
