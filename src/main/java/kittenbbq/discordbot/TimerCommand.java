package kittenbbq.discordbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;



public class TimerCommand extends AbstractCommandHandler{

    public TimerCommand(BotBase bot) {
        super(bot);
    }
    // Create a scheduled thread pool with 5 core threads
    ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(5);

    class MyRunnable implements Runnable {

        private IMessage message;

        public MyRunnable(IMessage _message) {
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
                if (splitMessage == "") { message.reply("time is up"); }
                else { message.reply(splitMessage); }

            }catch(Exception e){

            }
        }
    };

    @Override
    public void handleCommand(String command, MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        if (message.getContent().split(" ").length > 1) {
            try {
                Integer time = Integer.parseInt(message.getContent().split(" ")[1]);
                MyRunnable runner = new MyRunnable(event.getMessage());
                ScheduledFuture<?> delayFuture = sch.schedule(runner, time, TimeUnit.MINUTES);
            }
            catch(Exception e) {
                message.reply("time not valid, error: "+e);
            }

        }
        else { message.reply("!timer usage: !timer time (timermessage)");}
    }
}