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

import mr.rowad.domain.TeamMember;
import mr.rowad.domain.*; // for static metamodels
import mr.rowad.repository.TeamMemberRepository;
import mr.rowad.service.dto.TeamMemberCriteria;


/**
 * Service for executing complex queries for TeamMember entities in the database.
 * The main input is a {@link TeamMemberCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamMember} or a {@link Page} of {@link TeamMember} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamMemberQueryService extends QueryService<TeamMember> {

    private final Logger log = LoggerFactory.getLogger(TeamMemberQueryService.class);


    private final TeamMemberRepository teamMemberRepository;

    public TeamMemberQueryService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    /**
     * Return a {@link List} of {@link TeamMember} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamMember> findByCriteria(TeamMemberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TeamMember> specification = createSpecification(criteria);
        return teamMemberRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TeamMember} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamMember> findByCriteria(TeamMemberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TeamMember> specification = createSpecification(criteria);
        return teamMemberRepository.findAll(specification, page);
    }

    /**
     * Function to convert TeamMemberCriteria to a {@link Specifications}
     */
    private Specifications<TeamMember> createSpecification(TeamMemberCriteria criteria) {
        Specifications<TeamMember> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TeamMember_.id));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), TeamMember_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), TeamMember_.phone));
            }
            if (criteria.getDateOfBirth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfBirth(), TeamMember_.dateOfBirth));
            }
            if (criteria.getDiplome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiplome(), TeamMember_.diplome));
            }
            if (criteria.getResume() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResume(), TeamMember_.resume));
            }
            if (criteria.getTeamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTeamId(), TeamMember_.team, Team_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), TeamMember_.user, User_.id));
            }
        }
        return specification;
    }

}
