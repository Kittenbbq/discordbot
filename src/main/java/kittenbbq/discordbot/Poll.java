package kittenbbq.discordbot;


import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Poll {

    public Poll(String name, String question, String[] options) {
        this.name = name;
        this.question = question;

        this.options = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for(int i = 0; i < options.length; i++) {
            String key = options[i].trim();
            if(!key.isEmpty()) {
                this.options.put(options[i], 0);
            }
        }

    }

    private String name;

    private String question;

    private TreeMap<String, Integer> options;

    public String getName() {
        return name;
    }

    public String getQuestion() {
        return question;
    }

    public TreeMap<String, Integer> getOptions() {
        return options;
    }

    public String getResults() {
        if (name.isEmpty() || question.isEmpty() || options.isEmpty()) {
            return "No poll results :/";
        }
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format(Locale.ENGLISH, ":bar_chart: %s: %s\n", this.name, this.question));

        for( Map.Entry<String, Integer> entry : options.entrySet()) {
            strBuilder.append(String.format(Locale.ENGLISH, "   %s: %s", entry.getKey(), MyTools.numberToNumberEmoticon(entry.getValue())));
        }

        return strBuilder.toString();
    }

    public boolean addVote(String optionName) {
        boolean voteAdded = false;

        Integer count = this.options.get(optionName);
        if(count != null) {
            this.options.put(optionName, count + 1);
            voteAdded = true;
        }

        return voteAdded;
    }
}
