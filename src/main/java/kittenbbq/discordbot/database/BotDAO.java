package kittenbbq.discordbot.database;

import kittenbbq.discordbot.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class BotDAO {
    
    private Connection connection;
    private String connectionString;
    private String user, pass;
    
    public BotDAO(BotConfig config){
        connectionString = "jdbc:mysql://"+config.getDBhost()+":"+config.getDBport()+"/"+config.getDatabase();
        user = config.getDBuser();
        pass = config.getDBpass();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString, user, pass);
        }catch(Exception e){
            System.out.println("Creating database connection failed");
           // e.printStackTrace();
        }
    }
    
    protected void finalize(){
        try{
            if(connection != null)
                connection.close();
        }catch(Exception e){}
    }
    
    public CommandDTO getCommandResponse(CommandDTO command){
        CommandDTO tmp = null;
        ResultSet results = null;
        PreparedStatement statement = null;
        
        try{
            if(connection != null && !connection.isValid(5)){
                connection.close();
                connection = DriverManager.getConnection(connectionString, user, pass);
            }
            statement = connection.prepareStatement("SELECT * FROM commands WHERE command = ?");
            statement.setString(1, command.getCommand());
            results = statement.executeQuery();
            while(results.next()){
                String response = results.getString("response");
                CommandDTO newCommand = new CommandDTO();
                newCommand.setResponse(response);
                tmp = newCommand;
            }
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Fetching command response failed");
        }
        finally{
            try{
                if(results != null)
                    results.close();
                if(statement != null)
                    statement.close();
            }catch(Exception e){}
        }
        return tmp;
    }
    
    public void addCommand(CommandDTO newCommand){
        PreparedStatement statement = null;
        try{
            if(connection != null && !connection.isValid(5)){
                connection.close();
                connection = DriverManager.getConnection(connectionString, user, pass);
            }
            statement = connection.prepareStatement("INSERT INTO commands(command, response, user) "
                    + "VALUES (?, ?, ?)");
            statement.setString(1, newCommand.getCommand());
            statement.setString(2, newCommand.getResponse());
            statement.setString(3, newCommand.getUsername());
            statement.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Adding command failed");
        }
        finally{
            try{
                if(statement != null)
                    statement.close();
            }catch(Exception e){}
        }
    }
    
    public void removeCommand(CommandDTO oldCommand){
        PreparedStatement statement = null;
        try{
            if(connection != null && !connection.isValid(5)){
                connection.close();
                connection = DriverManager.getConnection(connectionString, user, pass);
            }
            statement = connection.prepareStatement("DELETE FROM commands WHERE command=?");
            statement.setString(1, oldCommand.getCommand());
            statement.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
            System.out.println("Removing command failed");
        }
        finally{
            try{
                if(statement != null)
                    statement.close();
            }catch(Exception e){}
        }
    }

    public void addMessage(MessageReceivedEvent e) {
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

        System.out.println(sent + " " + guildName + " / " + channelName + " " + authorName + ": " + content);

        PreparedStatement statement = null;
        try{
            if(connection != null && !connection.isValid(5)){
                connection.close();
                connection = DriverManager.getConnection(connectionString, user, pass);
            }
            statement = connection.prepareStatement("INSERT INTO messages(ID, authorID, authorName, sent, guildID, guildName, channelID, channelName, content) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setLong(1, messageID);
            statement.setLong(2, authorID);
            statement.setString(3, authorName);
            statement.setString(4, sent);
            statement.setLong(5, guildID);
            statement.setString(6, guildName);
            statement.setLong(7, channelID);
            statement.setString(8, channelName);
            statement.setString(9, content);

            statement.executeUpdate();
        }catch(Exception ex){
            System.out.println(ex);
            System.out.println("Adding command failed");
        }
        finally{
            try{
                if(statement != null)
                    statement.close();
            }catch(Exception exc){}
        }
    }
}
