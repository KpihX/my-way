package com.kpihx_labs.myway.service.mapper;

import com.kpihx_labs.myway.domain.Itinary;
import com.kpihx_labs.myway.domain.Location;
import com.kpihx_labs.myway.domain.LocationCategory;
import com.kpihx_labs.myway.service.dto.ItinaryDTO;
import com.kpihx_labs.myway.service.dto.LocationCategoryDTO;
import com.kpihx_labs.myway.service.dto.LocationDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "category", source = "category", qualifiedByName = "locationCategoryId")
    @Mapping(target = "itinaries", source = "itinaries", qualifiedByName = "itinaryIdSet")
    LocationDTO toDto(Location s);

    @Mapping(target = "itinaries", ignore = true)
    @Mapping(target = "removeItinary", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    @Named("locationCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationCategoryDTO toDtoLocationCategoryId(LocationCategory locationCategory);

    @Named("itinaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItinaryDTO toDtoItinaryId(Itinary itinary);

    @Named("itinaryIdSet")
    default Set<ItinaryDTO> toDtoItinaryIdSet(Set<Itinary> itinary) {
        return itinary.stream().map(this::toDtoItinaryId).collect(Collectors.toSet());
    }
}
