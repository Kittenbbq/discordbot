package kittenbbq.discordbot;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ChangeTopicCommand extends AbstractCommandHandler{

    public ChangeTopicCommand(IDiscordClient client) {
        super(client);
    }
    
    @Override
    public void handleCommand(String command, MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();
        List<IRole> userroles = user.getRolesForGuild(message.getGuild());
        IChannel channel = message.getChannel();
        if(inRoles(userroles, "Admin")){
            channel.changeTopic(getCommandContent(message));
        }
    }
}
