package kittenbbq.discordbot;
import analytics.Application;
import org.springframework.boot.SpringApplication;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class Main {
    public static void main(String[] args) {
        BotConfig config = new BotConfig();
        IDiscordClient client = BotBase.createClient(config.getBotToken(), true);
        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(new ChatCommandListener(client, config));
        SpringApplication.run(Application.class, args);
    }
}
