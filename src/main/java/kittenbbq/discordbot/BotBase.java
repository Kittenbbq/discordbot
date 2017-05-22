package kittenbbq.discordbot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
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
    
    private final IDiscordClient client;
    private final BotConfig config;
    private final ScheduledThreadPoolExecutor botScheduler;
    private final BotDAO dao;
    
    public BotBase(){
        config = new BotConfig();
        this.dao = new BotDAO(config);
        client = BotBase.createClient(config.getBotToken(), false);

        ScheduledThreadPoolExecutor BotScheduler = (ScheduledThreadPoolExecutor)
                Executors.newScheduledThreadPool(5, Executors.defaultThreadFactory());
        botScheduler = BotScheduler;
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

    public BotDAO getDao() { return dao; }

    public ScheduledThreadPoolExecutor getBotScheduler() {return botScheduler;}

    public IMessage reply(IMessage message, String content) {
        String replyContent = message.getAuthor().toString() + ", " + content;
        return sendMessage(replyContent, message.getChannel());
    }

    public IMessage sendMessage(EmbedObject embedObject, IChannel channel) {
        return sendMessage(embedObject, channel, config.getResponseDeleteTime());
    }

    public IMessage sendMessage(EmbedObject embedObject, IChannel channel, int deleteTime) {
        RequestBuffer.request(() -> {
            try {
                IMessage message = channel.sendMessage(embedObject);
                deleteMessage(message, deleteTime);
                return message;
            } catch(Exception e) {
                e.printStackTrace();
                throw(e);
            }
        });
        return null;
    }

    public IMessage sendMessage(String message, IChannel channel){
        return sendMessage(message, channel, config.getResponseDeleteTime());
    }

    public IMessage sendMessage(String message, long channelID){
        IChannel chn = client.getChannelByID(channelID);
        if (chn == null) {
            return null;
        }
        return sendMessage(message, chn, config.getResponseDeleteTime());
    }

    public IMessage sendMessage(String message, IChannel channel, int deleteTime){

        RequestBuffer.request(() ->{
            try {
                IMessage messageToDelete = new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
                deleteMessage(messageToDelete, deleteTime);
                return messageToDelete;
            }catch (Exception e) {
                e.printStackTrace();
                throw(e);
            }
        });

        return null;
    }

    public void deleteMessage(IMessage message) {
        RequestBuffer.request(() ->{
            try {
                message.delete();
            }catch (Exception e) {
                e.printStackTrace();
                throw(e);
            }
        });
    }

    public void deleteMessage(IMessage message, int deleteTime) {
        DeleteMessageRunnable runner = new DeleteMessageRunnable(message);
        botScheduler.schedule(runner, deleteTime, TimeUnit.MINUTES);
    }

    public boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.getName().equals(roleToCheck)));
    }

    class DeleteMessageRunnable implements Runnable {

        private IMessage message;

        public DeleteMessageRunnable(IMessage _message) {
            this.message = _message;
        }

        public void run() {
            RequestBuffer.request(() ->{
                try {
                    message.delete();
                }catch (Exception e) {
                    e.printStackTrace();
                    throw(e);
                }
            });
        }
    };
}

