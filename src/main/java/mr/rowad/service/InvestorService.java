package mr.rowad.service;

import mr.rowad.domain.Investor;
import mr.rowad.repository.InvestorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Investor.
 */
@Service
@Transactional
public class InvestorService {

    private final Logger log = LoggerFactory.getLogger(InvestorService.class);

    private final InvestorRepository investorRepository;

    public InvestorService(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    /**
     * Save a investor.
     *
     * @param investor the entity to save
     * @return the persisted entity
     */
    public Investor save(Investor investor) {
        log.debug("Request to save Investor : {}", investor);
        return investorRepository.save(investor);
    }

    /**
     * Get all the investors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Investor> findAll(Pageable pageable) {
        log.debug("Request to get all Investors");
        return investorRepository.findAll(pageable);
    }

    /**
     * Get one investor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Investor findOne(Long id) {
        log.debug("Request to get Investor : {}", id);
        return investorRepository.findOne(id);
    }

    /**
     * Delete the investor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Investor : {}", id);
        investorRepository.delete(id);
    }
}
