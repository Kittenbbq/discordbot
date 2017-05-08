package kittenbbq.discordbot;
import java.util.HashMap;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ChatCommandListener implements IListener<MessageReceivedEvent>{
    private final String prefix;
    private final AbstractCommandHandler defaultHandler;
    private final BotDAO dao;
    private HashMap<String, AbstractCommandHandler> commands;
    
    public ChatCommandListener(IDiscordClient client, BotConfig config){
        commands = new HashMap<>();
        dao = new BotDAO(config);
        AbstractCommandHandler tmp = new DatabaseCommand(client, dao);
        
        commands.put("topic", new ChangeTopicCommand(client));
        commands.put("add", tmp);
        commands.put("remove", tmp);
        defaultHandler = tmp;
        
        this.prefix = config.getPrefix();
    }
    
    @Override
    public void handle(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();
        if (user.isBot()) return;

        String[] split = message.getContent().split(" ");
        if (split.length >= 1 && split[0].startsWith(prefix)) {
            
            String command = split[0].replaceFirst(prefix, "");
            
            AbstractCommandHandler commandHandler = commands.get(command);
            if(commandHandler != null){
                commandHandler.handleCommand(command, event);
            }else{
                defaultHandler.handleCommand(command, event);
            }
        }
    }
}
