package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Receta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Receta entity.
 */
@SuppressWarnings("unused")
public interface RecetaRepository extends JpaRepository<Receta,Long> {

    @Query("select receta from Receta receta where receta.user.login = ?#{principal.username}")
    Page<Receta> findByUserIsCurrentUser(Pageable pageable);

   /* @Query("select comentario from Comentario comentario, Receta receta where receta.id=comentario.receta and comentario.receta=:id")
    Page<Receta> findComentarioByReceta(@Param("id") long id, Pageable pageable);*/

}
