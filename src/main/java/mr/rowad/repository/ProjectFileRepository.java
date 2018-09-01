package mr.rowad.repository;

import mr.rowad.domain.ProjectFile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProjectFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long>, JpaSpecificationExecutor<ProjectFile> {

}
