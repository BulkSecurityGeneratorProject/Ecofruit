package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Tienda;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.TiendaRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.TiendaSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tienda.
 */
@RestController
@RequestMapping("/api")
public class TiendaResource {

    private final Logger log = LoggerFactory.getLogger(TiendaResource.class);

    @Inject
    private TiendaRepository tiendaRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TiendaSearchRepository tiendaSearchRepository;

    /**
     * POST  /tiendas : Create a new tienda.
     *
     * @param tienda the tienda to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tienda, or with status 400 (Bad Request) if the tienda has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tiendas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tienda> createTienda(@RequestBody Tienda tienda) throws URISyntaxException {
        log.debug("REST request to save Tienda : {}", tienda);
        if (tienda.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tienda", "idexists", "A new tienda cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        tienda.setUser(user);
        Tienda result = tiendaRepository.save(tienda);
        tiendaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tiendas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tienda", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tiendas : Updates an existing tienda.
     *
     * @param tienda the tienda to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tienda,
     * or with status 400 (Bad Request) if the tienda is not valid,
     * or with status 500 (Internal Server Error) if the tienda couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tiendas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tienda> updateTienda(@RequestBody Tienda tienda) throws URISyntaxException {
        log.debug("REST request to update Tienda : {}", tienda);
        if (tienda.getId() == null) {
            return createTienda(tienda);
        }
        Tienda result = tiendaRepository.save(tienda);
        tiendaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tienda", tienda.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tiendas : get all the tiendas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tiendas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tiendas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Tienda>> getAllTiendas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tiendas");
        Page<Tienda> page = tiendaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tiendas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tiendas/:id : get the "id" tienda.
     *
     * @param id the id of the tienda to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tienda, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tiendas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tienda> getTienda(@PathVariable Long id) {
        log.debug("REST request to get Tienda : {}", id);
        Tienda tienda = tiendaRepository.findOne(id);
        return Optional.ofNullable(tienda)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tiendas/:id : delete the "id" tienda.
     *
     * @param id the id of the tienda to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tiendas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTienda(@PathVariable Long id) {
        log.debug("REST request to delete Tienda : {}", id);
        tiendaRepository.delete(id);
        tiendaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tienda", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tiendas?query=:query : search for the tienda corresponding
     * to the query.
     *
     * @param query the query of the tienda search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/tiendas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Tienda>> searchTiendas(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Tiendas for query {}", query);
        Page<Tienda> page = tiendaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tiendas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
