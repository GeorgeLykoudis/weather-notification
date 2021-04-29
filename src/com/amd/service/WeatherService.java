package com.amd.service;

import java.net.http.HttpResponse;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public interface WeatherService {

    HttpResponse<String> getWeatherTemperature();

}
