package analytics;

import kittenbbq.discordbot.BotConfig;
import kittenbbq.discordbot.Db;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    private Db db = new Db(new BotConfig());

    @RequestMapping("/test")
    public String test() {
        return ":D";
    }

    @RequestMapping("/api/messages")
    public List<Message> messages() {
        ResultSet results;
        PreparedStatement statement;
        List<Message> messages = new ArrayList<>();

        try {
            statement = db.getConn().prepareStatement("SELECT * FROM messages");
            results = statement.executeQuery();
            while(results.next()) {
                Message msg = new Message();
                msg.setMessageID(results.getString("ID"));
                msg.setAuthorID(results.getString("authorID"));
                msg.setAuthorName(results.getString("authorName"));
                msg.setSent(results.getTimestamp("sent").toLocalDateTime());
                msg.setGuildID(results.getString("guildID"));
                msg.setGuildName(results.getString("guildName"));
                msg.setChannelID(results.getString("channelID"));
                msg.setChannelName(results.getString("channelName"));
                msg.setContent(results.getString("content"));
                messages.add(msg);
            }
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("Message retrieval failed: " + e.getMessage());
        }

        return messages;
    }

    @RequestMapping("/api/messages/info")
    public MessageInfo info() {
        MessageInfo msgInfo = null;
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement("SELECT COUNT(*) as messageCount from messages");
            results = statement.executeQuery();
            msgInfo = new MessageInfo();
            if (results.first()) {
                msgInfo.setMessageCount(results.getInt("messageCount"));
            }
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("Message retrieval failed: " + e.getMessage());
        }

        return msgInfo;
    }
}
