package mr.rowad.repository;

import mr.rowad.domain.TeamInvitation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TeamInvitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long>, JpaSpecificationExecutor<TeamInvitation> {

}
