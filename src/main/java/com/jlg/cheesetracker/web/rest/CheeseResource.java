package com.jlg.cheesetracker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jlg.cheesetracker.domain.Cheese;

import com.jlg.cheesetracker.repository.CheeseRepository;
import com.jlg.cheesetracker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cheese.
 */
@RestController
@RequestMapping("/api")
public class CheeseResource {

    private final Logger log = LoggerFactory.getLogger(CheeseResource.class);
        
    @Inject
    private CheeseRepository cheeseRepository;

    /**
     * POST  /cheeses : Create a new cheese.
     *
     * @param cheese the cheese to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cheese, or with status 400 (Bad Request) if the cheese has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cheeses")
    @Timed
    public ResponseEntity<Cheese> createCheese(@RequestBody Cheese cheese) throws URISyntaxException {
        log.debug("REST request to save Cheese : {}", cheese);
        if (cheese.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cheese", "idexists", "A new cheese cannot already have an ID")).body(null);
        }
        Cheese result = cheeseRepository.save(cheese);
        return ResponseEntity.created(new URI("/api/cheeses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cheese", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cheeses : Updates an existing cheese.
     *
     * @param cheese the cheese to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cheese,
     * or with status 400 (Bad Request) if the cheese is not valid,
     * or with status 500 (Internal Server Error) if the cheese couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cheeses")
    @Timed
    public ResponseEntity<Cheese> updateCheese(@RequestBody Cheese cheese) throws URISyntaxException {
        log.debug("REST request to update Cheese : {}", cheese);
        if (cheese.getId() == null) {
            return createCheese(cheese);
        }
        Cheese result = cheeseRepository.save(cheese);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cheese", cheese.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cheeses : get all the cheeses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cheeses in body
     */
    @GetMapping("/cheeses")
    @Timed
    public List<Cheese> getAllCheeses() {
        log.debug("REST request to get all Cheeses");
        List<Cheese> cheeses = cheeseRepository.findAll();
        return cheeses;
    }

    /**
     * GET  /cheeses/:id : get the "id" cheese.
     *
     * @param id the id of the cheese to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cheese, or with status 404 (Not Found)
     */
    @GetMapping("/cheeses/{id}")
    @Timed
    public ResponseEntity<Cheese> getCheese(@PathVariable Long id) {
        log.debug("REST request to get Cheese : {}", id);
        Cheese cheese = cheeseRepository.findOne(id);
        return Optional.ofNullable(cheese)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cheeses/:id : delete the "id" cheese.
     *
     * @param id the id of the cheese to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cheeses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCheese(@PathVariable Long id) {
        log.debug("REST request to delete Cheese : {}", id);
        cheeseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cheese", id.toString())).build();
    }

}
