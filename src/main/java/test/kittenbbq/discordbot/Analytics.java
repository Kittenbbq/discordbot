package test.kittenbbq.discordbot;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Analytics {
    public static void addMessage(MessageReceivedEvent e) {
        IUser author = e.getAuthor();
        long userID = author.getLongID();
        String userName = author.getName();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        long guildID = e.getGuild().getLongID();
        String guildName = e.getGuild().getName();
        long channelID = e.getChannel().getLongID();
        String channelName = e.getChannel().getName();
        String msg = e.getMessage().getFormattedContent();



        // System.out.println(timestamp + " " + guildName + " / " + channelName + " " + userName + ": " + msg);
    }
}
