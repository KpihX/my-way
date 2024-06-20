package com.myway.myway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PlaceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("display_name", table, columnPrefix + "_display_name"));
        columns.add(Column.aliased("lat", table, columnPrefix + "_lat"));
        columns.add(Column.aliased("lon", table, columnPrefix + "_lon"));
        columns.add(Column.aliased("image", table, columnPrefix + "_image"));

        columns.add(Column.aliased("owner_id", table, columnPrefix + "_owner_id"));
        columns.add(Column.aliased("category_id", table, columnPrefix + "_category_id"));
        columns.add(Column.aliased("itinary_id", table, columnPrefix + "_itinary_id"));
        return columns;
    }
}
