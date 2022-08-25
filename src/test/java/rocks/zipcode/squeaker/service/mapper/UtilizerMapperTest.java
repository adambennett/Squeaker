package rocks.zipcode.squeaker.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilizerMapperTest {

    private UtilizerMapper utilizerMapper;

    @BeforeEach
    public void setUp() {
        utilizerMapper = new UtilizerMapperImpl();
    }
}
