package kittenbbq.discordbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
 
public class TimerCommand extends CommandHandler{

    public TimerCommand(IDiscordClient client) {
        super(client);
    }

    // Create a scheduled thread pool with 5 core threads
    ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(5);

    class MyRunnable implements Runnable {

        private String message;
        private IUser user;

        public MyRunnable(String _message, IUser _user) {
            this.message = _message;
            this.user = _user;
        }

        public void run() {
            try{

                System.out.println("@"+user+": "+message);

            }catch(Exception e){

            }
        }
    };

    @Override
    public void handleCommand(String command, MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String time = getCommandContent((message));
        String messageContent = message.getContent();
        IUser user = message.getAuthor();
        MyRunnable runner = new MyRunnable(messageContent, user);
        ScheduledFuture<?> delayFuture = sch.schedule(runner, Integer.parseInt(time), TimeUnit.SECONDS);

        }

    }