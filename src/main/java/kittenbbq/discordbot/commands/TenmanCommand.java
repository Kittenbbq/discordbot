package kittenbbq.discordbot.commands;

import java.util.ArrayList;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class TenmanCommand extends AbstractCommandHandler{
    
    private ArrayList<IUser> players, team1, team2;

    public TenmanCommand(BotBase bot) {
        super(bot);
        players = new ArrayList();
        team1 = new ArrayList();
        team2 = new ArrayList();
    }

    @Override
    protected void handleCommand(String command) {
        String[] args = getCommandArgs(event.getMessage());
        switch(command){
            case "addplayers":
                addPlayers(args);
                break;
            case "addchannelplayers":
                addPlayersFromChannel();
                break;
            case "removeplayers":
                removePlayers(args);
                break;
            case "suffleteams":
                suffleTeams();
                break;
            case "listplayers":
                listPlayers();
                break;
            case "start":
                startMatch();
                break;
            case "stop":
                endMatch();
                break;
            case "reset":
                resetPlayers();
                break;
        }
    }
    
    private void addPlayers(String[] playersToAdd) {
        IGuild guild = event.getGuild();
        
    }
    
    private void addPlayersFromChannel(){
        
    }
    
    private void removePlayers(String[] playersToRemove){
        
    }
    
    private void suffleTeams(){
        
    }
    
    private void startMatch(){
        
    }
    
    private void endMatch(){
        
    }
    
    private void resetPlayers(){
        
    }
    
    private void listPlayers(){
        
    }
}
