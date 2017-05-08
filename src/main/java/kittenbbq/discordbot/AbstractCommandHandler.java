package kittenbbq.discordbot;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.RequestBuffer;

abstract class AbstractCommandHandler {
    
    protected final IDiscordClient client;
    
    public AbstractCommandHandler(IDiscordClient client){
        this.client = client;
    }
    
    abstract void handleCommand(String command, MessageReceivedEvent event);
    
    
    protected boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
    }
    
    protected String getCommandContent(IMessage message){
        return message.getContent().split(" ", 2)[1];
    }
    
    protected String[] getCommandArgs(IMessage message){
        return getCommandContent(message).split(" ");
    }
    
    protected void sendMessage(String message, IChannel channel){
        RequestBuffer.request(() ->{
            try {
                new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
