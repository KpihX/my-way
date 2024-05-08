package com.kpihx_labs.myway.repository.rowmapper;

import com.kpihx_labs.myway.domain.LocationCategory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LocationCategory}, with proper type conversions.
 */
@Service
public class LocationCategoryRowMapper implements BiFunction<Row, String, LocationCategory> {

    private final ColumnConverter converter;

    public LocationCategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link LocationCategory} stored in the database.
     */
    @Override
    public LocationCategory apply(Row row, String prefix) {
        LocationCategory entity = new LocationCategory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
