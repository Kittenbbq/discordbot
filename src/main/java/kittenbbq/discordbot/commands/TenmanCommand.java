package kittenbbq.discordbot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.IVoiceState;

public class TenmanCommand extends AbstractCommandHandler{
    
    private final ArrayList<IUser> players, team1, team2;
    private IVoiceChannel originalChannel;

    public TenmanCommand(BotBase bot) {
        super(bot);
        players = new ArrayList();
        team1 = new ArrayList();
        team2 = new ArrayList();
    }

    @Override
    public String getHelpMessage(String command) {
        switch (command) {
            case "addplayers":
                return "`!addplayers [player1 player2 player3 ...]` adds the given users to the playerlist.";
            case "addchannelplayers":
                return "`!addchannelplayers` adds everyone from your current channel to the playerlist.";
            case "removeplayers":
                return "`!removeplayers [player1 player2 player3 ...]` removes the given users from the playerlist.";
            case "shuffleteams":
                return "`!shuffleteams` creates two randomed teams from the playerlist.";
            case "listplayers":
                return "`!listplayer` lists the users from the playerlist.";
            case "start":
                return "`!start` moves the created teams to their designated voicechannels.";
            case "stop":
                return "`!stop` moves everyone back to the staring voicechannel.";
            case "reset":
                return "`!reset` clears the playerlist and teams.";
            default:
                return "!addplayers|!addchannelplayers|!removeplayers|!shuffleteams|!listplayers|!start|!stop|!reset";
        }
    }

    @Override
    public String[] getCommandList() {
        return new String[]{"addplayers", "addchannelplayers", "removeplayers", "shuffleteams", "listplayers", "start", "stop", "reset"};
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
        if(players.size() == 10){
            Collections.shuffle(players);
            team1.clear();
            team2.clear();
            team1.addAll(players.subList(0, 5));
            team2.addAll(players.subList(5, 10));
            String teamString = "\nTeam 1: ";
            for(IUser user : team1){
                teamString += user.getName() + " ";
            }
            teamString += "\nTeam 2: ";
            for(IUser user : team2){
                teamString += user.getName() + " ";
            }
            sendMessage(teamString);
        }else{
            sendMessage("10 players needs to be added to shuffle. Current playercount: "+players.size());
        }
    }
    
    private void startMatch(){
        IVoiceState voiceState = event.getAuthor().getVoiceStatesLong().get(event.getGuild().getLongID());
        if(voiceState != null){
            originalChannel = voiceState.getChannel();
        }
        IVoiceChannel team1channel = event.getGuild().getVoiceChannelsByName("team1").get(0);
        IVoiceChannel team2channel = event.getGuild().getVoiceChannelsByName("team2").get(0);
        
        for(IUser user : team1){
            if(user.getVoiceStatesLong().get(event.getGuild().getLongID()) != null){
                user.moveToVoiceChannel(team1channel);
            }
        }
        for(IUser user : team2){
            if(user.getVoiceStatesLong().get(event.getGuild().getLongID()) != null){
                user.moveToVoiceChannel(team1channel);
            }
        }
    }
    
    private void endMatch(){
        for(IUser user : players){
            user.moveToVoiceChannel(originalChannel);
        }
    }
    
    private void resetPlayers(){
        players.clear();
        team1.clear();
        team2.clear();
    }
    
    private void listPlayers(){
        String playerString = "";
        for(IUser user : players){
            playerString += user.getName() + " ";
        }
        sendMessage("Players: "+playerString+" Playercount: "+players.size());
    }
}
