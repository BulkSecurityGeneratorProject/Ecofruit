package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Comentario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Comentario entity.
 */
@SuppressWarnings("unused")
public interface ComentarioRepository extends JpaRepository<Comentario,Long> {

    @Query("select comentario from Comentario comentario where comentario.user.login = ?#{principal.username}")
    Page<Comentario> findByUserIsCurrentUser(Pageable pageable);

    @Query("select comentario from Comentario comentario, Receta receta where receta.id=comentario.receta and comentario.receta= 1")
    Page<Comentario> findComentarioByReceta(Pageable pageable);



   /* @Query(value="select comentario from Comentario comentario where comentario.id ORDER BY comentario.id desc", nativeQuery = true)
    Page<Comentario> findIdDesc(Pageable pageable);

    @Query("select comentario from Comentario comentario")
    Page<Comentario> findByTextoOrderByIdDesc(Pageable pageable);
    /*
    select comentario from Comentario comentario, Receta receta where receta.id=comentario.receta_id and receta_id={{receta.id}};
    */
}
