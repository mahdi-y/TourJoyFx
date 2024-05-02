package services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.json.JSONObject;

public class WikipediaAPI {

    private HttpClient client;

    public WikipediaAPI() {
        client = HttpClient.newHttpClient();
    }

    public String getMonumentDescription(String monumentName) {
        String url = "https://en.wikipedia.org/api/rest_v1/page/summary/" + monumentName.replace(" ", "_");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                return jsonResponse.getString("extract");
            } else {
                return "Failed to get description: Status code " + response.statusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching description";
        }
    }
}
