package com.example.challengefinal.growshop.Repositorios;

import com.example.challengefinal.growshop.models.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OrdenRepositorio extends JpaRepository<Orden, Long> {
    Orden findByNumeroDeOrden(String numeroDeOrden);
}
