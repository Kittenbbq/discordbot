package kittenbbq.discordbot;


import sx.blah.discord.handle.obj.IRole;

import java.util.HashMap;
import java.util.List;

public class MyTools {

    private static HashMap<Character, String> numbers = new HashMap<>();

    static {
        numbers.put('0', ":zero:");
        numbers.put('1', ":one:");
        numbers.put('2', ":two:");
        numbers.put('3', ":three:");
        numbers.put('4', ":four:");
        numbers.put('5', ":five:");
        numbers.put('6', ":six:");
        numbers.put('7', ":seven:");
        numbers.put('8', ":eight:");
        numbers.put('9', ":nine:");
    }

    public static boolean inRoles(List<IRole> roles, String roleToCheck) {
        return roles.stream().anyMatch((role) -> (role.toString().equals(roleToCheck)));
    }

    public static String numberToNumberEmoticon(int number) {
        String numberStr = Integer.toString(number);
        StringBuilder strBuilder = new StringBuilder();

        for(int i = 0; i < numberStr.length(); i++) {
            strBuilder.append(numbers.get(numberStr.charAt(i)));
        }

        return strBuilder.toString();
    }
}
