package kittenbbq.discordbot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.EnumSet;
import java.util.List;


public class TopicCommandHandler extends AbstractBuiltinCommandHandler {



    public TopicCommandHandler(IDiscordClient client) {
        super(client);
    }

    @Override
    String getHelpMessage() {
        return "!topic [topic]";
    }

    @Override
    void handle(MessageReceivedEvent event, String command) {

        if(getContent() != null && getContent().length() > 0) {

            IUser user = getMessage().getAuthor();

            List<IRole> userRoles = user.getRolesForGuild(getMessage().getGuild());
            EnumSet<Permissions> userPermissions = user.getPermissionsForGuild(getMessage().getGuild());

            if(userPermissions.contains(Permissions.ADMINISTRATOR)) {
                IChannel channel = event.getMessage().getChannel();
                channel.changeTopic(getContent());
            }
        }
    }
}
