package com.myway.myway.service.mapper;

import static com.myway.myway.domain.PlaceAsserts.*;
import static com.myway.myway.domain.PlaceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaceMapperTest {

    private PlaceMapper placeMapper;

    @BeforeEach
    void setUp() {
        placeMapper = new PlaceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlaceSample1();
        var actual = placeMapper.toEntity(placeMapper.toDto(expected));
        assertPlaceAllPropertiesEquals(expected, actual);
    }
}
