package com.kpihx_labs.myway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationCategoryDTO.class);
        LocationCategoryDTO locationCategoryDTO1 = new LocationCategoryDTO();
        locationCategoryDTO1.setId(1L);
        LocationCategoryDTO locationCategoryDTO2 = new LocationCategoryDTO();
        assertThat(locationCategoryDTO1).isNotEqualTo(locationCategoryDTO2);
        locationCategoryDTO2.setId(locationCategoryDTO1.getId());
        assertThat(locationCategoryDTO1).isEqualTo(locationCategoryDTO2);
        locationCategoryDTO2.setId(2L);
        assertThat(locationCategoryDTO1).isNotEqualTo(locationCategoryDTO2);
        locationCategoryDTO1.setId(null);
        assertThat(locationCategoryDTO1).isNotEqualTo(locationCategoryDTO2);
    }
}
