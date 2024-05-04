package Filter;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ProfanityFilter {
    /*private static final String API_KEY = "Dhoq0aR5vUZClrtQ5kP7zGcD31kaiJVB7X3GRlwWSXz1e6PB";
    private static final String USER_ID = "yasmine";

    public static String filterText(String text) throws IOException, InterruptedException {
        String apiUrl = "https://neutrinoapi.com/bad-word-filter?user-id=" + USER_ID + "&api-key=" + API_KEY + "&content=" + text;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assuming the JSON response has a key called "censored-content" where the filtered text is stored
        return parseFilteredText(response.body());
    }

    private static String parseFilteredText(String jsonResponse) {
        // Implement JSON parsing here
        return jsonResponse; // Placeholder
    }*/

    private boolean containsProfanity(String text) {
        // Example using HttpClient (Java 11+)
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.example.com/profanityCheck?text=" + URLEncoder.encode(text, StandardCharsets.UTF_8)))
                .header("yasmine", "Dhoq0aR5vUZClrtQ5kP7zGcD31kaiJVB7X3GRlwWSXz1e6PB")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(response.body()); // Assuming the API returns 'true' or 'false' as a string
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false; // Consider profanity detection failure as absence of profanity to avoid blocking legitimate content on failure
        }
    }}
