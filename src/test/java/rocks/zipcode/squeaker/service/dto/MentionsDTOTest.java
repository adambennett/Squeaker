package rocks.zipcode.squeaker.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.squeaker.web.rest.TestUtil;

class MentionsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MentionsDTO.class);
        MentionsDTO mentionsDTO1 = new MentionsDTO();
        mentionsDTO1.setId(1L);
        MentionsDTO mentionsDTO2 = new MentionsDTO();
        assertThat(mentionsDTO1).isNotEqualTo(mentionsDTO2);
        mentionsDTO2.setId(mentionsDTO1.getId());
        assertThat(mentionsDTO1).isEqualTo(mentionsDTO2);
        mentionsDTO2.setId(2L);
        assertThat(mentionsDTO1).isNotEqualTo(mentionsDTO2);
        mentionsDTO1.setId(null);
        assertThat(mentionsDTO1).isNotEqualTo(mentionsDTO2);
    }
}
