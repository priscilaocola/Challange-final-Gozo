package com.example.challengefinal.growshop.servicios.Implement;

import com.example.challengefinal.growshop.Repositorios.OrdenRepositorio;
import com.example.challengefinal.growshop.dto.OrdenDTO;
import com.example.challengefinal.growshop.models.Orden;
import com.example.challengefinal.growshop.servicios.ServicioOrden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
public class ServicioOrdenImplement implements ServicioOrden {

    @Autowired
    private OrdenRepositorio ordenRepositorio;
    @Override
    public Set<OrdenDTO> traerOrdenesDTO() {
        return ordenRepositorio.findAll().stream().map(orden -> new OrdenDTO(orden)).collect(Collectors.toSet());
    }

    @Override
    public OrdenDTO traerOrdenDTO(Long id) {
        Orden orden = ordenRepositorio.findById(id).orElse(null);
        OrdenDTO ordenDTO = new OrdenDTO(orden);
        return ordenDTO;
    }

    @Override
    public Orden traerOrdenPorNumero(String numeroDeOrden) {
        return ordenRepositorio.findByNumeroDeOrden(numeroDeOrden);
    }

    @Override
    public void save(Orden orden) {
        ordenRepositorio.save(orden);
    }
}
