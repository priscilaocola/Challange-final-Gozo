package com.example.challengefinal.growshop.servicios.Implement;

import com.example.challengefinal.growshop.Repositorios.ClienteRepositorio;
import com.example.challengefinal.growshop.dto.ClienteDTO;
import com.example.challengefinal.growshop.models.Cliente;
import com.example.challengefinal.growshop.servicios.ServicioCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioClienteImplement implements ServicioCliente {

    @Autowired
    public ClienteRepositorio clienteRepositorio;

    @Override
    public List<ClienteDTO> traerClientesDTO() {
        return clienteRepositorio.findAll().stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
    }

    @Override
    public ClienteDTO traerClientePorId(Long id) {
        Cliente cliente = clienteRepositorio.findById(id).orElse(null);
        ClienteDTO clienteDTO = new ClienteDTO(cliente);
        return clienteDTO;
    }

    @Override
    public Cliente traerClientePorEmail(String email) {
        return clienteRepositorio.findByEmail(email);
    }

    @Override
    public void save(Cliente cliente) {
         clienteRepositorio.save(cliente);
    }
}
