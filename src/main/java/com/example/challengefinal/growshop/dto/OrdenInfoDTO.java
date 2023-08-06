package com.example.challengefinal.growshop.dto;

import com.example.challengefinal.growshop.models.OrdenProducto;

public class OrdenInfoDTO {
    private long id;

    private double total;

    private int totalProductos;

    private String nombre;

    public OrdenInfoDTO(){}
    public OrdenInfoDTO(OrdenProducto ordenProducto){
        this.id = ordenProducto.getId();
        this.total = ordenProducto.getPrecioUnitario();
        this.totalProductos = ordenProducto.getCantidadDeProductos();
        this.nombre = ordenProducto.getNombre();
    }

    public long getId() {
        return id;
    }

    public double getTotal() {
        return total;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public String getNombre() {
        return nombre;
    }
}