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
    private HashMap<String, AbstractCommandHandler> commands;
    
    public ChatCommandListener(BotBase bot, AbstractCommandHandler defaultcommand){
        commands = new HashMap<>();
        defaultHandler = defaultcommand;
        registerCommand(defaultcommand);
        this.prefix = bot.getConfig().getPrefix();
    }
    
    public final void registerCommand(AbstractCommandHandler command){
        String[] cmdlist = command.getCommandList();
        for(String s : cmdlist){
            commands.put(s, command);
        }
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
                    commandHandler.sendHelpMessage(command, event);
                } else {
                    commandHandler.executeCommand(command, event);
                }
            }else{
                defaultHandler.executeCommand(command, event);
            }
        }
    }
}
