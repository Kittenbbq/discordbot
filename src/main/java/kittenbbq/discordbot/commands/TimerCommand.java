package kittenbbq.discordbot.commands;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IMessage;

public class TimerCommand extends AbstractCommandHandler{

    public TimerCommand(BotBase bot) {
        super(bot);
    }
    private int timerLength = 5;

    @Override
    public String getHelpMessage(String command) {
        return "`!timer [timeInMinutes] [timerMessage]` starts a timer that will alert you when it expires.";
    }
    
    @Override
    public String[] getCommandList() {
        return new String[]{"timer"};
    }

    @Override
    protected int getCommandDeleteTime() {
        return 0;
    }

    @Override
    protected int getResponseDeleteTime() {
        return timerLength;
    }

    @Override
    protected void handleCommand(String command) {
        IMessage message = event.getMessage();
        if (message.getContent().split(" ").length > 1) {
            try {
                Integer time = Integer.parseInt(message.getContent().split(" ")[1]);
                timerLength = time;
                BotTimerRunnable runner = new BotTimerRunnable(event.getMessage());
                bot.getBotScheduler().schedule(runner, time, TimeUnit.MINUTES);
                sendMessage(String.format(Locale.ENGLISH, "%s, I will remind you in %d minutes", message.getAuthor().getName(), time));
            }
            catch(Exception e) {
                sendMessage("Time not valid");
            }
        }
        else {
            bot.reply(message, getHelpMessage(command));
        }
    }

    class BotTimerRunnable implements Runnable {

        private final IMessage message;

        public BotTimerRunnable(IMessage _message) {
            this.message = _message;
        }

        @Override
        public void run() {
            try{

                String[] split = message.toString().split(" ", 3);
                String response = split.length == 3 ? split[2] : "";

                //use generic message if no specific timermessage was given
                if (response.isEmpty())
                    bot.reply(message, "time is up");
                else 
                    bot.reply(message, response);

            }catch(Exception e){

            }
        }
    };
}