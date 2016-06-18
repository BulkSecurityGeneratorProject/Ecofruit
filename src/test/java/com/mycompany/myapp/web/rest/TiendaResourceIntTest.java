package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.EcofruitApp;
import com.mycompany.myapp.domain.Tienda;
import com.mycompany.myapp.repository.TiendaRepository;
import com.mycompany.myapp.repository.search.TiendaSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TiendaResource REST controller.
 *
 * @see TiendaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcofruitApp.class)
@WebAppConfiguration
@IntegrationTest
public class TiendaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DIRECCION = "AAAAA";
    private static final String UPDATED_DIRECCION = "BBBBB";
    private static final String DEFAULT_CIUDAD = "AAAAA";
    private static final String UPDATED_CIUDAD = "BBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;
    private static final String DEFAULT_HORARIO = "AAAAA";
    private static final String UPDATED_HORARIO = "BBBBB";
    private static final String DEFAULT_TELEFONO = "AAAAA";
    private static final String UPDATED_TELEFONO = "BBBBB";

    @Inject
    private TiendaRepository tiendaRepository;

    @Inject
    private TiendaSearchRepository tiendaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTiendaMockMvc;

    private Tienda tienda;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TiendaResource tiendaResource = new TiendaResource();
        ReflectionTestUtils.setField(tiendaResource, "tiendaSearchRepository", tiendaSearchRepository);
        ReflectionTestUtils.setField(tiendaResource, "tiendaRepository", tiendaRepository);
        this.restTiendaMockMvc = MockMvcBuilders.standaloneSetup(tiendaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tiendaSearchRepository.deleteAll();
        tienda = new Tienda();
        tienda.setNombre(DEFAULT_NOMBRE);
        tienda.setDireccion(DEFAULT_DIRECCION);
        tienda.setCiudad(DEFAULT_CIUDAD);
        tienda.setLatitud(DEFAULT_LATITUD);
        tienda.setLongitud(DEFAULT_LONGITUD);
        tienda.setHorario(DEFAULT_HORARIO);
        tienda.setTelefono(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    public void createTienda() throws Exception {
        int databaseSizeBeforeCreate = tiendaRepository.findAll().size();

        // Create the Tienda

        restTiendaMockMvc.perform(post("/api/tiendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tienda)))
                .andExpect(status().isCreated());

        // Validate the Tienda in the database
        List<Tienda> tiendas = tiendaRepository.findAll();
        assertThat(tiendas).hasSize(databaseSizeBeforeCreate + 1);
        Tienda testTienda = tiendas.get(tiendas.size() - 1);
        assertThat(testTienda.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTienda.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testTienda.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testTienda.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testTienda.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testTienda.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testTienda.getTelefono()).isEqualTo(DEFAULT_TELEFONO);

        // Validate the Tienda in ElasticSearch
        Tienda tiendaEs = tiendaSearchRepository.findOne(testTienda.getId());
        assertThat(tiendaEs).isEqualToComparingFieldByField(testTienda);
    }

    @Test
    @Transactional
    public void getAllTiendas() throws Exception {
        // Initialize the database
        tiendaRepository.saveAndFlush(tienda);

        // Get all the tiendas
        restTiendaMockMvc.perform(get("/api/tiendas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tienda.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
                .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD.toString())))
                .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.doubleValue())))
                .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.doubleValue())))
                .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
                .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())));
    }

    @Test
    @Transactional
    public void getTienda() throws Exception {
        // Initialize the database
        tiendaRepository.saveAndFlush(tienda);

        // Get the tienda
        restTiendaMockMvc.perform(get("/api/tiendas/{id}", tienda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tienda.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD.toString()))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.doubleValue()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.doubleValue()))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTienda() throws Exception {
        // Get the tienda
        restTiendaMockMvc.perform(get("/api/tiendas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTienda() throws Exception {
        // Initialize the database
        tiendaRepository.saveAndFlush(tienda);
        tiendaSearchRepository.save(tienda);
        int databaseSizeBeforeUpdate = tiendaRepository.findAll().size();

        // Update the tienda
        Tienda updatedTienda = new Tienda();
        updatedTienda.setId(tienda.getId());
        updatedTienda.setNombre(UPDATED_NOMBRE);
        updatedTienda.setDireccion(UPDATED_DIRECCION);
        updatedTienda.setCiudad(UPDATED_CIUDAD);
        updatedTienda.setLatitud(UPDATED_LATITUD);
        updatedTienda.setLongitud(UPDATED_LONGITUD);
        updatedTienda.setHorario(UPDATED_HORARIO);
        updatedTienda.setTelefono(UPDATED_TELEFONO);

        restTiendaMockMvc.perform(put("/api/tiendas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTienda)))
                .andExpect(status().isOk());

        // Validate the Tienda in the database
        List<Tienda> tiendas = tiendaRepository.findAll();
        assertThat(tiendas).hasSize(databaseSizeBeforeUpdate);
        Tienda testTienda = tiendas.get(tiendas.size() - 1);
        assertThat(testTienda.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTienda.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testTienda.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testTienda.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testTienda.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testTienda.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testTienda.getTelefono()).isEqualTo(UPDATED_TELEFONO);

        // Validate the Tienda in ElasticSearch
        Tienda tiendaEs = tiendaSearchRepository.findOne(testTienda.getId());
        assertThat(tiendaEs).isEqualToComparingFieldByField(testTienda);
    }

    @Test
    @Transactional
    public void deleteTienda() throws Exception {
        // Initialize the database
        tiendaRepository.saveAndFlush(tienda);
        tiendaSearchRepository.save(tienda);
        int databaseSizeBeforeDelete = tiendaRepository.findAll().size();

        // Get the tienda
        restTiendaMockMvc.perform(delete("/api/tiendas/{id}", tienda.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tiendaExistsInEs = tiendaSearchRepository.exists(tienda.getId());
        assertThat(tiendaExistsInEs).isFalse();

        // Validate the database is empty
        List<Tienda> tiendas = tiendaRepository.findAll();
        assertThat(tiendas).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTienda() throws Exception {
        // Initialize the database
        tiendaRepository.saveAndFlush(tienda);
        tiendaSearchRepository.save(tienda);

        // Search the tienda
        restTiendaMockMvc.perform(get("/api/_search/tiendas?query=id:" + tienda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tienda.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD.toString())))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO.toString())));
    }
}
