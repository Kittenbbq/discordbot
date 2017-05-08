package kittenbbq.discordbot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.ExtendedInviteObject;
import sx.blah.discord.api.internal.json.requests.InviteCreateRequest;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ExtendedInvite;

public class InviteMemberCommand extends AbstractCommandHandler{
    
    public InviteMemberCommand(IDiscordClient client) {
        super(client);
    }

    @Override
    void handleCommand(String command, MessageReceivedEvent event) {
        InviteCreateRequest invRequest = new InviteCreateRequest(300, 1, false, true);
        ExtendedInviteObject invObj = new ExtendedInviteObject();
        ExtendedInvite invite = new ExtendedInvite(client, invObj);
        System.out.println("https://discord.gg/"+invObj.code);
    }
}
