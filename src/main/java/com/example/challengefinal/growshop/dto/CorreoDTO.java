package com.example.challengefinal.growshop.dto;

public class CorreoDTO {
    private String remitente; // Dirección de correo del remitente
    private String comentario;
    private String asunto;

    public CorreoDTO() {
    }

    public String getRemitente() {
        return remitente;
    }

    public String getComentario() {
        return comentario;
    }

    public String getAsunto() {
        return asunto;
    }
}
