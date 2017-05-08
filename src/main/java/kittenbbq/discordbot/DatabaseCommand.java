package kittenbbq.discordbot;
import java.util.Arrays;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class DatabaseCommand extends AbstractCommandHandler{
    private final BotDAO dao;

    public DatabaseCommand(IDiscordClient client, BotDAO dao) {
        super(client);
        this.dao = dao;
    }

    @Override
    void handleCommand(String command, MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();
        List<IRole> userroles = user.getRolesForGuild(message.getGuild());
        IChannel channel = message.getChannel();
        String[] split = message.getContent().split(" ");
        String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
        
        switch(command){
            case "add":
                if(args.length > 1){
                    if(inRoles(userroles, "ObeseDude")){
                        CommandDTO command_add = new CommandDTO();
                        command_add.setCommand(args[0]);
                        command_add.setResponse(getCommandContent(message).split(" ", 2)[1]);
                        command_add.setUsername(user.getName());
                        dao.addCommand(command_add);
                    }
                }
                break;
            case "remove":
                if(args.length == 1){
                    if(inRoles(userroles, "ObeseDude")){
                        CommandDTO command_rmv = new CommandDTO();
                        command_rmv.setCommand(args[0]);
                        dao.removeCommand(command_rmv);
                    }
                }
                break;
            default:
                CommandDTO command_get = new CommandDTO();
                command_get.setCommand(command);
                CommandDTO response = dao.getCommandResponse(command_get);
                if(response != null){
                    sendMessage(response.getResponse(), channel);
                }
                break;
        }
    }
    
}
