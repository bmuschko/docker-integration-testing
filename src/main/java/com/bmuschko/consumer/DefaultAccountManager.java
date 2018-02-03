package com.bmuschko.consumer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class DefaultAccountManager implements AccountManager {

    public static final String BASE_URL = "http://localhost:8080/accounts";

    public BigDecimal credit(Long accountId, BigDecimal amount) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "?id=" + accountId);
        HttpURLConnection urlConnection = createConnection(url);
        urlConnection.connect();
        handleResponse(urlConnection);

        String responseBody = readResponseBody(urlConnection);
        BigDecimal balance = parseBalance(responseBody);
        BigDecimal newBalance = balance.add(amount);
        // Persist new balance
        return newBalance;
    }
    
    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestMethod("GET");
        return urlConnection;
    }
    
    private void handleResponse(HttpURLConnection urlConnection) throws IOException {
        int responseCode = urlConnection.getResponseCode();
        String responseMessage = urlConnection.getResponseMessage();

        if (responseCode != 200) {
            throw new IOException(String.format("URL '%s' responded with code %s (%s)", urlConnection.getURL(), responseCode, responseMessage));
        }
    }

    private String readResponseBody(HttpURLConnection urlConnection) throws IOException {
        InputStream is = null;

        try {
            is = urlConnection.getInputStream();
            StringBuffer sb = new StringBuffer();
            int ch;

            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }

            return sb.toString();
        } catch (IOException e) {
             throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private BigDecimal parseBalance(String responseBody) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
        return new BigDecimal((double) jsonObject.get("balance"));
    }
}