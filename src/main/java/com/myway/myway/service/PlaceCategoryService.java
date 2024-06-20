package com.myway.myway.service;

import com.myway.myway.repository.PlaceCategoryRepository;
import com.myway.myway.service.dto.PlaceCategoryDTO;
import com.myway.myway.service.mapper.PlaceCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.myway.myway.domain.PlaceCategory}.
 */
@Service
@Transactional
public class PlaceCategoryService {

    private final Logger log = LoggerFactory.getLogger(PlaceCategoryService.class);

    private final PlaceCategoryRepository placeCategoryRepository;

    private final PlaceCategoryMapper placeCategoryMapper;

    public PlaceCategoryService(PlaceCategoryRepository placeCategoryRepository, PlaceCategoryMapper placeCategoryMapper) {
        this.placeCategoryRepository = placeCategoryRepository;
        this.placeCategoryMapper = placeCategoryMapper;
    }

    /**
     * Save a placeCategory.
     *
     * @param placeCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlaceCategoryDTO> save(PlaceCategoryDTO placeCategoryDTO) {
        log.debug("Request to save PlaceCategory : {}", placeCategoryDTO);
        return placeCategoryRepository.save(placeCategoryMapper.toEntity(placeCategoryDTO)).map(placeCategoryMapper::toDto);
    }

    /**
     * Update a placeCategory.
     *
     * @param placeCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlaceCategoryDTO> update(PlaceCategoryDTO placeCategoryDTO) {
        log.debug("Request to update PlaceCategory : {}", placeCategoryDTO);
        return placeCategoryRepository.save(placeCategoryMapper.toEntity(placeCategoryDTO)).map(placeCategoryMapper::toDto);
    }

    /**
     * Partially update a placeCategory.
     *
     * @param placeCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PlaceCategoryDTO> partialUpdate(PlaceCategoryDTO placeCategoryDTO) {
        log.debug("Request to partially update PlaceCategory : {}", placeCategoryDTO);

        return placeCategoryRepository
            .findById(placeCategoryDTO.getId())
            .map(existingPlaceCategory -> {
                placeCategoryMapper.partialUpdate(existingPlaceCategory, placeCategoryDTO);

                return existingPlaceCategory;
            })
            .flatMap(placeCategoryRepository::save)
            .map(placeCategoryMapper::toDto);
    }

    /**
     * Get all the placeCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PlaceCategoryDTO> findAll() {
        log.debug("Request to get all PlaceCategories");
        return placeCategoryRepository.findAll().map(placeCategoryMapper::toDto);
    }

    /**
     * Returns the number of placeCategories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return placeCategoryRepository.count();
    }

    /**
     * Get one placeCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PlaceCategoryDTO> findOne(Long id) {
        log.debug("Request to get PlaceCategory : {}", id);
        return placeCategoryRepository.findById(id).map(placeCategoryMapper::toDto);
    }

    /**
     * Delete the placeCategory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PlaceCategory : {}", id);
        return placeCategoryRepository.deleteById(id);
    }
}
