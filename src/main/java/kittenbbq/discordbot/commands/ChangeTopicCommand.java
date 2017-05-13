package kittenbbq.discordbot.commands;

import java.util.EnumSet;
import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.*;

public class ChangeTopicCommand extends AbstractCommandHandler{

    public ChangeTopicCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        return "`!topic [topic]` changes the topic channels topic.";
    }

    @Override
    public String[] getCommandList() {
        return new String[]{"topic"};
    }
    
    @Override
    protected void handleCommand(String command) {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();

        EnumSet<Permissions> userPermissions = user.getPermissionsForGuild(message.getGuild());

        if(userPermissions.contains(Permissions.ADMINISTRATOR)) {
            IChannel channel = message.getChannel();
            channel.changeTopic(getCommandContent());
        }
    }
}
