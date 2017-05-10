package kittenbbq.discordbot;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

abstract class AbstractCommandHandler {
    
    protected final IDiscordClient client;
    protected final BotConfig config;

    // Create a scheduled thread pool with 5 core threads
    ScheduledThreadPoolExecutor deleteScheduler = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(5, Executors.defaultThreadFactory());

    public AbstractCommandHandler(BotBase bot){
        client = bot.getClient();
        config = bot.getConfig();

    }

    class DeleteMessageRunnable implements Runnable {

        private IMessage message;

        public DeleteMessageRunnable(IMessage _message) {
            this.message = _message;
        }

        public void run() {
            try{

                message.delete();

            }catch(Exception e){

            }
        }
    };
    
    abstract void handleCommand(String command, MessageReceivedEvent event);
    
    
    protected boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
    }
    
    protected String getCommandContent(IMessage message){
        return message.getContent().split(" ", 2)[1];
    }
    
    protected String[] getCommandArgs(IMessage message){
        return getCommandContent(message).split(" ");
    }
    
    protected void sendMessage(String message, IChannel channel){
        sendMessage(message, channel, config.getCmdDeleteTime());
    }
    
    protected void sendMessage(String message, IChannel channel, int deleteTime){
        RequestBuffer.request(() ->{
            try {
                IMessage messageToDelete = new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
                DeleteMessageRunnable runner = new DeleteMessageRunnable(messageToDelete);
                ScheduledFuture<?> delayFuture = deleteScheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
