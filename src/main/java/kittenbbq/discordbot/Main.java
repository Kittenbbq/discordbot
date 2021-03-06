package kittenbbq.discordbot;

import kittenbbq.discordbot.commands.*;
import sx.blah.discord.api.events.EventDispatcher;

public class Main {
    
    public static void main(String[] args) {
        BotBase bot = new BotBase();
        EventDispatcher dispatcher = bot.getClient().getDispatcher();
        
        ChatCommandListener cmdlistener = new ChatCommandListener(bot, new DatabaseCommand(bot));
        cmdlistener.registerCommand(new TenmanCommand(bot));
        cmdlistener.registerCommand(new ChangeTopicCommand(bot));
        cmdlistener.registerCommand(new InviteMemberCommand(bot));
        cmdlistener.registerCommand(new TimerCommand(bot));
        cmdlistener.registerCommand(new SteamStatusCommand(bot));
        cmdlistener.registerCommand(new StrawPollCommand(bot));
        cmdlistener.registerCommand(new IssuesCommand(bot));
        cmdlistener.registerCommand(new ClearCommand(bot));
        cmdlistener.registerCommand(new PlayingCommand(bot));

        dispatcher.registerListener(cmdlistener);
        bot.getClient().login();
    }
}
