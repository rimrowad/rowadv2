package mr.rowad.repository;

import mr.rowad.domain.TeamMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the TeamMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, JpaSpecificationExecutor<TeamMember> {

    @Query("select team_member from TeamMember team_member where team_member.user.login = ?#{principal.username}")
    List<TeamMember> findByUserIsCurrentUser();

}
