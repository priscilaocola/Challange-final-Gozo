package com.example.challengefinal.growshop.Repositorios;

import com.example.challengefinal.growshop.models.OrdenProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OrdenProductoRepositorio extends JpaRepository<OrdenProducto, Long> {
}
