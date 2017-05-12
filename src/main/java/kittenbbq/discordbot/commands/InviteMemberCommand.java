package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IInvite;

public class InviteMemberCommand extends AbstractCommandHandler{
    
    public InviteMemberCommand(BotBase bot) {
        super(bot);
    }

    @Override
    public String getHelpMessage(String command) {
        return "!invite";
    }

    @Override
    public void handleCommand(String command, MessageReceivedEvent event) {
        IChannel channel = event.getMessage().getChannel();
        IInvite invite = channel.createInvite(300, 1, false, false);
        sendMessage("https://discord.gg/"+invite.getCode(), channel);
    }
}
