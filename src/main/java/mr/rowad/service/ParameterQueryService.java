package mr.rowad.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mr.rowad.domain.Parameter;
import mr.rowad.domain.*; // for static metamodels
import mr.rowad.repository.ParameterRepository;
import mr.rowad.service.dto.ParameterCriteria;


/**
 * Service for executing complex queries for Parameter entities in the database.
 * The main input is a {@link ParameterCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Parameter} or a {@link Page} of {@link Parameter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParameterQueryService extends QueryService<Parameter> {

    private final Logger log = LoggerFactory.getLogger(ParameterQueryService.class);


    private final ParameterRepository parameterRepository;

    public ParameterQueryService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    /**
     * Return a {@link List} of {@link Parameter} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Parameter> findByCriteria(ParameterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Parameter> specification = createSpecification(criteria);
        return parameterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Parameter} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Parameter> findByCriteria(ParameterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Parameter> specification = createSpecification(criteria);
        return parameterRepository.findAll(specification, page);
    }

    /**
     * Function to convert ParameterCriteria to a {@link Specifications}
     */
    private Specifications<Parameter> createSpecification(ParameterCriteria criteria) {
        Specifications<Parameter> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Parameter_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Parameter_.type));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), Parameter_.value));
            }
        }
        return specification;
    }

}
