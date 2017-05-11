package analytics.Controllers;

import analytics.Models.*;
import kittenbbq.discordbot.BotBase;
import kittenbbq.discordbot.BotConfig;
import kittenbbq.discordbot.Db;
import org.springframework.web.bind.annotation.*;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.MessageBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    public MessageController() {
        BotConfig config = new BotConfig();
        this.client = BotBase.createClient(config.getBotToken(), true);
    }
    IDiscordClient client;
    private Db db = new Db(new BotConfig());

    @RequestMapping("/test")
    public String test() {
        return ":D";
    }

    // FOR TESTING REMOVE ME
    /*@RequestMapping("/api/messages")
    public List<MessageDTO> messages() {
        ResultSet results;
        PreparedStatement statement;
        List<MessageDTO> messages = new ArrayList<>();

        try {
            statement = db.getConn().prepareStatement("SELECT * FROM messages");
            results = statement.executeQuery();
            while(results.next()) {
                MessageDTO msg = new MessageDTO();
                msg.setMessageID(results.getString("ID"));
                msg.setAuthorID(results.getString("authorID"));
                msg.setAuthorName(results.getString("authorName"));
                msg.setSent(results.getTimestamp("sent"));
                msg.setGuildID(results.getString("guildID"));
                msg.setGuildName(results.getString("guildName"));
                msg.setChannelID(results.getString("channelID"));
                msg.setChannelName(results.getString("channelName"));
                msg.setContent(results.getString("content"));
                messages.add(msg);
            }
        } catch(Exception e) {
             ;
            System.out.println("Database query failed: : " + e.getMessage());
        }

        return messages;
    }*/

    /**
     * GET /api/messages/info
     * General information of sent messages.
     **/
    @RequestMapping("/api/messages/info")
    public MessageInfo getMessageInfo() {
        MessageInfo msgInfo = null;
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement("CALL messageInfo()");
            results = statement.executeQuery();
            msgInfo = new MessageInfo();
            if (results.first()) {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                msgInfo.setMessageCount(results.getInt("totalMessages"));
                msgInfo.setFirstMessage(f.format(results.getTimestamp("firstMessage")));
                msgInfo.setLastMessage(f.format(results.getTimestamp("lastMessage")));
            }
        } catch(Exception e) {
            System.out.println("Database query failed: : " + e.getMessage());
        }

        return msgInfo;
    }

    /**
     * GET /api/messages/countByDate
     * Gets message count by date.
     */
    @RequestMapping("/api/messages/countByDate")
    public List<MessageCount> getCountByDate(
            @RequestParam(value = "fromDate", defaultValue = "2010-01-01") String fromDate,
            @RequestParam(value = "toDate", defaultValue = "2030-01-01") String toDate
    ) {
        List<MessageCount> counts = new ArrayList<>();
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement(
                String.format("CALL messageCountByDate('%s','%s')", fromDate, toDate)
            );
            results = statement.executeQuery();
            while (results.next()) {
                MessageCount tmpCount = new MessageCount();
                tmpCount.setDate(results.getString("date"));
                tmpCount.setMessageCount(results.getInt("messageCount"));
                counts.add(tmpCount);
            }
        } catch(Exception e) {
            System.out.println("Database query failed: : " + e.getMessage());
        }
        return counts;
    }

    /**
     * GET /api/messages/countByAuthor
     * Gets message count by date.
     */
    @RequestMapping("/api/messages/countByAuthor")
    public List<MessageCountByAuthor> getCountByAuthor(
            @RequestParam(value = "fromDate", defaultValue = "2010-01-01") String fromDate,
            @RequestParam(value = "toDate", defaultValue = "2030-01-01") String toDate
    ) {
        List<MessageCountByAuthor> counts = new ArrayList<>();
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement(
                    String.format("CALL messageCountByAuthor('%s','%s')", fromDate, toDate)
            );
            results = statement.executeQuery();
            while (results.next()) {
                MessageCountByAuthor tmpCount = new MessageCountByAuthor();
                tmpCount.setAuthorID(results.getLong("authorID"));
                tmpCount.setAuthorName(results.getString("authorName"));
                tmpCount.setMessageCount(results.getInt("messageCount"));
                counts.add(tmpCount);
            }
        } catch(Exception e) {
            System.out.println("Database query failed: : " + e.getMessage());
        }
        return counts;
    }

    /**
     * GET /api/messages/countByDay
     * Gets message count by day.
     */
    @RequestMapping("/api/messages/countByDay")
    public List<MessageCountByDay> getCountByDay(
            @RequestParam(value = "fromDate", defaultValue = "2010-01-01") String fromDate,
            @RequestParam(value = "toDate", defaultValue = "2030-01-01") String toDate
    ) {
        List<MessageCountByDay> counts = new ArrayList<>();
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement(
                    String.format("CALL messageCountByDay('%s','%s')", fromDate, toDate)
            );
            results = statement.executeQuery();
            while (results.next()) {
                MessageCountByDay tmpCount = new MessageCountByDay();
                tmpCount.setDay(results.getString("day"));
                tmpCount.setMessageCount(results.getInt("messageCount"));
                counts.add(tmpCount);
            }
        } catch(Exception e) {
            System.out.println("Database query failed: : " + e.getMessage());
        }
        return counts;
    }


    /**
     * GET /api/messages/countByDayHour
     * Gets message count by day-hour.
     */
    @RequestMapping("/api/messages/countByDayHour")
    public List<MessageCountByDayHour> getCountByDayHour(
            @RequestParam(value = "fromDate", defaultValue = "2010-01-01") String fromDate,
            @RequestParam(value = "toDate", defaultValue = "2030-01-01") String toDate
    ) {
        List<MessageCountByDayHour> counts = new ArrayList<>();
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement(
                    String.format("CALL messageCountByDayHour('%s','%s')", fromDate, toDate)
            );
            results = statement.executeQuery();
            while (results.next()) {
                MessageCountByDayHour tmpCount = new MessageCountByDayHour();
                tmpCount.setDay(results.getString("day"));
                tmpCount.setHour(results.getInt("hour"));
                tmpCount.setMessageCount(results.getInt("messageCount"));
                counts.add(tmpCount);
            }
        } catch(Exception e) {
            System.out.println("Database query failed: : " + e.getMessage());
        }
        return counts;
    }


    /**
     * GET /api/messages/countByDayHour
     * Gets message count by hour of day (0-23).
     */
    @RequestMapping("/api/messages/countByHour")
    public List<MessageCountByHour> getCountByHour(
            @RequestParam(value = "fromDate", defaultValue = "2010-01-01") String fromDate,
            @RequestParam(value = "toDate", defaultValue = "2030-01-01") String toDate
    ) {
        List<MessageCountByHour> counts = new ArrayList<>();
        ResultSet results;
        PreparedStatement statement;

        try {
            statement = db.getConn().prepareStatement(
                    String.format("CALL messageCountByHour('%s','%s')", fromDate, toDate)
            );
            results = statement.executeQuery();
            while (results.next()) {
                MessageCountByHour tmpCount = new MessageCountByHour();
                tmpCount.setHour(results.getInt("hour"));
                tmpCount.setMessageCount(results.getInt("messageCount"));
                counts.add(tmpCount);
            }
        } catch(Exception e) {
            System.out.println("Database query failed: : " + e.getMessage());
        }
        return counts;
    }


    /**
     * POST /api/sendMessage
     * Sends a message with discordClient to specified channel.
     * Request body must contain message and channelID.
     */
    @RequestMapping("/api/sendMessage")
    @ResponseBody
    public PostResult sendMessage(@RequestBody SendMessageBody body) {
        PostResult result = new PostResult();

        String message = body.getMessage();
        long channelID = body.getChannelID();

        System.out.println(message + " + " + channelID);
        try {
            new MessageBuilder(this.client).withChannel(219436014412759050L).withContent(message).send();
            result.setSuccess(true);
        } catch(Exception e) {
            result.setError(e.getMessage());
            System.out.println(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }
}