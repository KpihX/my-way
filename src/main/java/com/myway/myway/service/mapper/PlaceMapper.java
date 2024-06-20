package com.myway.myway.service.mapper;

import com.myway.myway.domain.Itinary;
import com.myway.myway.domain.Place;
import com.myway.myway.domain.PlaceCategory;
import com.myway.myway.domain.User;
import com.myway.myway.service.dto.ItinaryDTO;
import com.myway.myway.service.dto.PlaceCategoryDTO;
import com.myway.myway.service.dto.PlaceDTO;
import com.myway.myway.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Place} and its DTO {@link PlaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaceMapper extends EntityMapper<PlaceDTO, Place> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "category", source = "category", qualifiedByName = "placeCategoryId")
    @Mapping(target = "itinary", source = "itinary", qualifiedByName = "itinaryId")
    PlaceDTO toDto(Place s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("placeCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlaceCategoryDTO toDtoPlaceCategoryId(PlaceCategory placeCategory);

    @Named("itinaryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItinaryDTO toDtoItinaryId(Itinary itinary);
}
