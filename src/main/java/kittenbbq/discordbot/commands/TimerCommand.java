package kittenbbq.discordbot.commands;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IMessage;

public class TimerCommand extends AbstractCommandHandler{

    public TimerCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        return "`!timer [timeInMinutes] [timerMessage]` starts a timer that will alert you when it expires.";
    }
    
    @Override
    public String[] getCommandList() {
        return new String[]{"timer"};
    }

    @Override
    protected void handleCommand(String command) {
        IMessage message = event.getMessage();
        if (message.getContent().split(" ").length > 1) {
            try {
                Integer time = Integer.parseInt(message.getContent().split(" ")[1]);
                BotTimerRunnable runner = new BotTimerRunnable(event.getMessage());
                bot.getBotScheduler().schedule(runner, time, TimeUnit.MINUTES);
                sendMessage(String.format(Locale.ENGLISH, "I will remind you in %d minutes", time));
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
                String messageBody[] = message.getContent().split(" ");
                String splitMessage = "";

                for(int i=2; i < messageBody.length; i++) {
                    splitMessage += messageBody[i]+" ";
                }

                //use generic message if no specific timermessage was given
                if (splitMessage.isEmpty())
                    bot.reply(message, "time is up");
                else 
                    bot.reply(message, splitMessage);

            }catch(Exception e){

            }
        }
    };
}