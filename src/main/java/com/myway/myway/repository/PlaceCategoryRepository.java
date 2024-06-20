package com.myway.myway.repository;

import com.myway.myway.domain.PlaceCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PlaceCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceCategoryRepository extends ReactiveCrudRepository<PlaceCategory, Long>, PlaceCategoryRepositoryInternal {
    @Override
    <S extends PlaceCategory> Mono<S> save(S entity);

    @Override
    Flux<PlaceCategory> findAll();

    @Override
    Mono<PlaceCategory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlaceCategoryRepositoryInternal {
    <S extends PlaceCategory> Mono<S> save(S entity);

    Flux<PlaceCategory> findAllBy(Pageable pageable);

    Flux<PlaceCategory> findAll();

    Mono<PlaceCategory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PlaceCategory> findAllBy(Pageable pageable, Criteria criteria);
}
