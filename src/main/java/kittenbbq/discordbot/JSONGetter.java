package kittenbbq.discordbot;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class JSONGetter {

    public JSONObject getJSONObject(String url) { return handleHTTPRequest(url); }

    private JSONObject handleHTTPRequest(String url) {

        try {

            URL URLToGet = new URL(url);
            HttpsURLConnection request = (HttpsURLConnection) URLToGet.openConnection();
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
                JSONObject compiledBuffer = new JSONObject(sb.toString());
                return compiledBuffer;
            }
        } catch (Exception e) {

            e.getStackTrace();
            return null;
        }
    return null;
    }
}

