package com.myway.myway.repository;

import com.myway.myway.domain.Place;
import com.myway.myway.repository.rowmapper.ItinaryRowMapper;
import com.myway.myway.repository.rowmapper.PlaceCategoryRowMapper;
import com.myway.myway.repository.rowmapper.PlaceRowMapper;
import com.myway.myway.repository.rowmapper.UserRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Place entity.
 */
@SuppressWarnings("unused")
class PlaceRepositoryInternalImpl extends SimpleR2dbcRepository<Place, Long> implements PlaceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final PlaceCategoryRowMapper placecategoryMapper;
    private final ItinaryRowMapper itinaryMapper;
    private final PlaceRowMapper placeMapper;

    private static final Table entityTable = Table.aliased("place", EntityManager.ENTITY_ALIAS);
    private static final Table ownerTable = Table.aliased("jhi_user", "owner");
    private static final Table categoryTable = Table.aliased("place_category", "category");
    private static final Table itinaryTable = Table.aliased("itinary", "itinary");

    public PlaceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        PlaceCategoryRowMapper placecategoryMapper,
        ItinaryRowMapper itinaryMapper,
        PlaceRowMapper placeMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Place.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.placecategoryMapper = placecategoryMapper;
        this.itinaryMapper = itinaryMapper;
        this.placeMapper = placeMapper;
    }

    @Override
    public Flux<Place> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Place> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PlaceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(ownerTable, "owner"));
        columns.addAll(PlaceCategorySqlHelper.getColumns(categoryTable, "category"));
        columns.addAll(ItinarySqlHelper.getColumns(itinaryTable, "itinary"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ownerTable)
            .on(Column.create("owner_id", entityTable))
            .equals(Column.create("id", ownerTable))
            .leftOuterJoin(categoryTable)
            .on(Column.create("category_id", entityTable))
            .equals(Column.create("id", categoryTable))
            .leftOuterJoin(itinaryTable)
            .on(Column.create("itinary_id", entityTable))
            .equals(Column.create("id", itinaryTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Place.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Place> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Place> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Place process(Row row, RowMetadata metadata) {
        Place entity = placeMapper.apply(row, "e");
        entity.setOwner(userMapper.apply(row, "owner"));
        entity.setCategory(placecategoryMapper.apply(row, "category"));
        entity.setItinary(itinaryMapper.apply(row, "itinary"));
        return entity;
    }

    @Override
    public <S extends Place> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
