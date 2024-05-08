package com.kpihx_labs.myway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpihx_labs.myway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FrequentItinaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FrequentItinaryDTO.class);
        FrequentItinaryDTO frequentItinaryDTO1 = new FrequentItinaryDTO();
        frequentItinaryDTO1.setId(1L);
        FrequentItinaryDTO frequentItinaryDTO2 = new FrequentItinaryDTO();
        assertThat(frequentItinaryDTO1).isNotEqualTo(frequentItinaryDTO2);
        frequentItinaryDTO2.setId(frequentItinaryDTO1.getId());
        assertThat(frequentItinaryDTO1).isEqualTo(frequentItinaryDTO2);
        frequentItinaryDTO2.setId(2L);
        assertThat(frequentItinaryDTO1).isNotEqualTo(frequentItinaryDTO2);
        frequentItinaryDTO1.setId(null);
        assertThat(frequentItinaryDTO1).isNotEqualTo(frequentItinaryDTO2);
    }
}
