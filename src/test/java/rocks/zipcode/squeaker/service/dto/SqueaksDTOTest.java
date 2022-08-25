package rocks.zipcode.squeaker.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.squeaker.web.rest.TestUtil;

class SqueaksDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SqueaksDTO.class);
        SqueaksDTO squeaksDTO1 = new SqueaksDTO();
        squeaksDTO1.setId(1L);
        SqueaksDTO squeaksDTO2 = new SqueaksDTO();
        assertThat(squeaksDTO1).isNotEqualTo(squeaksDTO2);
        squeaksDTO2.setId(squeaksDTO1.getId());
        assertThat(squeaksDTO1).isEqualTo(squeaksDTO2);
        squeaksDTO2.setId(2L);
        assertThat(squeaksDTO1).isNotEqualTo(squeaksDTO2);
        squeaksDTO1.setId(null);
        assertThat(squeaksDTO1).isNotEqualTo(squeaksDTO2);
    }
}
