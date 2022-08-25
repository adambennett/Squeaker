package rocks.zipcode.squeaker.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.squeaker.domain.User;
import rocks.zipcode.squeaker.domain.Utilizer;
import rocks.zipcode.squeaker.service.dto.UserDTO;
import rocks.zipcode.squeaker.service.dto.UtilizerDTO;

/**
 * Mapper for the entity {@link Utilizer} and its DTO {@link UtilizerDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilizerMapper extends EntityMapper<UtilizerDTO, Utilizer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UtilizerDTO toDto(Utilizer s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
