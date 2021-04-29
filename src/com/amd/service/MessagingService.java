package com.amd.service;

/**
 * @author George Lykoudis
 * @version 29/04/2021
 */
public interface MessagingService extends Runnable {

    /**
     * Calls the weather service and sends an SMS to receiver from sender
     * with information about the weather.
     *
     * @throws Exception
     */
    void weatherTemperatureSmsNotification() throws Exception;

}
