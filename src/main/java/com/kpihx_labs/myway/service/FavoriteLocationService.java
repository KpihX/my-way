package com.kpihx_labs.myway.service;

import com.kpihx_labs.myway.repository.FavoriteLocationRepository;
import com.kpihx_labs.myway.service.dto.FavoriteLocationDTO;
import com.kpihx_labs.myway.service.mapper.FavoriteLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.kpihx_labs.myway.domain.FavoriteLocation}.
 */
@Service
@Transactional
public class FavoriteLocationService {

    private final Logger log = LoggerFactory.getLogger(FavoriteLocationService.class);

    private final FavoriteLocationRepository favoriteLocationRepository;

    private final FavoriteLocationMapper favoriteLocationMapper;

    public FavoriteLocationService(FavoriteLocationRepository favoriteLocationRepository, FavoriteLocationMapper favoriteLocationMapper) {
        this.favoriteLocationRepository = favoriteLocationRepository;
        this.favoriteLocationMapper = favoriteLocationMapper;
    }

    /**
     * Save a favoriteLocation.
     *
     * @param favoriteLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FavoriteLocationDTO> save(FavoriteLocationDTO favoriteLocationDTO) {
        log.debug("Request to save FavoriteLocation : {}", favoriteLocationDTO);
        return favoriteLocationRepository.save(favoriteLocationMapper.toEntity(favoriteLocationDTO)).map(favoriteLocationMapper::toDto);
    }

    /**
     * Update a favoriteLocation.
     *
     * @param favoriteLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FavoriteLocationDTO> update(FavoriteLocationDTO favoriteLocationDTO) {
        log.debug("Request to update FavoriteLocation : {}", favoriteLocationDTO);
        return favoriteLocationRepository.save(favoriteLocationMapper.toEntity(favoriteLocationDTO)).map(favoriteLocationMapper::toDto);
    }

    /**
     * Partially update a favoriteLocation.
     *
     * @param favoriteLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FavoriteLocationDTO> partialUpdate(FavoriteLocationDTO favoriteLocationDTO) {
        log.debug("Request to partially update FavoriteLocation : {}", favoriteLocationDTO);

        return favoriteLocationRepository
            .findById(favoriteLocationDTO.getId())
            .map(existingFavoriteLocation -> {
                favoriteLocationMapper.partialUpdate(existingFavoriteLocation, favoriteLocationDTO);

                return existingFavoriteLocation;
            })
            .flatMap(favoriteLocationRepository::save)
            .map(favoriteLocationMapper::toDto);
    }

    /**
     * Get all the favoriteLocations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FavoriteLocationDTO> findAll() {
        log.debug("Request to get all FavoriteLocations");
        return favoriteLocationRepository.findAll().map(favoriteLocationMapper::toDto);
    }

    /**
     * Returns the number of favoriteLocations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return favoriteLocationRepository.count();
    }

    /**
     * Get one favoriteLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FavoriteLocationDTO> findOne(Long id) {
        log.debug("Request to get FavoriteLocation : {}", id);
        return favoriteLocationRepository.findById(id).map(favoriteLocationMapper::toDto);
    }

    /**
     * Delete the favoriteLocation by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FavoriteLocation : {}", id);
        return favoriteLocationRepository.deleteById(id);
    }
}
