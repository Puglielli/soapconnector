package br.com.atelecom.soap.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.nio.charset.StandardCharsets;


public class ServerConnect {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BASIC = "Basic";

    private String endpoint;
    private String user;
    private String pass;
    private int timeout;
    private String response;

    public ServerConnect(String endpoint, String user, String pass, int timeout) {
        this.endpoint = endpoint;
        this.user = user;
        this.pass = pass;
        this.timeout = timeout;
    }

    private HttpURLConnection createConnection() throws IOException {
        URL url = new URL(this.endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(AUTHORIZATION_HEADER, basicAuthentication());
        conn.setDoOutput(true);
        conn.setReadTimeout(this.timeout);
        return conn;
    }

    private String basicAuthentication(){
        return BASIC + " " + Base64.getEncoder().encodeToString((this.user + ":" +
                this.pass).getBytes(StandardCharsets.UTF_8));
    }


    private String readLines(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            reader.lines().forEach(sb::append);
        }
        return sb.toString();
    }

    public int request(String request) throws IOException {
        HttpURLConnection conn = createConnection();
        int responseCode;
        try(OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())){
            writer.write(request);
            writer.flush();

            responseCode = conn.getResponseCode();

            if (responseCode >= 200 && responseCode < 400) {
                this.response = readLines(conn.getInputStream());
            }
            else {
                this.response = readLines(conn.getErrorStream());
            }

        } finally {
            conn.disconnect();
        }

        return responseCode;
    }

    public String getResponse() {
        return this.response;
    }
}