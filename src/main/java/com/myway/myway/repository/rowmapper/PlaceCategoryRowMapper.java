package com.myway.myway.repository.rowmapper;

import com.myway.myway.domain.PlaceCategory;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PlaceCategory}, with proper type conversions.
 */
@Service
public class PlaceCategoryRowMapper implements BiFunction<Row, String, PlaceCategory> {

    private final ColumnConverter converter;

    public PlaceCategoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PlaceCategory} stored in the database.
     */
    @Override
    public PlaceCategory apply(Row row, String prefix) {
        PlaceCategory entity = new PlaceCategory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        return entity;
    }
}
