package com.kpihx_labs.myway.service;

import com.kpihx_labs.myway.repository.FrequentItinaryRepository;
import com.kpihx_labs.myway.service.dto.FrequentItinaryDTO;
import com.kpihx_labs.myway.service.mapper.FrequentItinaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.kpihx_labs.myway.domain.FrequentItinary}.
 */
@Service
@Transactional
public class FrequentItinaryService {

    private final Logger log = LoggerFactory.getLogger(FrequentItinaryService.class);

    private final FrequentItinaryRepository frequentItinaryRepository;

    private final FrequentItinaryMapper frequentItinaryMapper;

    public FrequentItinaryService(FrequentItinaryRepository frequentItinaryRepository, FrequentItinaryMapper frequentItinaryMapper) {
        this.frequentItinaryRepository = frequentItinaryRepository;
        this.frequentItinaryMapper = frequentItinaryMapper;
    }

    /**
     * Save a frequentItinary.
     *
     * @param frequentItinaryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FrequentItinaryDTO> save(FrequentItinaryDTO frequentItinaryDTO) {
        log.debug("Request to save FrequentItinary : {}", frequentItinaryDTO);
        return frequentItinaryRepository.save(frequentItinaryMapper.toEntity(frequentItinaryDTO)).map(frequentItinaryMapper::toDto);
    }

    /**
     * Update a frequentItinary.
     *
     * @param frequentItinaryDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<FrequentItinaryDTO> update(FrequentItinaryDTO frequentItinaryDTO) {
        log.debug("Request to update FrequentItinary : {}", frequentItinaryDTO);
        return frequentItinaryRepository.save(frequentItinaryMapper.toEntity(frequentItinaryDTO)).map(frequentItinaryMapper::toDto);
    }

    /**
     * Partially update a frequentItinary.
     *
     * @param frequentItinaryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FrequentItinaryDTO> partialUpdate(FrequentItinaryDTO frequentItinaryDTO) {
        log.debug("Request to partially update FrequentItinary : {}", frequentItinaryDTO);

        return frequentItinaryRepository
            .findById(frequentItinaryDTO.getId())
            .map(existingFrequentItinary -> {
                frequentItinaryMapper.partialUpdate(existingFrequentItinary, frequentItinaryDTO);

                return existingFrequentItinary;
            })
            .flatMap(frequentItinaryRepository::save)
            .map(frequentItinaryMapper::toDto);
    }

    /**
     * Get all the frequentItinaries.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FrequentItinaryDTO> findAll() {
        log.debug("Request to get all FrequentItinaries");
        return frequentItinaryRepository.findAll().map(frequentItinaryMapper::toDto);
    }

    /**
     * Returns the number of frequentItinaries available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return frequentItinaryRepository.count();
    }

    /**
     * Get one frequentItinary by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FrequentItinaryDTO> findOne(Long id) {
        log.debug("Request to get FrequentItinary : {}", id);
        return frequentItinaryRepository.findById(id).map(frequentItinaryMapper::toDto);
    }

    /**
     * Delete the frequentItinary by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FrequentItinary : {}", id);
        return frequentItinaryRepository.deleteById(id);
    }
}
