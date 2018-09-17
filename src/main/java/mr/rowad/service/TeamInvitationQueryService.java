package mr.rowad.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;
// for static metamodels
import mr.rowad.domain.TeamInvitation;
import mr.rowad.domain.TeamInvitation_;
import mr.rowad.domain.TeamMember_;
import mr.rowad.domain.Team_;
import mr.rowad.domain.User_;
import mr.rowad.repository.TeamInvitationRepository;
import mr.rowad.service.dto.TeamInvitationCriteria;


/**
 * Service for executing complex queries for TeamInvitation entities in the database.
 * The main input is a {@link TeamInvitationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeamInvitation} or a {@link Page} of {@link TeamInvitation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeamInvitationQueryService extends QueryService<TeamInvitation> {

    private final Logger log = LoggerFactory.getLogger(TeamInvitationQueryService.class);


    private final TeamInvitationRepository teamInvitationRepository;

    public TeamInvitationQueryService(TeamInvitationRepository teamInvitationRepository) {
        this.teamInvitationRepository = teamInvitationRepository;
    }

    /**
     * Return a {@link List} of {@link TeamInvitation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeamInvitation> findByCriteria(TeamInvitationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TeamInvitation> specification = createSpecification(criteria);
        return teamInvitationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TeamInvitation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamInvitation> findByCriteria(TeamInvitationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TeamInvitation> specification = createSpecification(criteria);
        return teamInvitationRepository.findAll(specification, page);
    }

    /**
     * Function to convert TeamInvitationCriteria to a {@link Specifications}
     */
    private Specifications<TeamInvitation> createSpecification(TeamInvitationCriteria criteria) {
        Specifications<TeamInvitation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TeamInvitation_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), TeamInvitation_.status));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), TeamInvitation_.date));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSenderId(), TeamInvitation_.sender, TeamMember_.id));
            }
            if (criteria.getReceiverId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReceiverId(), TeamInvitation_.receiver, TeamMember_.id));
            }
            if (criteria.getTeamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTeamId(), TeamInvitation_.team, Team_.id));
            }
            if(criteria.getSenderLogin() != null) {
                specification = specification.and(senderSpecification(criteria));
            }
            if(criteria.getReceiverLogin() != null) {
                specification = specification.and(receiverSpecification(criteria));
            }
        }
        return specification;
    }

    private Specification<TeamInvitation> senderSpecification(TeamInvitationCriteria criteria) {
        return (root, query, builder) -> builder.equal(root.get(TeamInvitation_.sender).get(TeamMember_.user).get(User_.login), criteria.getSenderLogin());
    }
    private Specification<TeamInvitation> receiverSpecification(TeamInvitationCriteria criteria) {
        return (root, query, builder) -> builder.equal(root.get(TeamInvitation_.receiver).get(TeamMember_.user).get(User_.login), criteria.getReceiverLogin());
    }

}
