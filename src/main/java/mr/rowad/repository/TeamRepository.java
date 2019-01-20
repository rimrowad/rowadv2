package mr.rowad.repository;

import mr.rowad.domain.Team;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    @Query("select team from Team team where team.owner.login = ?#{principal.username}")
    List<Team> findByOwnerIsCurrentUser();

}
