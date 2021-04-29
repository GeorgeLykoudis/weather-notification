package com.amd.service.impl;

import com.amd.service.MessagingService;
import com.amd.service.WeatherService;

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
    }
}
