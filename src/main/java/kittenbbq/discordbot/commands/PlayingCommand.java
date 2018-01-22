package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;

public class PlayingCommand extends AbstractCommandHandler{

    public PlayingCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        return "`!playing [text to set]` to set the bots playing message.";
    }

    @Override
    public String[] getCommandList() {
        return new String[]{"playing"};
    }

    @Override
    protected int getCommandDeleteTime() {
        return 0;
    }

    @Override
    protected void handleCommand(String command) {
        bot.getClient().changePlayingText(getCommandContent());
    }
}
