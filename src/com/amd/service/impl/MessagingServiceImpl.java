package com.amd.service.impl;

import com.amd.service.MessagingService;
import com.amd.service.WeatherService;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.http.HttpResponse;

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
        HttpResponse weatherApiResponse = weatherService.getWeatherTemperature();
        if(HttpURLConnection.HTTP_OK == weatherApiResponse.statusCode()) {
            double temperature = mapWeatherTemperature(weatherApiResponse.body().toString());
            String smsText = buildSmsText(temperature);
            System.out.println(smsText);
        }

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
