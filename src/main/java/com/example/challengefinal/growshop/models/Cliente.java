package com.example.challengefinal.growshop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private  String nombre, apellido, email, direccion, contraseña, telefono;

    private int edad;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private Set<Orden> ordenesDeCompra = new HashSet<>();


    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String email, String contraseña, String direccion, String telefono, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.edad = edad;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @JsonIgnore
    public Set<Orden> getOrdenesDeCompra() {
        return ordenesDeCompra;
    }

    public void setOrdenesDeCompra(Set<Orden> ordenesDeCompra) {
        this.ordenesDeCompra = ordenesDeCompra;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void añadirOrdenes(Orden orden){
        orden.setCliente(this);
        ordenesDeCompra.add(orden);
    }
}
