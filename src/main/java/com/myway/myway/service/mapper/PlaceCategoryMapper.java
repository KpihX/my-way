package com.myway.myway.service.mapper;

import com.myway.myway.domain.PlaceCategory;
import com.myway.myway.service.dto.PlaceCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlaceCategory} and its DTO {@link PlaceCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaceCategoryMapper extends EntityMapper<PlaceCategoryDTO, PlaceCategory> {}
