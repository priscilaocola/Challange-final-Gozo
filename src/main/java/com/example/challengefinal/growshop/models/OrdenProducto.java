package com.example.challengefinal.growshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class OrdenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double precioUnitario;

    private int cantidadDeProductos;

    private String nombre;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ordenDeCompra_id")
    private Orden orden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;


    public OrdenProducto() {
    }

    public OrdenProducto(double precioUnitario, int cantidadDeProductos, String nombre) {
        this.precioUnitario = precioUnitario;
        this.cantidadDeProductos = cantidadDeProductos;
        this.nombre = nombre;
    }


    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {this.precioUnitario = precioUnitario;}

    public int getCantidadDeProductos() {
        return cantidadDeProductos;
    }

    public void setCantidadDeProductos(int cantidadDeProductos) {
        this.cantidadDeProductos = cantidadDeProductos;
    }

    public Orden getOrdenDeCompra() {
        return orden;
    }

    public void setOrdenDeCompra(Orden orden) {
        this.orden = orden;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProductos(Producto producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
