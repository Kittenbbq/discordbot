package kittenbbq.discordbot;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

public class MessagesDAO {
    public static void addMessage(MessageReceivedEvent e, Db db) {
        MessageDTO msg = new MessageDTO();
        IUser author = e.getAuthor();
        msg.setMessageID(String.valueOf(e.getMessage().getLongID()));
        msg.setAuthorID(String.valueOf(author.getLongID()));
        msg.setAuthorName(author.getName());
        msg.setSent(new Date());
        msg.setGuildID(String.valueOf(e.getGuild().getLongID()));
        msg.setGuildName(e.getGuild().getName());
        msg.setChannelID(String.valueOf(e.getChannel().getLongID()));
        msg.setChannelName(e.getChannel().getName());
        msg.setContent(e.getMessage().getFormattedContent());

        PreparedStatement statement = null;
        try {
            statement = db.getConn().prepareStatement(
                    "INSERT INTO messages(ID, authorID, authorName, sent, guildID, guildName, channelID, channelName, content) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, msg.getMessageID());
            statement.setString(2, msg.getAuthorID());
            statement.setString(3, msg.getAuthorName());
            statement.setTimestamp(4, new Timestamp(msg.getSent().getTime()));
            statement.setString(5, msg.getGuildID());
            statement.setString(6, msg.getGuildName());
            statement.setString(7, msg.getChannelID());
            statement.setString(8, msg.getChannelName());
            statement.setString(9, msg.getContent());
            statement.executeUpdate();
        } catch(Exception ex) {
            System.out.println(ex);
            System.out.println("Message insert failed: " + ex.getMessage());
        }
        finally {
            System.out.println(":D");
            try {
                if (statement != null)
                    statement.close();
            } catch(Exception ex) { }
        }
    }
}
