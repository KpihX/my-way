package com.kpihx_labs.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocationCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LocationCategory getLocationCategorySample1() {
        return new LocationCategory().id(1L).name("name1");
    }

    public static LocationCategory getLocationCategorySample2() {
        return new LocationCategory().id(2L).name("name2");
    }

    public static LocationCategory getLocationCategoryRandomSampleGenerator() {
        return new LocationCategory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
