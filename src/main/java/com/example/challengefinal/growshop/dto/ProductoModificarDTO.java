package com.example.challengefinal.growshop.dto;

import com.example.challengefinal.growshop.models.Categoria;
import com.example.challengefinal.growshop.models.Producto;

public class ProductoModificarDTO {
    private Long id;
    private String nombre;
    private String marca;
    private String descripcion;
    private double precio;
    private long cantidad;

    public ProductoModificarDTO(){}

    public ProductoModificarDTO(Producto producto){
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.marca = producto.getMarca();
        this.descripcion = producto.getDescripcion();
        this.cantidad = producto.getCantidad();
        this.precio = producto.getPrecio();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public long getCantidad() {
        return cantidad;
    }

}
