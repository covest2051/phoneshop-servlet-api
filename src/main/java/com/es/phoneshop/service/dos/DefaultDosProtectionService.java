package com.es.phoneshop.service.dos;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 10;

    private Map<String, RequestCounter> requestCounterMap = new ConcurrentHashMap<>();

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        RequestCounter requestCounter = requestCounterMap.get(ip);

        if (requestCounter == null) {
            requestCounter = new RequestCounter(Instant.now(), 1L);

            requestCounterMap.put(ip, requestCounter);
        } else {
            if (Duration.between(requestCounter.getInstant(), Instant.now()).toSeconds() > 20) {
                requestCounter.setCountOfRequests(0);
                requestCounter.setInstant(Instant.now());
            }
            if (requestCounter.getCountOfRequests() > THRESHOLD) {
                return false;
            }

            requestCounter.setCountOfRequests(requestCounter.getCountOfRequests() + 1);
        }
        return true;
    }
}
