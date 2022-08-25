package rocks.zipcode.squeaker.service.mapper;

import org.mapstruct.*;
import rocks.zipcode.squeaker.domain.Squeaks;
import rocks.zipcode.squeaker.domain.Utilizer;
import rocks.zipcode.squeaker.service.dto.SqueaksDTO;
import rocks.zipcode.squeaker.service.dto.UtilizerDTO;

/**
 * Mapper for the entity {@link Squeaks} and its DTO {@link SqueaksDTO}.
 */
@Mapper(componentModel = "spring")
public interface SqueaksMapper extends EntityMapper<SqueaksDTO, Squeaks> {
    @Mapping(target = "utilizer", source = "utilizer", qualifiedByName = "utilizerHandle")
    SqueaksDTO toDto(Squeaks s);

    @Named("utilizerHandle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "handle", source = "handle")
    UtilizerDTO toDtoUtilizerHandle(Utilizer utilizer);
}
