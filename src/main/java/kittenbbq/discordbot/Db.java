package kittenbbq.discordbot;

import java.sql.Connection;
import java.sql.DriverManager;

public class Db {
    private Connection conn;
    public Db(BotConfig config) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + config.getDBhost() + ":" + config.getDBport() + "/" + config.getDatabase(), config.getDBuser(), config.getDBpass());
        } catch(Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}
