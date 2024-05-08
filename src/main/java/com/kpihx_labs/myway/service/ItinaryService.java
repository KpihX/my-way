package com.kpihx_labs.myway.service;

import com.kpihx_labs.myway.repository.ItinaryRepository;
import com.kpihx_labs.myway.service.dto.ItinaryDTO;
import com.kpihx_labs.myway.service.mapper.ItinaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.kpihx_labs.myway.domain.Itinary}.
 */
@Service
@Transactional
public class ItinaryService {

    private final Logger log = LoggerFactory.getLogger(ItinaryService.class);

    private final ItinaryRepository itinaryRepository;

    private final ItinaryMapper itinaryMapper;

    public ItinaryService(ItinaryRepository itinaryRepository, ItinaryMapper itinaryMapper) {
        this.itinaryRepository = itinaryRepository;
        this.itinaryMapper = itinaryMapper;
    }

    /**
     * Save a itinary.
     *
     * @param itinaryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItinaryDTO> save(ItinaryDTO itinaryDTO) {
        log.debug("Request to save Itinary : {}", itinaryDTO);
        return itinaryRepository.save(itinaryMapper.toEntity(itinaryDTO)).map(itinaryMapper::toDto);
    }

    /**
     * Update a itinary.
     *
     * @param itinaryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ItinaryDTO> update(ItinaryDTO itinaryDTO) {
        log.debug("Request to update Itinary : {}", itinaryDTO);
        return itinaryRepository.save(itinaryMapper.toEntity(itinaryDTO)).map(itinaryMapper::toDto);
    }

    /**
     * Partially update a itinary.
     *
     * @param itinaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ItinaryDTO> partialUpdate(ItinaryDTO itinaryDTO) {
        log.debug("Request to partially update Itinary : {}", itinaryDTO);

        return itinaryRepository
            .findById(itinaryDTO.getId())
            .map(existingItinary -> {
                itinaryMapper.partialUpdate(existingItinary, itinaryDTO);

                return existingItinary;
            })
            .flatMap(itinaryRepository::save)
            .map(itinaryMapper::toDto);
    }

    /**
     * Get all the itinaries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ItinaryDTO> findAll() {
        log.debug("Request to get all Itinaries");
        return itinaryRepository.findAll().map(itinaryMapper::toDto);
    }

    /**
     * Get all the itinaries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ItinaryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itinaryRepository.findAllWithEagerRelationships(pageable).map(itinaryMapper::toDto);
    }

    /**
     * Returns the number of itinaries available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return itinaryRepository.count();
    }

    /**
     * Get one itinary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ItinaryDTO> findOne(Long id) {
        log.debug("Request to get Itinary : {}", id);
        return itinaryRepository.findOneWithEagerRelationships(id).map(itinaryMapper::toDto);
    }

    /**
     * Delete the itinary by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Itinary : {}", id);
        return itinaryRepository.deleteById(id);
    }
}
