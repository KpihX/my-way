package com.myway.myway.service.mapper;

import com.myway.myway.domain.Itinary;
import com.myway.myway.domain.User;
import com.myway.myway.service.dto.ItinaryDTO;
import com.myway.myway.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Itinary} and its DTO {@link ItinaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItinaryMapper extends EntityMapper<ItinaryDTO, Itinary> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    ItinaryDTO toDto(Itinary s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
