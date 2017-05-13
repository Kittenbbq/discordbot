package kittenbbq.discordbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BotDAO {
    
    private Connection mycon;
    
    public BotDAO(BotConfig config){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            mycon = DriverManager.getConnection("jdbc:mysql://"+config.getDBhost()+":"+config.getDBport()+"/"+config.getDatabase(), config.getDBuser(), config.getDBpass());
        }catch(Exception e){
            System.out.println("Creating database connection failed");
            e.printStackTrace();
        }
    }
    
    protected void finalize(){
        try{
            if(mycon != null)
                mycon.close();
        }catch(Exception e){}
    }
    
    public CommandDTO getCommandResponse(CommandDTO command){
        //WTF KITTEN IS THIS FUCKING VALI?!? EXPLAIN YOURSELF.
        CommandDTO vali = null;
        ResultSet results = null;
        PreparedStatement statement = null;
        
        try{
            statement = mycon.prepareStatement("SELECT * FROM commands WHERE command = ?");
            statement.setString(1, command.getCommand());
            results = statement.executeQuery();
            while(results.next()){
                String response = results.getString("response");
                CommandDTO newCommand = new CommandDTO();
                newCommand.setResponse(response);
                vali = newCommand;
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
        return vali;
    }
    
    public void addCommand(CommandDTO newCommand){
        PreparedStatement statement = null;
        try{
            statement = mycon.prepareStatement("INSERT INTO commands(command, response, user) "
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
            statement = mycon.prepareStatement("DELETE FROM commands WHERE command=?");
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
}
