package com.amd.service.impl;

import com.amd.service.MessagingService;
import com.amd.service.WeatherService;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public class MessagingServiceImpl implements MessagingService {

    private static final String SENDER_NAME     = "George Lykoudis";
    private static final String RECEIVER_NUMBER = "+306978745957";

    private static final Double TEMPERATURE_FLOOR = 20.0;

    private static final String WEATHER_API_MAIN_ATTRIBUTE      = "main";
    private static final String WEATHER_API_MAIN_TEMP_ATTRIBUTE = "temp";

    private static final String APPLICATION_ID     = "5f9138288b71de3617a87cd3";
    private static final String APPLICATION_SECRET = "RSj69jLowJ";

    private static final String URL_AUTH_TOKEN = "https://auth.routee.net/oauth/token";
    private static final String URL_SMS        = "https://connect.routee.net/sms";

    private static WeatherService weatherService;

    public MessagingServiceImpl() {
        weatherService = new WeatherServiceImpl();
    }

    /**
     * Calls the {@link WeatherService#getWeatherTemperature()} weather} service and
     * sends an SMS to {@link #RECEIVER_NUMBER reciever} from {@link #SENDER_NAME sender}
     * with information about the weather.
     */
    @Override
    public void weatherTemperatureSmsNotification() {
        // Call OpenWeather API
        HttpResponse weatherApiResponse = weatherService.getWeatherTemperature();
        // If the call succeeds then proceed with the calls to Routee
        if(HttpURLConnection.HTTP_OK == weatherApiResponse.statusCode()) {
            double temperature = mapWeatherTemperature(weatherApiResponse.body().toString()); // extract temperature
            String smsText = buildSmsText(temperature); // build the sms that will be sent
            System.out.println(smsText);

            // Get Authenticated from Routee
            HttpResponse authResponse = routeeRestConnection(URL_AUTH_TOKEN, "grant_type=client_credentials",
                    "application/x-www-form-urlencoded", "Basic " + encodeBase64());

            //If user is authenticated then send message
            if(HttpURLConnection.HTTP_OK == authResponse.statusCode()) {
                String accessToken = mapAuthToken(authResponse.body().toString()); // extract access token
                String body = "{ \"body\" : \"" + smsText + "\", \"to\" : \""+ RECEIVER_NUMBER + "\", \"from\": \"amdTelecom\"}";
                HttpResponse smsResponse = routeeRestConnection(URL_SMS, body, "application/json","Bearer "+accessToken);
                if(HttpURLConnection.HTTP_OK == smsResponse.statusCode()) {
                    System.out.println("A message sent");
                }
            }
        }
    }

    /**
     * Make the actual call to Routee API in order to send the message to receiver.
     *
     * @param url the API address.
     * @param body query parameters to be sent in yaml format.
     * @param contentType entity header that indicates the media type of the resource.
     * @param authorization the credentials needed to authenticate the user with the server.
     * @return an HttpResponse with String as a body type.
     */
    private HttpResponse<String> routeeRestConnection(String url, String body, String contentType, String authorization) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Authorization", authorization)
                .header("Content-Type", contentType)
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Parse the response body from the OpenWeather call in order to
     * extract the temperature.
     *
     * @param responseBody the HttpResponse from OpenWeather request
     *                     in JSON format.
     * @return a double with the temperature
     */
    private double mapWeatherTemperature(String responseBody) {
        JSONObject weather = new JSONObject(responseBody);
        JSONObject main_attribute = weather.getJSONObject(WEATHER_API_MAIN_ATTRIBUTE);
        return main_attribute.getDouble(WEATHER_API_MAIN_TEMP_ATTRIBUTE);
    }

    /**
     * Parse the response body from the Routee call in order to
     * extract the access token.
     *
     * @param responseBody the HttpResponse from Routee request
     *                     in JSON format.
     * @return a double with the temperature
     */
    private String mapAuthToken(String responseBody) {
        JSONObject authentication = new JSONObject(responseBody);
        return authentication.getString("access_token");
    }

    /**
     * Encode the application id and application secret in a base64 String.
     *
     * @return a String.
     */
    private String encodeBase64() {
        String input = APPLICATION_ID + ":" + APPLICATION_SECRET;
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    /**
     * Constructs the message to be sent based on the {@code temperature}
     *
     * @param temperature the temperature that retrieved from OpenWeather API.
     * @return a String.
     */
    private String buildSmsText(double temperature) {
        String message;
        if(temperature > TEMPERATURE_FLOOR) {
            message = SENDER_NAME + " and Temperature more than " + TEMPERATURE_FLOOR.intValue() + "C. " + temperature;
        }
        else {
            message = SENDER_NAME + " and Temperature less than " + TEMPERATURE_FLOOR.intValue() + "C. " + temperature;
        }
        return message;
    }
}
