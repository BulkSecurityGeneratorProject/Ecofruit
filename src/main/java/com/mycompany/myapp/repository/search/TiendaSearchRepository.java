package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Tienda;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tienda entity.
 */
public interface TiendaSearchRepository extends ElasticsearchRepository<Tienda, Long> {
}
