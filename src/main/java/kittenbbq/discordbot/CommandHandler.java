package kittenbbq.discordbot;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;

abstract class CommandHandler {
    protected final IDiscordClient client;
    
    public CommandHandler(IDiscordClient client){
        this.client = client;
    }
    abstract void handleCommand(String command, MessageReceivedEvent event);
    
    
    protected boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
    }
    protected String getContentCommand(IMessage message, String[] args, String command){
        return message.getContent().substring(command.length()+args[0].length()+3);
    }
    protected String getContent(IMessage message, String command){
        return message.getContent().substring(command.length()+1);
    }
    protected void sendMessage(String message, IChannel channel){
        try {
            new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
