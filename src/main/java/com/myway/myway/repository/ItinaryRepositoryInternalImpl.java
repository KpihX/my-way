package com.myway.myway.repository;

import com.myway.myway.domain.Itinary;
import com.myway.myway.repository.rowmapper.ItinaryRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Itinary entity.
 */
@SuppressWarnings("unused")
class ItinaryRepositoryInternalImpl extends SimpleR2dbcRepository<Itinary, Long> implements ItinaryRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final UserRowMapper userMapper;
    private final ItinaryRowMapper itinaryMapper;

    private static final Table entityTable = Table.aliased("itinary", EntityManager.ENTITY_ALIAS);
    private static final Table ownerTable = Table.aliased("jhi_user", "owner");

    public ItinaryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        UserRowMapper userMapper,
        ItinaryRowMapper itinaryMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Itinary.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.userMapper = userMapper;
        this.itinaryMapper = itinaryMapper;
    }

    @Override
    public Flux<Itinary> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Itinary> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItinarySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(UserSqlHelper.getColumns(ownerTable, "owner"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(ownerTable)
            .on(Column.create("owner_id", entityTable))
            .equals(Column.create("id", ownerTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Itinary.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Itinary> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Itinary> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Itinary process(Row row, RowMetadata metadata) {
        Itinary entity = itinaryMapper.apply(row, "e");
        entity.setOwner(userMapper.apply(row, "owner"));
        return entity;
    }

    @Override
    public <S extends Itinary> Mono<S> save(S entity) {
        return super.save(entity);
    }
}