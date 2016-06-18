package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tienda;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tienda entity.
 */
@SuppressWarnings("unused")
public interface TiendaRepository extends JpaRepository<Tienda,Long> {

    @Query("select tienda from Tienda tienda where tienda.user.login = ?#{principal.username}")
    List<Tienda> findByUserIsCurrentUser();

}
