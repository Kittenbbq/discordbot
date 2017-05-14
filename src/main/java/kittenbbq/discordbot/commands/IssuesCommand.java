package kittenbbq.discordbot.commands;

import kittenbbq.discordbot.BotBase;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;

public class IssuesCommand extends AbstractCommandHandler {
    private final String clientId;
    private final String clientSecret;
    private final String apiRepoBaseUrl;

    public IssuesCommand(BotBase bot) {
        super(bot);

        this.clientId = bot.getConfig().getGithub_clientid();
        this.clientSecret = bot.getConfig().getGithub_clientsecret();
        this.apiRepoBaseUrl = bot.getConfig().getGithub_apirepourl();
    }

    @Override
    public String getHelpMessage(String command) {
        return "`!issues` gets the open issues from DiscordBot GitHub repository.";
    }

    @Override
    public String[] getCommandList() {
        return new String[]{"issues"};
    }

    @Override
    protected void handleCommand(String command) {
        IMessage message = event.getMessage();

        if (clientId == "" || clientSecret == "" || apiRepoBaseUrl == "") {
            sendMessage("Whoops! I have been configured incorrectly. Check the GitHub configs and try again.");
            return;
        }

        try {
            int maxIssueCount = 5;
            String issuesUrl = "";
            StringBuilder issuesListString = new StringBuilder();

            String query = "/issues?sort=updated&direction=desc&state=open&client_id=" + clientId + "&client_secret=" + clientSecret;

            URL url = new URL(apiRepoBaseUrl + query);
            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.connect();

            if (request.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append('\r');
                }

                br.close();
                String jsonResponse = sb.toString();
                JSONArray jsonIssuesArray = new JSONArray(jsonResponse);
                int issueCount = 0;
                issueCount = jsonIssuesArray.length();

                // Append issue lines to message
                for (int i = 0; i < issueCount; i++) {
                    // Info displayed for each issue
                    int issueNumber = -1;
                    String issueTitle = "";
                    String issueLink = "";

                    // Only display a maximum of maxIssueCount issues
                    if ((i + 1) >= maxIssueCount) break;

                    JSONObject jsonIssue = jsonIssuesArray.getJSONObject(i);
                    issueNumber = jsonIssue.getInt("number");
                    issueTitle = jsonIssue.getString("title");
                    issueLink = jsonIssue.getString("html_url");

                    issuesListString.append(MessageFormat.format("\n[#{0}]({1}): {2}", issueNumber, issueLink, issueTitle));

                    // Get issues URL (ugly)
                    if (issuesUrl == "") issuesUrl = issueLink.substring(0, issueLink.lastIndexOf('/'));
                }

                // ..and {0} more issues entry
                if (issueCount > maxIssueCount) {
                    int remainingIssueCount = issueCount - maxIssueCount;
                    issuesListString.append(MessageFormat.format("\n\n..and {0} more.", remainingIssueCount));
                }

                if (issueCount == 0) issuesListString.append("I am perfect!");

                // Append main field to embed
                EmbedBuilder builder = new EmbedBuilder();
                builder.appendField(MessageFormat.format(
                        "I have {0} open issue{1} at GitHub", issueCount == 0 ? "no" : issueCount, issueCount == 1 ? "" : "s"), issuesListString.toString(), false);

                // See all issues entry
                if (issueCount > 0) {
                    builder.withFooterText(MessageFormat.format("\nSee all issues at {0}", issuesUrl));
                    builder.withColor(70, 70, 200);
                } else {
                    builder.withColor(70, 200, 70);
                }

                RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
            } else {
                sendMessage("Whoops! I could not connect to GitHub API.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("An error occurred: " + e);
        }
    }
}
