package com.myway.myway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlaceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Place getPlaceSample1() {
        return new Place().id(1L).name("name1").displayName("displayName1").image("image1");
    }

    public static Place getPlaceSample2() {
        return new Place().id(2L).name("name2").displayName("displayName2").image("image2");
    }

    public static Place getPlaceRandomSampleGenerator() {
        return new Place()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .displayName(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString());
    }
}
