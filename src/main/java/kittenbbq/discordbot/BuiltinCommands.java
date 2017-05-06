package kittenbbq.discordbot;

import java.util.HashMap;

public class BuiltinCommands {
    private static HashMap commands;

    static {
        commands = new HashMap<String, Class>();
        commands.put("topic", TopicCommand.class);
    }

    public static boolean commandExists(String commandName) {
        return commands.containsKey(commandName);
    }

    /*
    public static Class getCommandClass(String commandName) {
        return (Class) commands.get(commandName);
    }
    */

    public static IBuiltinCommand getCommand(String commandName) {
        IBuiltinCommand builtinCommand = null;

        try {
            Class commandClass = (Class) commands.get(commandName);
            builtinCommand = (IBuiltinCommand) commandClass.newInstance();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return builtinCommand;

    }
}
