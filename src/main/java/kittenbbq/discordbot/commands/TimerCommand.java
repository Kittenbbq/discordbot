package kittenbbq.discordbot.commands;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IMessage;

public class TimerCommand extends AbstractCommandHandler{

    public TimerCommand(BotBase bot) {
        super(bot);
    }
    
    @Override
    public String[] getCommandList() {
        return new String[]{"timer"};
    }

    @Override
    public String getHelpMessage(String command) {
        return "!timer [timeInMinutes]";
    }

    class BotTimerRunnable implements Runnable {

        private IMessage message;

        public BotTimerRunnable(IMessage _message) {
            this.message = _message;
        }

        public void run() {
            try{
                String messageBody[] = message.getContent().split(" ");
                String splitMessage = "";

                for(int i=2; i < messageBody.length; i++) {
                    splitMessage += messageBody[i]+" ";
                }

                //use generic message if no specific timermessage was given
                if (splitMessage == "") 
                    Reply(message, "time is up", message.getChannel());
                else 
                    Reply(message, splitMessage, message.getChannel());

            }catch(Exception e){

            }
        }
    };

    @Override
    protected void handleCommand(String command) {
        IMessage message = event.getMessage();
        if (message.getContent().split(" ").length > 1) {
            try {
                Integer time = Integer.parseInt(message.getContent().split(" ")[1]);
                BotTimerRunnable runner = new BotTimerRunnable(event.getMessage());
                ScheduledFuture<?> delayFuture = scheduler.schedule(runner, time, TimeUnit.MINUTES);
                sendMessage("Timer is running",event.getChannel());
            }
            catch(Exception e) {
                Reply(message, "time not valid, error: "+e,message.getChannel());
            }

        }
        else {
            Reply(message, "!timer usage: !timer time (timermessage)",message.getChannel());
        }
    }
}