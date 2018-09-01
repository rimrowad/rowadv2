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

import mr.rowad.domain.Project;
import mr.rowad.domain.*; // for static metamodels
import mr.rowad.repository.ProjectRepository;
import mr.rowad.service.dto.ProjectCriteria;


/**
 * Service for executing complex queries for Project entities in the database.
 * The main input is a {@link ProjectCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Project} or a {@link Page} of {@link Project} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProjectQueryService extends QueryService<Project> {

    private final Logger log = LoggerFactory.getLogger(ProjectQueryService.class);


    private final ProjectRepository projectRepository;

    public ProjectQueryService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * Return a {@link List} of {@link Project} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Project> findByCriteria(ProjectCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Project> specification = createSpecification(criteria);
        return projectRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Project} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Project> findByCriteria(ProjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Project> specification = createSpecification(criteria);
        return projectRepository.findAll(specification, page);
    }

    /**
     * Function to convert ProjectCriteria to a {@link Specifications}
     */
    private Specifications<Project> createSpecification(ProjectCriteria criteria) {
        Specifications<Project> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Project_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Project_.title));
            }
            if (criteria.getShortDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortDescription(), Project_.shortDescription));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), Project_.details));
            }
            if (criteria.getRendement() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRendement(), Project_.rendement));
            }
            if (criteria.getBudget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBudget(), Project_.budget));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Project_.startDate));
            }
            if (criteria.getEstimatedEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimatedEndDate(), Project_.estimatedEndDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Project_.status));
            }
            if (criteria.getCible() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCible(), Project_.cible));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Project_.type));
            }
            if (criteria.getTeamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTeamId(), Project_.team, Team_.id));
            }
        }
        return specification;
    }

}
