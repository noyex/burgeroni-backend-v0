package com.noyex.service.utils;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class OrderNumberGenerator {
    private static long lastTimeStamp = 0L;
    private static int sequence = 0;

    public static synchronized String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        long currentTimeStamp = System.currentTimeMillis();

        synchronized (OrderNumberGenerator.class) {
            if (currentTimeStamp == lastTimeStamp) {
                sequence++;
                if (sequence > 9999) {
                    try{
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    currentTimeStamp = System.currentTimeMillis();
                    sequence = 0;
                }
            } else {
                sequence = 0;
                lastTimeStamp = currentTimeStamp;
            }
        }
        int randInt = ThreadLocalRandom.current().nextInt(1000);

        return String.format("%02d%02d%02d", day, month, randInt);
    }
}
