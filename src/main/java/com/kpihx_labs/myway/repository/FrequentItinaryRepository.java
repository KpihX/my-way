package com.kpihx_labs.myway.repository;

import com.kpihx_labs.myway.domain.FrequentItinary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FrequentItinary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FrequentItinaryRepository extends ReactiveCrudRepository<FrequentItinary, Long>, FrequentItinaryRepositoryInternal {
    @Query("SELECT * FROM frequent_itinary entity WHERE entity.user_id = :id")
    Flux<FrequentItinary> findByUser(Long id);

    @Query("SELECT * FROM frequent_itinary entity WHERE entity.user_id IS NULL")
    Flux<FrequentItinary> findAllWhereUserIsNull();

    @Override
    <S extends FrequentItinary> Mono<S> save(S entity);

    @Override
    Flux<FrequentItinary> findAll();

    @Override
    Mono<FrequentItinary> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FrequentItinaryRepositoryInternal {
    <S extends FrequentItinary> Mono<S> save(S entity);

    Flux<FrequentItinary> findAllBy(Pageable pageable);

    Flux<FrequentItinary> findAll();

    Mono<FrequentItinary> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FrequentItinary> findAllBy(Pageable pageable, Criteria criteria);
}
