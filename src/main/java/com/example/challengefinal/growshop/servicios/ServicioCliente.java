package com.example.challengefinal.growshop.servicios;

import com.example.challengefinal.growshop.dto.ClienteDTO;
import com.example.challengefinal.growshop.models.Cliente;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ServicioCliente {

    List<ClienteDTO> traerClientesDTO();
    ClienteDTO traerClientePorId(@PathVariable Long id);

    Cliente traerClientePorEmail(String email);
    void save (Cliente cliente);
}
