package mr.rowad.repository;

import mr.rowad.domain.ProjectEvent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ProjectEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectEventRepository extends JpaRepository<ProjectEvent, Long>, JpaSpecificationExecutor<ProjectEvent> {

    @Query("select project_event from ProjectEvent project_event where project_event.user.login = ?#{principal.username}")
    List<ProjectEvent> findByUserIsCurrentUser();

}
