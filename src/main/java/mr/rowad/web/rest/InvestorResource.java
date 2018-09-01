package mr.rowad.web.rest;

import com.codahale.metrics.annotation.Timed;
import mr.rowad.domain.Investor;
import mr.rowad.service.InvestorService;
import mr.rowad.web.rest.errors.BadRequestAlertException;
import mr.rowad.web.rest.util.HeaderUtil;
import mr.rowad.web.rest.util.PaginationUtil;
import mr.rowad.service.dto.InvestorCriteria;
import mr.rowad.service.InvestorQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Investor.
 */
@RestController
@RequestMapping("/api")
public class InvestorResource {

    private final Logger log = LoggerFactory.getLogger(InvestorResource.class);

    private static final String ENTITY_NAME = "investor";

    private final InvestorService investorService;

    private final InvestorQueryService investorQueryService;

    public InvestorResource(InvestorService investorService, InvestorQueryService investorQueryService) {
        this.investorService = investorService;
        this.investorQueryService = investorQueryService;
    }

    /**
     * POST  /investors : Create a new investor.
     *
     * @param investor the investor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new investor, or with status 400 (Bad Request) if the investor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/investors")
    @Timed
    public ResponseEntity<Investor> createInvestor(@RequestBody Investor investor) throws URISyntaxException {
        log.debug("REST request to save Investor : {}", investor);
        if (investor.getId() != null) {
            throw new BadRequestAlertException("A new investor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Investor result = investorService.save(investor);
        return ResponseEntity.created(new URI("/api/investors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /investors : Updates an existing investor.
     *
     * @param investor the investor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated investor,
     * or with status 400 (Bad Request) if the investor is not valid,
     * or with status 500 (Internal Server Error) if the investor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/investors")
    @Timed
    public ResponseEntity<Investor> updateInvestor(@RequestBody Investor investor) throws URISyntaxException {
        log.debug("REST request to update Investor : {}", investor);
        if (investor.getId() == null) {
            return createInvestor(investor);
        }
        Investor result = investorService.save(investor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, investor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /investors : get all the investors.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of investors in body
     */
    @GetMapping("/investors")
    @Timed
    public ResponseEntity<List<Investor>> getAllInvestors(InvestorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Investors by criteria: {}", criteria);
        Page<Investor> page = investorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/investors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /investors/:id : get the "id" investor.
     *
     * @param id the id of the investor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the investor, or with status 404 (Not Found)
     */
    @GetMapping("/investors/{id}")
    @Timed
    public ResponseEntity<Investor> getInvestor(@PathVariable Long id) {
        log.debug("REST request to get Investor : {}", id);
        Investor investor = investorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(investor));
    }

    /**
     * DELETE  /investors/:id : delete the "id" investor.
     *
     * @param id the id of the investor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/investors/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvestor(@PathVariable Long id) {
        log.debug("REST request to delete Investor : {}", id);
        investorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
