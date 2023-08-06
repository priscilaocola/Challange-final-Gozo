package com.example.challengefinal.growshop.dto;

import com.example.challengefinal.growshop.models.Orden;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class OrdenDTO {

    private Long id;

    private String numeroDeOrden;

    private LocalDateTime fecha;

    private Double montoTotal;

    private Set<OrdenProductoDTO> ordenProductos;


    public OrdenDTO(Orden orden) {
        this.id = orden.getId();
        this.numeroDeOrden = orden.getNumeroDeOrden();
        this.fecha = orden.getFecha();
        this.montoTotal = orden.getMontoTotal();
        this.ordenProductos = orden.getOrdenProductos().stream().map(ordenProducto -> new OrdenProductoDTO(ordenProducto)).collect(Collectors.toSet());
    }

    public String getNumeroDeOrden() {
        return numeroDeOrden;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Double getMontoTotal() {return montoTotal;}

    public Set<OrdenProductoDTO> getOrdenProductos() {
        return ordenProductos;
    }
}
