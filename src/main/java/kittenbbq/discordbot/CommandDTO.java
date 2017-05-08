package kittenbbq.discordbot;

public class CommandDTO {
    private String command;
    private String response;
    private String username;
    
    public String getCommand() {
        return command;
    }
    public String getUsername() {
        return username;
    }
    public String getResponse() {
        return response;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    
    public String toString(){
        return command + " " + response;
    }
}
