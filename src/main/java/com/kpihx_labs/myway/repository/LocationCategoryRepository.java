package com.kpihx_labs.myway.repository;

import com.kpihx_labs.myway.domain.LocationCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LocationCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationCategoryRepository extends ReactiveCrudRepository<LocationCategory, Long>, LocationCategoryRepositoryInternal {
    @Override
    <S extends LocationCategory> Mono<S> save(S entity);

    @Override
    Flux<LocationCategory> findAll();

    @Override
    Mono<LocationCategory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LocationCategoryRepositoryInternal {
    <S extends LocationCategory> Mono<S> save(S entity);

    Flux<LocationCategory> findAllBy(Pageable pageable);

    Flux<LocationCategory> findAll();

    Mono<LocationCategory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LocationCategory> findAllBy(Pageable pageable, Criteria criteria);
}
