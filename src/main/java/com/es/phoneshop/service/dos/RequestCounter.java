package com.es.phoneshop.service.dos;

import java.time.Instant;

public class RequestCounter {
    private Instant instant;
    private long countOfRequests;

    public RequestCounter(Instant instant, long countOfRequests) {
        this.instant = instant;
        this.countOfRequests = countOfRequests;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public long getCountOfRequests() {
        return countOfRequests;
    }

    public void setCountOfRequests(long countOfRequests) {
        this.countOfRequests = countOfRequests;
    }
}
