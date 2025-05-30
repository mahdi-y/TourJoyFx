package Filter;

/*public class ProfanityFilter {
    private static final String API_KEY = "Dhoq0aR5vUZClrtQ5kP7zGcD31kaiJVB7X3GRlwWSXz1e6PB";
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


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ProfanityFilter {



      private static final String API_KEY = "ySKGTiiYOdKUnRpme6H5hB8OLbTgeRNAXl727GKXJqeQ8gwy";
      private static final String USER_ID = "tourjoy";
   public static CompletableFuture<Boolean> containsProfanity(String text) {

      String apiUrl = "https://neutrinoapi.com/bad-word-filter?user-id=" + USER_ID + "&api-key=" + API_KEY + "&content=" + text;

         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(apiUrl))
                 .build();

      return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenApply(response -> {
                 System.out.println("Response: " + response.body());
                 return response.body().contains("\"is-bad\":true");
              });
   }

}
