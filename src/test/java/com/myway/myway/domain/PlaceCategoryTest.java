package com.myway.myway.domain;

import static com.myway.myway.domain.PlaceCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myway.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaceCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaceCategory.class);
        PlaceCategory placeCategory1 = getPlaceCategorySample1();
        PlaceCategory placeCategory2 = new PlaceCategory();
        assertThat(placeCategory1).isNotEqualTo(placeCategory2);

        placeCategory2.setId(placeCategory1.getId());
        assertThat(placeCategory1).isEqualTo(placeCategory2);

        placeCategory2 = getPlaceCategorySample2();
        assertThat(placeCategory1).isNotEqualTo(placeCategory2);
    }
}
