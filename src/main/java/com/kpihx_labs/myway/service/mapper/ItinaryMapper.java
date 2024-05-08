package com.kpihx_labs.myway.service.mapper;

import com.kpihx_labs.myway.domain.Itinary;
import com.kpihx_labs.myway.domain.Location;
import com.kpihx_labs.myway.service.dto.ItinaryDTO;
import com.kpihx_labs.myway.service.dto.LocationDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Itinary} and its DTO {@link ItinaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItinaryMapper extends EntityMapper<ItinaryDTO, Itinary> {
    @Mapping(target = "locations", source = "locations", qualifiedByName = "locationNameSet")
    ItinaryDTO toDto(Itinary s);

    @Mapping(target = "removeLocation", ignore = true)
    Itinary toEntity(ItinaryDTO itinaryDTO);

    @Named("locationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LocationDTO toDtoLocationName(Location location);

    @Named("locationNameSet")
    default Set<LocationDTO> toDtoLocationNameSet(Set<Location> location) {
        return location.stream().map(this::toDtoLocationName).collect(Collectors.toSet());
    }
}
