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

import mr.rowad.domain.ProjectEvent;
import mr.rowad.domain.*; // for static metamodels
import mr.rowad.repository.ProjectEventRepository;
import mr.rowad.service.dto.ProjectEventCriteria;


/**
 * Service for executing complex queries for ProjectEvent entities in the database.
 * The main input is a {@link ProjectEventCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProjectEvent} or a {@link Page} of {@link ProjectEvent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectEventQueryService extends QueryService<ProjectEvent> {

    private final Logger log = LoggerFactory.getLogger(ProjectEventQueryService.class);


    private final ProjectEventRepository projectEventRepository;

    public ProjectEventQueryService(ProjectEventRepository projectEventRepository) {
        this.projectEventRepository = projectEventRepository;
    }

    /**
     * Return a {@link List} of {@link ProjectEvent} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProjectEvent> findByCriteria(ProjectEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProjectEvent> specification = createSpecification(criteria);
        return projectEventRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProjectEvent} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProjectEvent> findByCriteria(ProjectEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProjectEvent> specification = createSpecification(criteria);
        return projectEventRepository.findAll(specification, page);
    }

    /**
     * Function to convert ProjectEventCriteria to a {@link Specifications}
     */
    private Specifications<ProjectEvent> createSpecification(ProjectEventCriteria criteria) {
        Specifications<ProjectEvent> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProjectEvent_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ProjectEvent_.type));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), ProjectEvent_.creationDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ProjectEvent_.description));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProjectId(), ProjectEvent_.project, Project_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), ProjectEvent_.user, User_.id));
            }
        }
        return specification;
    }

}
