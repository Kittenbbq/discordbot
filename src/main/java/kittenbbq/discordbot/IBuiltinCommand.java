package kittenbbq.discordbot;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface IBuiltinCommand {

    void handle(MessageReceivedEvent event, String command, String[] params);
}
