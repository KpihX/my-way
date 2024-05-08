package com.kpihx_labs.myway.service.mapper;

import com.kpihx_labs.myway.domain.FrequentItinary;
import com.kpihx_labs.myway.domain.User;
import com.kpihx_labs.myway.service.dto.FrequentItinaryDTO;
import com.kpihx_labs.myway.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FrequentItinary} and its DTO {@link FrequentItinaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface FrequentItinaryMapper extends EntityMapper<FrequentItinaryDTO, FrequentItinary> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    FrequentItinaryDTO toDto(FrequentItinary s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
