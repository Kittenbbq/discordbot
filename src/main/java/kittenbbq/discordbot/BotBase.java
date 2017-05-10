package kittenbbq.discordbot;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class BotBase {
    private IDiscordClient client;
    private BotConfig config;
    
    public BotBase(){
        config = new BotConfig();
        this.client = BotBase.createClient(config.getBotToken(), false);
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
}

