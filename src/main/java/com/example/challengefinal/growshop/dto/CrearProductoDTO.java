package com.example.challengefinal.growshop.dto;

import com.example.challengefinal.growshop.models.Categoria;
import com.example.challengefinal.growshop.models.Producto;

public class CrearProductoDTO {
    private Long id;

    private String nombre;

    private String descripcion;

    private double precio;

    private Categoria tipoDeCategoria;

    private long cantidad;

    private String imagen;

    public CrearProductoDTO() {
    }


    public CrearProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.tipoDeCategoria = producto.getCategoria();
        this.cantidad = producto.getCantidad();
        this.imagen = producto.getImagen();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public Categoria getCategoria() {
        return tipoDeCategoria;
    }

    public long getCantidad() {
        return cantidad;
    }

    public String getImagen() {
        return imagen;
    }
}
