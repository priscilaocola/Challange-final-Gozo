package com.example.challengefinal.growshop.Repositorios;

import com.example.challengefinal.growshop.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    Cliente findByEmail(String email);
}
