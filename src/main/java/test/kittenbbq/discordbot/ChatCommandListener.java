package test.kittenbbq.discordbot;
import java.util.Arrays;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageBuilder;

public class ChatCommandListener implements IListener<MessageReceivedEvent>{
    private final IDiscordClient client;
    private final String prefix;
    private final CommandsDAO dao;
    public ChatCommandListener(IDiscordClient client, BotConfig config){
        this.client = client;
        this.prefix = config.getPrefix();
        dao = new CommandsDAO(config);
    }
    
    @Override
    public void handle(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();
        if (user.isBot()) return;
        List<IRole> userroles = user.getRolesForGuild(message.getGuild());
        
        IChannel channel = message.getChannel();
        String[] split = message.getContent().split(" ");
        if (split.length >= 1 && split[0].startsWith(prefix)) {
            String command = split[0].replaceFirst(prefix, "");
            String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
            
            switch(command){
                case "add":
                    if(args.length > 1){
                        if(inRoles(userroles, "ObeseDude")){
                            CommandDTO command_add = new CommandDTO();
                            command_add.setCommand(args[0]);
                            command_add.setResponse(getContentCommand(message, args, command));
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
                case "topic":
                    if(args.length > 0){
                        if(inRoles(userroles, "Admin")){
                            channel.changeTopic(getContent(message, command));
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
    private boolean inRoles(List<IRole> roles, String roleToCheck){
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
    }
    private String getContentCommand(IMessage message, String[] args, String command){
        return message.getContent().substring(command.length()+args[0].length()+3);
    }
    private String getContent(IMessage message, String command){
        return message.getContent().substring(command.length()+1);
    }
    private void sendMessage(String message, IChannel channel){
        try {
            new MessageBuilder(this.client).withChannel(channel).withContent(message).build();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
