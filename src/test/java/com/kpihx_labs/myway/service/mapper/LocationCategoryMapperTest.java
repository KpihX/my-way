package com.kpihx_labs.myway.service.mapper;

import static com.kpihx_labs.myway.domain.LocationCategoryAsserts.*;
import static com.kpihx_labs.myway.domain.LocationCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationCategoryMapperTest {

    private LocationCategoryMapper locationCategoryMapper;

    @BeforeEach
    void setUp() {
        locationCategoryMapper = new LocationCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLocationCategorySample1();
        var actual = locationCategoryMapper.toEntity(locationCategoryMapper.toDto(expected));
        assertLocationCategoryAllPropertiesEquals(expected, actual);
    }
}
