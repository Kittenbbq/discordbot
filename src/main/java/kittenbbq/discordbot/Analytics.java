package kittenbbq.discordbot;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Analytics {
    public static void addMessage(MessageReceivedEvent e) {
        long messageID = e.getMessage().getLongID();
        IUser author = e.getAuthor();
        long authorID = author.getLongID();
        String authorName = author.getName();
        String sent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        long guildID = e.getGuild().getLongID();
        String guildName = e.getGuild().getName();
        long channelID = e.getChannel().getLongID();
        String channelName = e.getChannel().getName();
        String content = e.getMessage().getFormattedContent();

        //System.out.println(sent + " " + guildName + " / " + channelName + " " + authorName + ": " + content);


    }
}
