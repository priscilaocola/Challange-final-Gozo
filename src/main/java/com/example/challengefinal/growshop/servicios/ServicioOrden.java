package com.example.challengefinal.growshop.servicios;

import com.example.challengefinal.growshop.dto.OrdenDTO;
import com.example.challengefinal.growshop.models.Orden;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

public interface ServicioOrden {

    Set<OrdenDTO> traerOrdenesDTO();
    OrdenDTO traerOrdenDTO(@PathVariable Long id);
    Orden traerOrdenPorNumero(String numeroDeOrden);
    void save(Orden orden);
}
