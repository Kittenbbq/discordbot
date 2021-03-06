package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageHistory;

import java.time.*;

public class ClearCommand extends AbstractCommandHandler {

    public ClearCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        switch(command) {
            case "clear":
                return "`!clear [numberOfMessagesToDelete]` to clear your messages";
            case "clearbot":
                return "`!clearbot [numberOfMessagesToDelete]` to clear bot messages";
            default:
                return "`!clear [numberOfMessagesToDelete]` to clear your messages\n`!clearbot [numberOfMessagesToDelete]` to clear bot messages";
        }
    }

    @Override
    public String[] getCommandList() {
        return new String[]{"clear", "clearbot"};
    }

    @Override
    protected int getCommandDeleteTime() {
        return 0;
    }

    @Override
    protected void handleCommand(String command) {

        int messagesToDelete;

        if(getCommandArgs().length != 1) {
            sendMessage(getHelpMessage(command));
            return;
        }

        messagesToDelete = Integer.parseInt(getCommandArgs()[0]) + 1;

        boolean clearBotMessages = command.equals("clearbot");

        try {
            IUser user = event.getAuthor();

            for(IMessage message : getMessageHistory()) {
                if(messagesToDelete <= 0) break;

                if(clearBotMessages) {
                    if(!message.isDeleted() && message.getAuthor().isBot()) {
                        bot.deleteMessage(message);
                        messagesToDelete--;
                    }
                }
                else if(!message.isDeleted() && message.getAuthor().equals(user)) {
                    bot.deleteMessage(message);
                    messagesToDelete--;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            sendMessage("WOW! Something went wrong");
        }
    }

    private MessageHistory getMessageHistory() {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.of("Europe/Paris"));
        Instant endInstant = now.toInstant();
        Instant startInstant = now.minusDays(1).toInstant();

        return event.getChannel().getMessageHistoryIn(startInstant, endInstant);
    }
}
