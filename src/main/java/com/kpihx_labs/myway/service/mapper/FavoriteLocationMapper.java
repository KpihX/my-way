package com.kpihx_labs.myway.service.mapper;

import com.kpihx_labs.myway.domain.FavoriteLocation;
import com.kpihx_labs.myway.domain.User;
import com.kpihx_labs.myway.service.dto.FavoriteLocationDTO;
import com.kpihx_labs.myway.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FavoriteLocation} and its DTO {@link FavoriteLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FavoriteLocationMapper extends EntityMapper<FavoriteLocationDTO, FavoriteLocation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    FavoriteLocationDTO toDto(FavoriteLocation s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
