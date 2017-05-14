package kittenbbq.discordbot;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class BotConfig {
    
    private String token;
    private String db_user;
    private String db_pass;
    private String db_host;
    private String db_port;
    private String db_db;
    private String prefix;
    private int cmd_delete_time;
    private String github_clientid;
    private String github_clientsecret;
    private String github_apirepourl;


    public BotConfig(){
        File configFile = new File("config/botsettings.properties");

        try { 
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            token = props.getProperty("bot_token", "notoken");
            db_user = props.getProperty("db_user", "user");
            db_pass = props.getProperty("db_pass", "pass");
            db_host = props.getProperty("db_host", "localhost");
            db_port = props.getProperty("db_port", "3306");
            db_db = props.getProperty("database_name", "discordbot");
            prefix = props.getProperty("command_prefix", "!");
            cmd_delete_time = Integer.parseInt(props.getProperty("default_command_delete_time", "5"));
            github_clientid = props.getProperty("github_clientid", "");
            github_clientsecret = props.getProperty("github_clientsecret", "");
            github_apirepourl = props.getProperty("github_apirepourl", "");

            reader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getBotToken() {
        return token;
    }

    public String getDBuser() {
        return db_user;
    }

    public String getDBpass() {
        return db_pass;
    }

    public String getDBhost() {
        return db_host;
    }

    public String getDBport() {
        return db_port;
    }

    public String getDatabase() {
        return db_db;
    }

    public String getPrefix() {
        return prefix;
    }
    
    public int getCmdDeleteTime() {
        return cmd_delete_time;
    }

    public String getGithub_clientsecret() {
        return github_clientsecret;
    }

    public void setGithub_clientsecret(String github_clientsecret) {
        this.github_clientsecret = github_clientsecret;
    }

    public String getGithub_clientid() {
        return github_clientid;
    }

    public void setGithub_clientid(String github_clientid) {
        this.github_clientid = github_clientid;
    }

    public String getGithub_apirepourl() {
        return github_apirepourl;
    }

    public void setGithub_apirepourl(String github_apirepourl) {
        this.github_apirepourl = github_apirepourl;
    }
}
