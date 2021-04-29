package com.amd;

import com.amd.service.MessagingService;
import com.amd.service.impl.MessagingServiceImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final int MAX_NUMBER_OF_SMS = 1;
    public static final int SMS_SEND_PERIOD = 10;
    public static final int CANCEL_TASK_DELAY = SMS_SEND_PERIOD * MAX_NUMBER_OF_SMS - 1;

    public static void main(String[] args) {

        MessagingService messagingService = new MessagingServiceImpl();

        // A scheduler with a single thread, that will call the messagingService API in order to send
        // 10 SMS with 10 seconds delay.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledTask = scheduler.scheduleAtFixedRate(messagingService, 0,
                SMS_SEND_PERIOD, TimeUnit.SECONDS);

        // A task to be executed after CANCEL_TASK_DELAY seconds
        // where it terminates the scheduled task and then terminates
        // the scheduler.
        Runnable cancelTask = () -> {
            scheduledTask.cancel(true); // cancel the task
            scheduler.shutdown();
        };

        // call the cancelTask task
        scheduler.schedule(cancelTask, CANCEL_TASK_DELAY, TimeUnit.SECONDS);
    }
}
