package kittenbbq.discordbot;

import kittenbbq.discordbot.commands.AbstractCommandHandler;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BotBase {
    
    private IDiscordClient client;
    private BotConfig config;
    private ScheduledThreadPoolExecutor botScheduler;
    
    public BotBase(){
        config = new BotConfig();
        this.client = BotBase.createClient(config.getBotToken(), false);

        ScheduledThreadPoolExecutor BotScheduler = (ScheduledThreadPoolExecutor)
                Executors.newScheduledThreadPool(5, Executors.defaultThreadFactory());
        this.botScheduler = BotScheduler;
    }

    private static IDiscordClient createClient(String token, boolean login) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            if (login) {
                return clientBuilder.login();
            } else {
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public IDiscordClient getClient() {
        return client;
    }

    public BotConfig getConfig() {
        return config;
    }

    public ScheduledThreadPoolExecutor getBotScheduler() {return botScheduler;}

    public void reply(IMessage message, String content) {
        String replyContent = message.getAuthor().toString() + ", " + content;
        sendMessage(replyContent, message.getChannel());
    }

    /*
    protected void sendMessage(String message){
        sendMessage(message, event.getMessage().getChannel(), config.getCmdDeleteTime());
    }
    */

    public void sendMessage(EmbedObject embedObject, IChannel channel) {
        sendMessage(embedObject, channel, config.getCmdDeleteTime());
    }

    public void sendMessage(EmbedObject embedObject, IChannel channel, int deleteTime) {
        RequestBuffer.request(() -> {
            try {
                IMessage message = channel.sendMessage(embedObject);
                DeleteMessageRunnable runner = new DeleteMessageRunnable(message);
                botScheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(String message, IChannel channel){
        sendMessage(message, channel, config.getCmdDeleteTime());
    }

    public void sendMessage(String message, long channelID){
        IChannel chn = client.getChannelByID(channelID);
        if (chn == null) {
            return;
        }
        sendMessage(message, chn, config.getCmdDeleteTime());
    }

    public void sendMessage(String message, IChannel channel, int deleteTime){
        RequestBuffer.request(() ->{
            try {
                IMessage messageToDelete = new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
                DeleteMessageRunnable runner = new DeleteMessageRunnable(messageToDelete);
                botScheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
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
}

