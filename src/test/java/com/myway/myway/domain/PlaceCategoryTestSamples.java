package com.myway.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlaceCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PlaceCategory getPlaceCategorySample1() {
        return new PlaceCategory().id(1L).name("name1");
    }

    public static PlaceCategory getPlaceCategorySample2() {
        return new PlaceCategory().id(2L).name("name2");
    }

    public static PlaceCategory getPlaceCategoryRandomSampleGenerator() {
        return new PlaceCategory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
