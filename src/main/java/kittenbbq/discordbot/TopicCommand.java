package kittenbbq.discordbot;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by Arto on 3.5.2017.
 */
public class TopicCommand implements IBuiltinCommand {
    @Override
    public void handle(MessageReceivedEvent event, String command, String[] params) {
        if(params[0] != null) {
            IChannel channel = event.getMessage().getChannel();
            channel.changeTopic(params[0]);
        }
    }
}
