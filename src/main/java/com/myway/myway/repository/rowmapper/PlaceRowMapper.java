package com.myway.myway.repository.rowmapper;

import com.myway.myway.domain.Place;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Place}, with proper type conversions.
 */
@Service
public class PlaceRowMapper implements BiFunction<Row, String, Place> {

    private final ColumnConverter converter;

    public PlaceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Place} stored in the database.
     */
    @Override
    public Place apply(Row row, String prefix) {
        Place entity = new Place();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDisplayName(converter.fromRow(row, prefix + "_display_name", String.class));
        entity.setLat(converter.fromRow(row, prefix + "_lat", Double.class));
        entity.setLon(converter.fromRow(row, prefix + "_lon", Double.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setOwnerId(converter.fromRow(row, prefix + "_owner_id", Long.class));
        entity.setCategoryId(converter.fromRow(row, prefix + "_category_id", Long.class));
        entity.setItinaryId(converter.fromRow(row, prefix + "_itinary_id", Long.class));
        return entity;
    }
}
