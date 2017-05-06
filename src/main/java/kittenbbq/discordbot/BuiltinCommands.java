package kittenbbq.discordbot;

import sx.blah.discord.api.IDiscordClient;

import java.util.HashMap;

public class BuiltinCommands {
    private HashMap commands;

    public BuiltinCommands(IDiscordClient client, CommandsDAO dao) {
        PollCommandHandler pollCommandHandler  = new PollCommandHandler(client);
        commands = new HashMap<String, AbstractBuiltinCommandHandler>();

        commands.put("topic", new TopicCommandHandler(client));
        commands.put("poll", pollCommandHandler);
        commands.put("vote", pollCommandHandler);
    }

    public  boolean commandExists(String commandName) {
        return commands.containsKey(commandName);
    }

    public AbstractBuiltinCommandHandler getCommand(String commandName) {
        return (AbstractBuiltinCommandHandler) commands.get(commandName);
    }
}
