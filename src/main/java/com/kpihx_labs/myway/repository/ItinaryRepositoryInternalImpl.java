package com.kpihx_labs.myway.repository;

import com.kpihx_labs.myway.domain.Itinary;
import com.kpihx_labs.myway.domain.Location;
import com.kpihx_labs.myway.repository.rowmapper.ItinaryRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
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

    private final ItinaryRowMapper itinaryMapper;

    private static final Table entityTable = Table.aliased("itinary", EntityManager.ENTITY_ALIAS);

    private static final EntityManager.LinkTable locationLink = new EntityManager.LinkTable(
        "rel_itinary__location",
        "itinary_id",
        "location_id"
    );

    public ItinaryRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
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
        this.itinaryMapper = itinaryMapper;
    }

    @Override
    public Flux<Itinary> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Itinary> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ItinarySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
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

    @Override
    public Mono<Itinary> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Itinary> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Itinary> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Itinary process(Row row, RowMetadata metadata) {
        Itinary entity = itinaryMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends Itinary> Mono<S> save(S entity) {
        return super.save(entity).flatMap((S e) -> updateRelations(e));
    }

    protected <S extends Itinary> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(locationLink, entity.getId(), entity.getLocations().stream().map(Location::getId))
            .then();
        return result.thenReturn(entity);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId).then(super.deleteById(entityId));
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(locationLink, entityId);
    }
}
