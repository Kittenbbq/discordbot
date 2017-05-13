package kittenbbq.discordbot.commands;

import java.util.List;
import kittenbbq.discordbot.BotBase;
import kittenbbq.discordbot.BotDAO;
import kittenbbq.discordbot.CommandDTO;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class DatabaseCommand extends AbstractCommandHandler{
    
    private final BotDAO dao;

    public DatabaseCommand(BotBase bot, BotDAO dao) {
        super(bot);
        this.dao = dao;
    }

    @Override
    public String getHelpMessage(String command) {
        switch(command) {
            case "add":
                return "!add [commandName] [commandResponse]";
            case "remove":
                return "!remove [commandName]";
            default:
                return "![commandName]";
        }
    }
    
    @Override
    public String[] getCommandList() {
        return new String[]{"add", "remove"};
    }

    @Override
    protected void handleCommand(String command) {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();
        List<IRole> userroles = user.getRolesForGuild(message.getGuild());
        //IChannel channel = message.getChannel();
        //String[] split = message.getContent().split(" ");
        //String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
        String[] args = getCommandArgs();
        
        switch(command){
            case "add":
                if(args.length > 1){
                    if(bot.inRoles(userroles, "ObeseDude")){
                        CommandDTO command_add = new CommandDTO();
                        command_add.setCommand(args[0]);
                        command_add.setResponse(getCommandContent(message).split(" ", 2)[1]);
                        command_add.setUsername(user.getName());
                        dao.addCommand(command_add);
                    }
                } else {
                    sendMessage(getHelpMessage(command));
                }
                break;
            case "remove":
                if(args.length == 1){
                    if(bot.inRoles(userroles, "ObeseDude")){
                        CommandDTO command_rmv = new CommandDTO();
                        command_rmv.setCommand(args[0]);
                        dao.removeCommand(command_rmv);
                    }
                } else {
                    sendMessage(getHelpMessage(command));
                }

                break;
            default:
                CommandDTO command_get = new CommandDTO();
                command_get.setCommand(command);
                CommandDTO response = dao.getCommandResponse(command_get);
                if(response != null){
                    sendMessage(response.getResponse());
                }
                break;
        }
    }
    
}
