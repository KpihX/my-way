package com.myway.myway.service.mapper;

import static com.myway.myway.domain.PlaceCategoryAsserts.*;
import static com.myway.myway.domain.PlaceCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaceCategoryMapperTest {

    private PlaceCategoryMapper placeCategoryMapper;

    @BeforeEach
    void setUp() {
        placeCategoryMapper = new PlaceCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlaceCategorySample1();
        var actual = placeCategoryMapper.toEntity(placeCategoryMapper.toDto(expected));
        assertPlaceCategoryAllPropertiesEquals(expected, actual);
    }
}
