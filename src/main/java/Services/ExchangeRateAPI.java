package Services;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateAPI {
    private static final String API_KEY = "4d1fe7b1a33b2a238069b621";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";
    private static Map<String, JsonObject> cache = new HashMap<>();

    public static double getExchangeRate(String baseCurrency, String targetCurrency) throws IOException {
        // Check cache first
        if (cache.containsKey(baseCurrency)) {
            JsonObject rates = cache.get(baseCurrency);
            return rates.getJsonObject("conversion_rates").getJsonNumber(targetCurrency).doubleValue();
        }

        // Build the URL using the base currency
        String urlString = BASE_URL + "/" + API_KEY + "/latest/" + baseCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parse the response with JSON-P
                try (JsonReader jsonReader = Json.createReader(new StringReader(response.toString()))) {
                    JsonObject jsonResponse = jsonReader.readObject();
                    cache.put(baseCurrency, jsonResponse); // Cache the response
                    return jsonResponse.getJsonObject("conversion_rates").getJsonNumber(targetCurrency).doubleValue();
                }
            }
        } else {
            throw new IOException("Error: HTTP response code: " + responseCode);
        }
    }
}
