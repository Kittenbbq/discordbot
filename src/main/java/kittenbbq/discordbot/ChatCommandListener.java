package kittenbbq.discordbot;

import kittenbbq.discordbot.commands.AbstractCommandHandler;
import kittenbbq.discordbot.commands.DatabaseCommand;
import kittenbbq.discordbot.commands.TimerCommand;
import kittenbbq.discordbot.commands.ChangeTopicCommand;
import kittenbbq.discordbot.commands.InviteMemberCommand;
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
        AbstractCommandHandler tmp = new DatabaseCommand(bot, dao);
        
        commands.put("topic", new ChangeTopicCommand(bot));
        commands.put("add", tmp);
        commands.put("remove", tmp);
        commands.put("invite", new InviteMemberCommand(bot));
        commands.put("timer", new TimerCommand(bot));

        defaultHandler = tmp;
        
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

            boolean isHelp = false;

            if(command.equals("help") && split.length == 2) {
                command = split[1];
                isHelp = true;
            }

            AbstractCommandHandler commandHandler = commands.get(command);
            if(commandHandler != null){
                if(isHelp) {
                    commandHandler.sendHelpMessage(command, event.getChannel());
                } else {
                    commandHandler.handleCommand(command, event);
                }
            }else{
                defaultHandler.handleCommand(command, event);
            }
        }
    }
}
