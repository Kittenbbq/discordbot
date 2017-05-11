package kittenbbq.discordbot;

import kittenbbq.discordbot.commands.*;
import java.util.HashMap;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ChatCommandListener implements IListener<MessageReceivedEvent>{
    
    private final String prefix;
    private final AbstractCommandHandler defaultHandler;
    private final BotDAO dao;
    private HashMap<String, AbstractCommandHandler> commands;
    
    public ChatCommandListener(BotBase bot){
        commands = new HashMap<>();
        dao = new BotDAO(bot.getConfig());
        AbstractCommandHandler dbcommand = new DatabaseCommand(bot, dao);
        AbstractCommandHandler tmcommand = new TenmanCommand(bot);
        
        commands.put("topic", new ChangeTopicCommand(bot));
        commands.put("add", dbcommand);
        commands.put("remove", dbcommand);
        commands.put("invite", new InviteMemberCommand(bot));
        commands.put("timer", new TimerCommand(bot));
        commands.put("addplayers", tmcommand);
        commands.put("addchannelplayers", tmcommand);
        commands.put("removeplayers", tmcommand);
        commands.put("reset", tmcommand);
        commands.put("start", tmcommand);
        commands.put("stop", tmcommand);
        commands.put("shuffleteams", tmcommand);
        commands.put("listplayers", tmcommand);

        defaultHandler = dbcommand;
        
        this.prefix = bot.getConfig().getPrefix();
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
                commandHandler.executeCommand(command, event);
            }else{
                defaultHandler.executeCommand(command, event);
            }
        }
    }
}
