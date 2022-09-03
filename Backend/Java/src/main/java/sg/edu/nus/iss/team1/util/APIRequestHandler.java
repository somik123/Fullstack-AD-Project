package sg.edu.nus.iss.team1.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIRequestHandler {
    protected HttpURLConnection conn = null;
    protected final Integer timeout = 20000;

    public APIRequestHandler(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(timeout);
        conn.setConnectTimeout(timeout);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
    }

    public String getRequest() {
        try {
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
                return getData(); // Get json reply from server
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public String postRequest(String jsonData) {
        try {
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Send json to server
            sendData(jsonData);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
                return getData(); // Get json reply from server
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public String putRequest(String jsonData) {
        try {
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Send json to server
            sendData(jsonData);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
                return getData(); // Get json reply from server
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public int deleteRequest() {
        try {
            conn.setRequestMethod("GET");
            conn.setDoInput(false);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
                return responseCode; // Reply with response code from server
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return -1;
    }

    // Send Data to server
    protected void sendData(String jsonData) throws IOException {
        OutputStream os = conn.getOutputStream();
        byte[] input = jsonData.getBytes("utf-8");
        os.write(input, 0, input.length);
    }

    // Get Data from server
    protected String getData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        System.out.println(response.toString());
        return response.toString();
    }
}
