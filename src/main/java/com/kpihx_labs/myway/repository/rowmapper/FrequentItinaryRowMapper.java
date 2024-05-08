package com.kpihx_labs.myway.repository.rowmapper;

import com.kpihx_labs.myway.domain.FrequentItinary;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FrequentItinary}, with proper type conversions.
 */
@Service
public class FrequentItinaryRowMapper implements BiFunction<Row, String, FrequentItinary> {

    private final ColumnConverter converter;

    public FrequentItinaryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FrequentItinary} stored in the database.
     */
    @Override
    public FrequentItinary apply(Row row, String prefix) {
        FrequentItinary entity = new FrequentItinary();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
