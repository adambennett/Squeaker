package rocks.zipcode.squeaker.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqueaksMapperTest {

    private SqueaksMapper squeaksMapper;

    @BeforeEach
    public void setUp() {
        squeaksMapper = new SqueaksMapperImpl();
    }
}
