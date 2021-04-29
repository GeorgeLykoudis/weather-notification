package com.amd.service.impl;

import com.amd.exceptions.CityNotFoundException;
import com.amd.exceptions.NotAuthorizedException;
import com.amd.service.WeatherService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public class WeatherServiceImpl implements WeatherService {

    private static final String API_KEY     = "b385aa7d4e568152288b3c9f5c2458a5";
    private static final String CITY_NAME   = "Thessaloniki";
    private static final String UNIT_METRIC = "metric"; // In order to get temperature in Celsius.
    private static final String URL         = "https://api.openweathermap.org/data/2.5/weather?q=" +
            CITY_NAME + "&units=" + UNIT_METRIC + "&appid=" + API_KEY;

    /**
     * Calls the OpenWeather API in order to retrieve data
     * for the city of Thessaloniki. The call will return
     * the temperatures in Celsius.
     *
     * @return an HttpResponse with String as body type.
     */
    @Override
    public HttpResponse<String> getWeatherTemperature() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new CityNotFoundException();
            }
            // unauthorized request
            if(response.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new NotAuthorizedException();
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (CityNotFoundException | NotAuthorizedException e) {
        System.out.println(e.getMessage());
        }
        return response;
    }
}
