package com.example.challengefinal.growshop.dto;

import com.example.challengefinal.growshop.models.Categoria;
import com.example.challengefinal.growshop.models.Producto;

public class ProductoDTO {

    private Long id;

    private String nombre;
    private String marca;

    private String descripcion;
    private String currency_id;
    private double precio;

    private Categoria tipoDeCategoria;
    private String subCategoria;
    private String imagen;
    private long cantidad;
    private boolean activo;

    public ProductoDTO(){}

    public ProductoDTO(Producto producto) {
        id = producto.getId();
        nombre = producto.getNombre();
        marca = producto.getMarca();
        descripcion = producto.getDescripcion();
        precio = producto.getPrecio();
        tipoDeCategoria = producto.getCategoria();
        subCategoria = producto.getSubCategoria();
        imagen = producto.getImagen();
        cantidad = producto.getCantidad();
        activo = producto.isActivo();
        currency_id = producto.getCurrency_id();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {return marca;}

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public Categoria getCategoria() {
        return tipoDeCategoria;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public String getImagen() {return imagen;}

    public long getCantidad() {
        return cantidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getCurrency_id() {
        return currency_id;
    }
}
