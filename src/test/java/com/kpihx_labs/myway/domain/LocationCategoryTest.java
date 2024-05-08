package com.kpihx_labs.myway.domain;

import static com.kpihx_labs.myway.domain.LocationCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationCategory.class);
        LocationCategory locationCategory1 = getLocationCategorySample1();
        LocationCategory locationCategory2 = new LocationCategory();
        assertThat(locationCategory1).isNotEqualTo(locationCategory2);

        locationCategory2.setId(locationCategory1.getId());
        assertThat(locationCategory1).isEqualTo(locationCategory2);

        locationCategory2 = getLocationCategorySample2();
        assertThat(locationCategory1).isNotEqualTo(locationCategory2);
    }
}
