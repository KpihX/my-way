package com.kpihx_labs.myway.service.mapper;

import com.kpihx_labs.myway.domain.LocationCategory;
import com.kpihx_labs.myway.service.dto.LocationCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocationCategory} and its DTO {@link LocationCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationCategoryMapper extends EntityMapper<LocationCategoryDTO, LocationCategory> {}
