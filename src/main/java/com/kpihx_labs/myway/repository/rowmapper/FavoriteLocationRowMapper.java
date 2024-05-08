package com.kpihx_labs.myway.repository.rowmapper;

import com.kpihx_labs.myway.domain.FavoriteLocation;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FavoriteLocation}, with proper type conversions.
 */
@Service
public class FavoriteLocationRowMapper implements BiFunction<Row, String, FavoriteLocation> {

    private final ColumnConverter converter;

    public FavoriteLocationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FavoriteLocation} stored in the database.
     */
    @Override
    public FavoriteLocation apply(Row row, String prefix) {
        FavoriteLocation entity = new FavoriteLocation();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
