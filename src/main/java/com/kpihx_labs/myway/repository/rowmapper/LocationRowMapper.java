package com.kpihx_labs.myway.repository.rowmapper;

import com.kpihx_labs.myway.domain.Location;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Location}, with proper type conversions.
 */
@Service
public class LocationRowMapper implements BiFunction<Row, String, Location> {

    private final ColumnConverter converter;

    public LocationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Location} stored in the database.
     */
    @Override
    public Location apply(Row row, String prefix) {
        Location entity = new Location();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLatitude(converter.fromRow(row, prefix + "_latitude", Double.class));
        entity.setLongitude(converter.fromRow(row, prefix + "_longitude", Double.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setAddress(converter.fromRow(row, prefix + "_address", String.class));
        entity.setCategoryId(converter.fromRow(row, prefix + "_category_id", Long.class));
        return entity;
    }
}
