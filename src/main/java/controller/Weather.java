package controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Weather {



    @FXML
    private TextField cityTextField;
    @FXML
    private ImageView todayWeatherIcon;
    @FXML
    private Label todayTemperatureLabel;
    @FXML
    private ImageView next1, next2, next3, next4, next5;
    @FXML
    private Label w1, w2, w3, w4, w5;
    @FXML
    private Label day1, day2, day3, day4, day5; // These are your day labels

    @FXML
    private VBox mainContainer;

    @FXML
    private Label time, date, amorpm;

    // Replace with your Tomorrow.io API key
    private static final String API_KEY = "GyZ9bRuwniflZSjgTClBrQc5htbH6O9y";

    @FXML
    private void enterCity() {
        String city = cityTextField.getText();
        updateWeather(city);

    }
    @FXML
    public void initialize() {
        updateDateTime();
        updateDayLabels();

    }




    private void updateDayLabels() {
        LocalDate today = LocalDate.now();
        // Update labels to show the next days of the week
        day1.setText(today.plusDays(1).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        day2.setText(today.plusDays(2).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        day3.setText(today.plusDays(3).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        day4.setText(today.plusDays(4).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        day5.setText(today.plusDays(5).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
    }
    private void updateDateTime() {
        // Using system default timezone
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.systemDefault());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        DateTimeFormatter amPmFormatter = DateTimeFormatter.ofPattern("a");

        time.setText(zdt.format(timeFormatter));
        date.setText(zdt.format(dateFormatter));
        amorpm.setText(zdt.format(amPmFormatter));
    }

    private void updateWeather(String city) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(5); // Fetch data for the next 5 days

            String url = "https://api.tomorrow.io/v4/timelines?location=" + encodedCity +
                    "&fields=temperature&timesteps=1d" +
                    "&startTime=" + today.format(DateTimeFormatter.ISO_DATE) +
                    "&endTime=" + endDate.format(DateTimeFormatter.ISO_DATE) +
                    "&apikey=" + API_KEY;

            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("API Response: " + responseBody);
            JSONObject jsonObject = new JSONObject(responseBody);
            parseAndDisplayWeather(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseAndDisplayWeather(JSONObject jsonObject) {
        if (jsonObject.has("data")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray timelines = data.getJSONArray("timelines");
            JSONObject todayTimeline = timelines.getJSONObject(0);
            JSONArray todayIntervals = todayTimeline.getJSONArray("intervals");
            JSONObject todayWeather = todayIntervals.getJSONObject(0).getJSONObject("values");
            double todayTemp = todayWeather.getDouble("temperature");
            todayTemperatureLabel.setText(String.format("%.1f°C", todayTemp));

            // Assuming there's only one timeline with intervals for each day
            for (int i = 1; i < todayIntervals.length(); i++) {
                JSONObject interval = todayIntervals.getJSONObject(i);
                JSONObject values = interval.getJSONObject("values");
                double temperature = values.getDouble("temperature");

                // Debugging log
                System.out.println("Day " + i + ": " + temperature);

                switch (i) {
                    case 1:
                        w1.setText(String.format("%.1f°C", temperature));
                        break;
                    case 2:
                        w2.setText(String.format("%.1f°C", temperature));
                        break;
                    case 3:
                        w3.setText(String.format("%.1f°C", temperature));
                        break;
                    case 4:
                        w4.setText(String.format("%.1f°C", temperature));
                        break;
                    case 5:
                        w5.setText(String.format("%.1f°C", temperature));
                        break;
                }
            }
        }
    }



    private String getIconPath(String weatherCondition) {
        String imagePath = null;
        switch (weatherCondition.toLowerCase()) {
            case "clear":
                imagePath = "/images/sun.png";
                break;
            case "clouds":
                imagePath = "/images/cloud.png";
                break;
            case "rain":
                imagePath = "/images/rain.png";
                break;
            case "snow":
                imagePath = "/images/snow.png";
                break;
            default:
                imagePath = "/images/default.png";
                break;
        }
        URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl == null) {
            System.out.println("Image not found: " + imagePath);
            return null; // Or handle some default case
        }
        return imgUrl.toExternalForm();
    }

}
