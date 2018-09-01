package mr.rowad.repository;

import mr.rowad.domain.Investor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Investor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long>, JpaSpecificationExecutor<Investor> {

    @Query("select investor from Investor investor where investor.user.login = ?#{principal.username}")
    List<Investor> findByUserIsCurrentUser();

}
