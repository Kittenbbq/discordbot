package kittenbbq.discordbot;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
}

