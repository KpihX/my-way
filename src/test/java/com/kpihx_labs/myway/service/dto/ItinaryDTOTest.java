package com.kpihx_labs.myway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItinaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItinaryDTO.class);
        ItinaryDTO itinaryDTO1 = new ItinaryDTO();
        itinaryDTO1.setId(1L);
        ItinaryDTO itinaryDTO2 = new ItinaryDTO();
        assertThat(itinaryDTO1).isNotEqualTo(itinaryDTO2);
        itinaryDTO2.setId(itinaryDTO1.getId());
        assertThat(itinaryDTO1).isEqualTo(itinaryDTO2);
        itinaryDTO2.setId(2L);
        assertThat(itinaryDTO1).isNotEqualTo(itinaryDTO2);
        itinaryDTO1.setId(null);
        assertThat(itinaryDTO1).isNotEqualTo(itinaryDTO2);
    }
}
