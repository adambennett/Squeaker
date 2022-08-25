package rocks.zipcode.squeaker.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.squeaker.domain.Squeaks;
import rocks.zipcode.squeaker.domain.Tags;
import rocks.zipcode.squeaker.service.dto.SqueaksDTO;
import rocks.zipcode.squeaker.service.dto.TagsDTO;

/**
 * Mapper for the entity {@link Tags} and its DTO {@link TagsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagsMapper extends EntityMapper<TagsDTO, Tags> {
    @Mapping(target = "squeaks", source = "squeaks", qualifiedByName = "squeaksId")
    TagsDTO toDto(Tags s);

    @Named("squeaksId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SqueaksDTO toDtoSqueaksId(Squeaks squeaks);
}
