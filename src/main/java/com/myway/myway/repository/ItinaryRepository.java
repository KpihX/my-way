package com.myway.myway.repository;

import com.myway.myway.domain.Itinary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Itinary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItinaryRepository extends ReactiveCrudRepository<Itinary, Long>, ItinaryRepositoryInternal {
    @Query("SELECT * FROM itinary entity WHERE entity.owner_id = :id")
    Flux<Itinary> findByOwner(Long id);

    @Query("SELECT * FROM itinary entity WHERE entity.owner_id IS NULL")
    Flux<Itinary> findAllWhereOwnerIsNull();

    @Override
    <S extends Itinary> Mono<S> save(S entity);

    @Override
    Flux<Itinary> findAll();

    @Override
    Mono<Itinary> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ItinaryRepositoryInternal {
    <S extends Itinary> Mono<S> save(S entity);

    Flux<Itinary> findAllBy(Pageable pageable);

    Flux<Itinary> findAll();

    Mono<Itinary> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Itinary> findAllBy(Pageable pageable, Criteria criteria);
}
