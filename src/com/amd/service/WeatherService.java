package com.amd.service;

import java.net.http.HttpResponse;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public interface WeatherService {

    /**
     * Calls the OpenWeather API in order to retrieve data
     * for the city of Thessaloniki. The call will return
     * the temperatures in Celsius.
     *
     * @return an HttpResponse with String body.
     * @throws Exception
     */
    HttpResponse<String> getWeatherTemperature() throws Exception;

}
