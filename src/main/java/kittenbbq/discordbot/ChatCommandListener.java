package kittenbbq.discordbot;
import java.util.Arrays;
import java.util.List;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
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

        String[] split = message.getContent().split(" ");
        if (split.length >= 1 && split[0].startsWith(prefix)) {
            String command = split[0].replaceFirst(prefix, "").toLowerCase();
            String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];

            if (BuiltinCommands.commandExists(command)) {
                BuiltinCommands.getCommand(command).handle(event, command, args);
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
