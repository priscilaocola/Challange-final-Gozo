package com.example.challengefinal.growshop.dto;

public class ClienteRegistroDTO {

    private  long id;

    private  String nombre, apellido, email, direccion, contraseña, telefono;

    private int edad;

    public ClienteRegistroDTO() {
    }

    public ClienteRegistroDTO(String nombre, String apellido, String email, String direccion, String contraseña, String telefono, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.direccion = direccion;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.edad = edad;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getEdad() {
        return edad;
    }
}
