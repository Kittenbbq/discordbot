package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IInvite;

public class InviteMemberCommand extends AbstractCommandHandler{
    
    public InviteMemberCommand(BotBase bot) {
        super(bot);
    }
    
    @Override
    public String[] getCommandList() {
        return new String[]{"invite"};
    }

    @Override
    protected void handleCommand(String command) {
        IChannel channel = event.getMessage().getChannel();
        IInvite invite = channel.createInvite(300, 1, false, false);
        sendMessage("https://discord.gg/"+invite.getCode(), channel);
    }
}
