package com.myway.myway.repository.rowmapper;

import com.myway.myway.domain.Itinary;
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
        entity.setPrefName(converter.fromRow(row, prefix + "_pref_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setTotalDistance(converter.fromRow(row, prefix + "_total_distance", Double.class));
        entity.setTotalTime(converter.fromRow(row, prefix + "_total_time", Double.class));
        entity.setImage(converter.fromRow(row, prefix + "_image", String.class));
        entity.setOwnerId(converter.fromRow(row, prefix + "_owner_id", Long.class));
        return entity;
    }
}
