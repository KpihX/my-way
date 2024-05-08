package com.kpihx_labs.myway.service;

import com.kpihx_labs.myway.repository.LocationCategoryRepository;
import com.kpihx_labs.myway.service.dto.LocationCategoryDTO;
import com.kpihx_labs.myway.service.mapper.LocationCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.kpihx_labs.myway.domain.LocationCategory}.
 */
@Service
@Transactional
public class LocationCategoryService {

    private final Logger log = LoggerFactory.getLogger(LocationCategoryService.class);

    private final LocationCategoryRepository locationCategoryRepository;

    private final LocationCategoryMapper locationCategoryMapper;

    public LocationCategoryService(LocationCategoryRepository locationCategoryRepository, LocationCategoryMapper locationCategoryMapper) {
        this.locationCategoryRepository = locationCategoryRepository;
        this.locationCategoryMapper = locationCategoryMapper;
    }

    /**
     * Save a locationCategory.
     *
     * @param locationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LocationCategoryDTO> save(LocationCategoryDTO locationCategoryDTO) {
        log.debug("Request to save LocationCategory : {}", locationCategoryDTO);
        return locationCategoryRepository.save(locationCategoryMapper.toEntity(locationCategoryDTO)).map(locationCategoryMapper::toDto);
    }

    /**
     * Update a locationCategory.
     *
     * @param locationCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LocationCategoryDTO> update(LocationCategoryDTO locationCategoryDTO) {
        log.debug("Request to update LocationCategory : {}", locationCategoryDTO);
        return locationCategoryRepository.save(locationCategoryMapper.toEntity(locationCategoryDTO)).map(locationCategoryMapper::toDto);
    }

    /**
     * Partially update a locationCategory.
     *
     * @param locationCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LocationCategoryDTO> partialUpdate(LocationCategoryDTO locationCategoryDTO) {
        log.debug("Request to partially update LocationCategory : {}", locationCategoryDTO);

        return locationCategoryRepository
            .findById(locationCategoryDTO.getId())
            .map(existingLocationCategory -> {
                locationCategoryMapper.partialUpdate(existingLocationCategory, locationCategoryDTO);

                return existingLocationCategory;
            })
            .flatMap(locationCategoryRepository::save)
            .map(locationCategoryMapper::toDto);
    }

    /**
     * Get all the locationCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LocationCategoryDTO> findAll() {
        log.debug("Request to get all LocationCategories");
        return locationCategoryRepository.findAll().map(locationCategoryMapper::toDto);
    }

    /**
     * Returns the number of locationCategories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return locationCategoryRepository.count();
    }

    /**
     * Get one locationCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LocationCategoryDTO> findOne(Long id) {
        log.debug("Request to get LocationCategory : {}", id);
        return locationCategoryRepository.findById(id).map(locationCategoryMapper::toDto);
    }

    /**
     * Delete the locationCategory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete LocationCategory : {}", id);
        return locationCategoryRepository.deleteById(id);
    }
}
