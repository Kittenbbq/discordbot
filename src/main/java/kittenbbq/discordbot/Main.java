package kittenbbq.discordbot;
import sx.blah.discord.api.events.EventDispatcher;

public class Main {
    
    public static void main(String[] args) {
        BotBase bot = new BotBase();
        EventDispatcher dispatcher = bot.getClient().getDispatcher();
        dispatcher.registerListener(new ChatCommandListener(bot));
        bot.getClient().login();
    }
}
