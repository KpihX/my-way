package com.myway.myway.service;

import com.myway.myway.repository.PlaceRepository;
import com.myway.myway.service.dto.PlaceDTO;
import com.myway.myway.service.mapper.PlaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.myway.myway.domain.Place}.
 */
@Service
@Transactional
public class PlaceService {

    private final Logger log = LoggerFactory.getLogger(PlaceService.class);

    private final PlaceRepository placeRepository;

    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    /**
     * Save a place.
     *
     * @param placeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlaceDTO> save(PlaceDTO placeDTO) {
        log.debug("Request to save Place : {}", placeDTO);
        return placeRepository.save(placeMapper.toEntity(placeDTO)).map(placeMapper::toDto);
    }

    /**
     * Update a place.
     *
     * @param placeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlaceDTO> update(PlaceDTO placeDTO) {
        log.debug("Request to update Place : {}", placeDTO);
        return placeRepository.save(placeMapper.toEntity(placeDTO)).map(placeMapper::toDto);
    }

    /**
     * Partially update a place.
     *
     * @param placeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PlaceDTO> partialUpdate(PlaceDTO placeDTO) {
        log.debug("Request to partially update Place : {}", placeDTO);

        return placeRepository
            .findById(placeDTO.getId())
            .map(existingPlace -> {
                placeMapper.partialUpdate(existingPlace, placeDTO);

                return existingPlace;
            })
            .flatMap(placeRepository::save)
            .map(placeMapper::toDto);
    }

    /**
     * Get all the places.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PlaceDTO> findAll() {
        log.debug("Request to get all Places");
        return placeRepository.findAll().map(placeMapper::toDto);
    }

    /**
     * Returns the number of places available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return placeRepository.count();
    }

    /**
     * Get one place by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PlaceDTO> findOne(Long id) {
        log.debug("Request to get Place : {}", id);
        return placeRepository.findById(id).map(placeMapper::toDto);
    }

    /**
     * Delete the place by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Place : {}", id);
        return placeRepository.deleteById(id);
    }
}
