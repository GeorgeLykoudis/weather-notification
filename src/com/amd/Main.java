package com.amd;

import com.amd.service.MessagingService;
import com.amd.service.impl.MessagingServiceImpl;

public class Main {

    public static void main(String[] args) {

        MessagingService messagingService = new MessagingServiceImpl();
        messagingService.weatherTemperatureSmsNotification();

    }
}
