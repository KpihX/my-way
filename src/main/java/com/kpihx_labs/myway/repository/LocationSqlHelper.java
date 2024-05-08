package com.kpihx_labs.myway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LocationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("latitude", table, columnPrefix + "_latitude"));
        columns.add(Column.aliased("longitude", table, columnPrefix + "_longitude"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("address", table, columnPrefix + "_address"));

        columns.add(Column.aliased("category_id", table, columnPrefix + "_category_id"));
        return columns;
    }
}
