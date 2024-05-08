package com.kpihx_labs.myway.service.mapper;

import static com.kpihx_labs.myway.domain.FavoriteLocationAsserts.*;
import static com.kpihx_labs.myway.domain.FavoriteLocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoriteLocationMapperTest {

    private FavoriteLocationMapper favoriteLocationMapper;

    @BeforeEach
    void setUp() {
        favoriteLocationMapper = new FavoriteLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFavoriteLocationSample1();
        var actual = favoriteLocationMapper.toEntity(favoriteLocationMapper.toDto(expected));
        assertFavoriteLocationAllPropertiesEquals(expected, actual);
    }
}
