package mr.rowad.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mr.rowad.domain.Investor;
import mr.rowad.domain.*; // for static metamodels
import mr.rowad.repository.InvestorRepository;
import mr.rowad.service.dto.InvestorCriteria;


/**
 * Service for executing complex queries for Investor entities in the database.
 * The main input is a {@link InvestorCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Investor} or a {@link Page} of {@link Investor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvestorQueryService extends QueryService<Investor> {

    private final Logger log = LoggerFactory.getLogger(InvestorQueryService.class);


    private final InvestorRepository investorRepository;

    public InvestorQueryService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    /**
     * Return a {@link List} of {@link Investor} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Investor> findByCriteria(InvestorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Investor> specification = createSpecification(criteria);
        return investorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Investor} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Investor> findByCriteria(InvestorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Investor> specification = createSpecification(criteria);
        return investorRepository.findAll(specification, page);
    }

    /**
     * Function to convert InvestorCriteria to a {@link Specifications}
     */
    private Specifications<Investor> createSpecification(InvestorCriteria criteria) {
        Specifications<Investor> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Investor_.id));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Investor_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Investor_.phone));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), Investor_.dateOfBirth));
            }
            if (criteria.getShortDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortDescription(), Investor_.shortDescription));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Investor_.description));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Investor_.user, User_.id));
            }
        }
        return specification;
    }

}
