package com.kpihx_labs.myway.domain;

import static com.kpihx_labs.myway.domain.FavoriteLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FavoriteLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteLocation.class);
        FavoriteLocation favoriteLocation1 = getFavoriteLocationSample1();
        FavoriteLocation favoriteLocation2 = new FavoriteLocation();
        assertThat(favoriteLocation1).isNotEqualTo(favoriteLocation2);

        favoriteLocation2.setId(favoriteLocation1.getId());
        assertThat(favoriteLocation1).isEqualTo(favoriteLocation2);

        favoriteLocation2 = getFavoriteLocationSample2();
        assertThat(favoriteLocation1).isNotEqualTo(favoriteLocation2);
    }
}
