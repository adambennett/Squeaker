package rocks.zipcode.squeaker.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.squeaker.domain.Mentions;
import rocks.zipcode.squeaker.domain.Squeaks;
import rocks.zipcode.squeaker.service.dto.MentionsDTO;
import rocks.zipcode.squeaker.service.dto.SqueaksDTO;

/**
 * Mapper for the entity {@link Mentions} and its DTO {@link MentionsDTO}.
 */
@Mapper(componentModel = "spring")
public interface MentionsMapper extends EntityMapper<MentionsDTO, Mentions> {
    @Mapping(target = "squeaks", source = "squeaks", qualifiedByName = "squeaksId")
    MentionsDTO toDto(Mentions s);

    @Named("squeaksId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SqueaksDTO toDtoSqueaksId(Squeaks squeaks);
}
