package rocks.zipcode.squeaker.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.squeaker.web.rest.TestUtil;

class UtilizerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilizerDTO.class);
        UtilizerDTO utilizerDTO1 = new UtilizerDTO();
        utilizerDTO1.setId(1L);
        UtilizerDTO utilizerDTO2 = new UtilizerDTO();
        assertThat(utilizerDTO1).isNotEqualTo(utilizerDTO2);
        utilizerDTO2.setId(utilizerDTO1.getId());
        assertThat(utilizerDTO1).isEqualTo(utilizerDTO2);
        utilizerDTO2.setId(2L);
        assertThat(utilizerDTO1).isNotEqualTo(utilizerDTO2);
        utilizerDTO1.setId(null);
        assertThat(utilizerDTO1).isNotEqualTo(utilizerDTO2);
    }
}
