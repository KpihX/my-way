package com.kpihx_labs.myway.repository.rowmapper;

import com.kpihx_labs.myway.domain.Itinary;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Itinary}, with proper type conversions.
 */
@Service
public class ItinaryRowMapper implements BiFunction<Row, String, Itinary> {

    private final ColumnConverter converter;

    public ItinaryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Itinary} stored in the database.
     */
    @Override
    public Itinary apply(Row row, String prefix) {
        Itinary entity = new Itinary();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setDistance(converter.fromRow(row, prefix + "_distance", Double.class));
        entity.setDuration(converter.fromRow(row, prefix + "_duration", Integer.class));
        entity.setPolyline(converter.fromRow(row, prefix + "_polyline", String.class));
        return entity;
    }
}
