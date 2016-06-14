package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Producto entity.
 */
@SuppressWarnings("unused")
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    @Query("select producto from Producto producto where producto.subcategoria=:productosFruta")
    Page<Producto> findByFruta(@Param("productosFruta") Integer productosFrutas, Pageable pageable);

    @Query("select producto from Producto producto, Subcategoria subcategoria where producto.subcategoria = subcategoria.id and producto.subcategoria=1")
    Page<Producto> findBySubcategoriaIsFruta(Pageable pageable);

    @Query("select producto from Producto producto, Subcategoria subcategoria where producto.subcategoria = subcategoria.id and producto.subcategoria=33")
    Page<Producto> findBySubcategoriaIsCarne(Pageable pageable);

    @Query("select producto from Producto producto, Subcategoria subcategoria where producto.subcategoria = subcategoria.id and producto.subcategoria=65")
    Page<Producto> findBySubcategoriaIsVerdura(Pageable pageable);

    @Query("select producto from Producto producto, Subcategoria subcategoria where producto.subcategoria = subcategoria.id and producto.subcategoria=66")
    Page<Producto> findBySubcategoriaIsBatidos(Pageable pageable);

    //@Query("select producto from Producto producto,Subcategoria subcategoria where producto.subcategoria_id=subcategoria.id and producto.subcategoria_id=1")
    //Page<Producto> findProductoByFruta(Pageable pageable);

   // @Query("select producto from Producto producto where producto.subcategoria=1")
   // Page<Producto> findProductoFruta(Pageable pageable);

    /*
    @Query("select jugador from Jugador jugador where jugador.asistencias >=:totalAsist")
    Page<Jugador> Asist(@Param("totalAsist") Integer totalAsist, Pageable pageable);

    select comentario from Comentario comentario, Receta receta where receta.id=comentario.receta_id and receta_id=
     */
}
