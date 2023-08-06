package com.example.challengefinal.growshop.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(strategy = "native", name = "native")
    private  long id;

    private String nombre;
    private String marca;
    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    private double precio;

    private Categoria categoria;
    private String subCategoria;
    private String currency_id;
    @Column(name = "imagen", length = 500)
    private String imagen;
    private long cantidad;

    private boolean activo;

    @JsonIgnore
    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    private Set<OrdenProducto> ordenProductos = new HashSet<>();

    public Producto() {
    }

    public Producto(String nombre, String marca, String descripcion, double precio, Categoria categoria, String subCategoria,String imagen, long cantidad, boolean activo, String currency_id) {
        this.nombre = nombre;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.subCategoria = subCategoria;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.activo = activo;
        this.currency_id = currency_id;
    }


    public long getId() {
        return id;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }

    public Set<OrdenProducto> getOrdenProductos() {
        return ordenProductos;
    }

    public void setOrdenProductos(Set<OrdenProducto> ordenProductos) {
        this.ordenProductos = ordenProductos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {return marca;}

    public void setMarca(String marca) {this.marca = marca;}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria tipoDeCategoria) {
        this.categoria = categoria;
    }

    public String getSubCategoria() {return subCategoria;}

    public void setSubCategoria(String subCategoria) {this.subCategoria = subCategoria;}

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void añadirOrdenProducto(OrdenProducto ordenProducto){
        ordenProducto.setProductos(this);
        ordenProductos.add(ordenProducto);
    }

    public boolean isActivo() {return activo;}

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
