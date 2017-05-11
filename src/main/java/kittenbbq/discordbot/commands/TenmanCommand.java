package kittenbbq.discordbot.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceState;

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
        String[] args;
        switch(command){
            case "addplayers":
                args = getCommandArgs(event.getMessage());
                addPlayers(args);
                break;
            case "addchannelplayers":
                addPlayersFromChannel();
                break;
            case "removeplayers":
                args = getCommandArgs(event.getMessage());
                removePlayers(args);
                break;
            case "shuffleteams":
                shuffleTeams();
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
        List<IUser> tmp_users = guild.getUsers();
        ArrayList<String> playerNames = new ArrayList();
        for(String s: playersToAdd){
            playerNames.add(s.toLowerCase());
        }
        for(IUser user : tmp_users){
            if(playerNames.isEmpty()){
                break;
            }else{
                String username = user.getName().toLowerCase();
                if(playerNames.contains(username)){
                    players.add(user);
                    playerNames.remove(username);
                }
            }
        }
    }
    
    private void addPlayersFromChannel(){
        IVoiceState voiceState = event.getAuthor().getVoiceStatesLong().get(event.getGuild().getLongID());
        if(voiceState != null){
            ArrayList<IUser> channelUsers = (ArrayList)voiceState.getChannel().getConnectedUsers();
            players.addAll(channelUsers);
        }
    }
    
    private void removePlayers(String[] playersToRemove){
        ArrayList<String> playerNames = new ArrayList();
        for(String s: playersToRemove){
            playerNames.add(s.toLowerCase());
        }
        for(Iterator<IUser> it = players.iterator(); it.hasNext();){
            if(playerNames.isEmpty()){
                break;
            }else{
                IUser user = it.next();
                String username = user.getName().toLowerCase();
                if(playerNames.contains(username)){
                    players.remove(user);
                    playerNames.remove(username);
                }
            }
        }
    }
    
    private void shuffleTeams(){
        
    }
    
    private void startMatch(){
        
    }
    
    private void endMatch(){
        
    }
    
    private void resetPlayers(){
        players.clear();
    }
    
    private void listPlayers(){
        for(IUser user : players){
            sendMessage(user.getName());
        }
    }
}
