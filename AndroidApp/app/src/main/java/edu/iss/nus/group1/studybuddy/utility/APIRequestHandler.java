package edu.iss.nus.group1.studybuddy.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIRequestHandler {
    protected HttpURLConnection conn;
    protected final Integer timeout = 20000;
    protected String token = null;

    public APIRequestHandler(String apiUrl, String token) throws IOException {
        if (token != null)
            this.token = "Bearer " + token;
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
            if (token != null)
                conn.setRequestProperty("Authorization", token);
            conn.setDoInput(true);

            int responseCode = conn.getResponseCode();
            //System.out.println("Response Code :: " + responseCode);
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
            if (token != null)
                conn.setRequestProperty("Authorization", token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Send json to server
            sendData(jsonData);

            int responseCode = conn.getResponseCode();
            //System.out.println("Response Code :: " + responseCode);
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
            if (token != null)
                conn.setRequestProperty("Authorization", token);
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
            if (token != null)
                conn.setRequestProperty("Authorization", token);
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

    // Post file by android FILE
    public String postFile(InputStream fileStream, String fileName) {
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            conn.setRequestMethod("POST");
            if (token != null)
                conn.addRequestProperty("Authorization", token);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //String boundary = UUID.randomUUID().toString();
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // Open connection to server
            OutputStream os = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            // Include the section to describe the file
            bw.write(twoHyphens + boundary + crlf);
            bw.write("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + crlf);
            bw.write("Content-Transfer-Encoding: binary" + crlf);
            bw.write(crlf);

            bw.flush();

            // Send the actual file contents to server
            //FileInputStream fis = new FileInputStream(uploadFile);
            InputStream fis = fileStream;

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();

            bw.write(crlf + twoHyphens + boundary + twoHyphens + crlf);
            bw.flush();

            // Close streams
            os.close();
            bw.close();

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


    // Send Data to server
    protected void sendData(String jsonData) throws IOException {
        if (jsonData != null) {
            OutputStream os = conn.getOutputStream();
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
            os.close();
        }
    }

    // Get Data from server
    protected String getData() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();
        //System.out.println(response.toString());
        return response.toString();
    }
}
